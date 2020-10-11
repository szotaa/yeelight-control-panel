package pl.szotaa.ycp

import cats.effect.{ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Resource, Timer}
import io.circe.config.parser
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Router, Server}
import pl.szotaa.ycp.config.YcpConfig
import pl.szotaa.ycp.domain._
import pl.szotaa.ycp.infrastructure.endpoint.LedCommandEndpoints
import pl.szotaa.ycp.infrastructure.tcp.TcpClient

import scala.concurrent.ExecutionContext.Implicits.global

//TODO: automatic discovery of leds instead of reading them from configuration
object Main extends IOApp {

  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, Server[F]] = for {
    conf <- Resource.liftF(parser.decodePathF[F, YcpConfig]("ycp"))
    tcpClient = TcpClient(conf.led.host, conf.led.port)
    ledService = LedControlService.apply(tcpClient)
    httpApp = Router (
      "/command" -> LedCommandEndpoints.endpoints[F](ledService)
    ).orNotFound
    server <- BlazeServerBuilder[F](global)
      .bindHttp(conf.server.port, conf.server.host)
      .withHttpApp(httpApp)
      .resource
  } yield server

  override def run(args: List[String]): IO[ExitCode] = createServer.use(_ => IO.never).as(ExitCode.Success)
}
