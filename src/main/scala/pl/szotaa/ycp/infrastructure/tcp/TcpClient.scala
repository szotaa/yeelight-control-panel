package pl.szotaa.ycp.infrastructure.tcp

import java.net.InetSocketAddress

import cats.effect.{Blocker, Concurrent, ContextShift, Resource}
import fs2.Chunk
import fs2.io.tcp.{Socket, SocketGroup}
import pl.szotaa.ycp.domain.LedClientAlgebra


//TODO: error handling, logging, retrieving response
class TcpClient[F[_]: Concurrent: ContextShift](resource: Resource[F, Socket[F]]) extends LedClientAlgebra[F] {

  override def write(message: Array[Byte]): F[Unit] = resource.use {
    client => client.write(Chunk.bytes(message))
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