package pl.szotaa.ycp.infrastructure.tcp

import java.net.InetSocketAddress

import cats.effect.{Blocker, Concurrent, ContextShift, Resource}
import cats.syntax.all._
import fs2.io.tcp.{Socket, SocketGroup}
import fs2.{Chunk, Stream, text}
import pl.szotaa.ycp.domain.LedClientAlgebra


//TODO: error handling, logging, retrieving response
class TcpClient[F[_]: Concurrent: ContextShift](resource: Resource[F, Socket[F]]) extends LedClientAlgebra[F] {

  override def write(message: Array[Byte]): F[String] = resource.use {
    client => client.write(Chunk.bytes(message)) >> client.reads(8192)
      .through(text.utf8Decode)
      .through(text.lines)
      .interleave(Stream.constant("\n"))
      .head
      .compile
      .string
  }
}

object TcpClient {
  def apply[F[_] : Concurrent : ContextShift](host: String, port: Int): TcpClient[F] = new TcpClient(buildClient(host, port))

  def buildClient[F[_] : Concurrent : ContextShift](host: String, port: Int): Resource[F, Socket[F]] = for {
    blocker <- Blocker[F]
    socketGroup <- SocketGroup[F](blocker)
    client <- socketGroup.client(new InetSocketAddress(host, port))
    _ = client.write(Chunk.bytes(host.getBytes()))
  } yield client
}