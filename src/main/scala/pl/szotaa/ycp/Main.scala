package pl.szotaa.ycp

import cats.effect.{ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Resource, Timer}
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Router, Server}
import pl.szotaa.ycp.config.YcpConfig
import pl.szotaa.ycp.infrastructure.endpoint.LedControlEndpoints
import io.circe.config.parser
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends IOApp {

  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, Server[F]] = for {
    conf <- Resource.liftF(parser.decodePathF[F, YcpConfig]("ycp"))
    httpApp = Router (
      "/control" -> LedControlEndpoints.endpoints[F]()
    ).orNotFound
    server <- BlazeServerBuilder[F](global)
      .bindHttp(conf.serverConfig.port, conf.serverConfig.host)
      .withHttpApp(httpApp)
      .resource
  } yield server

  override def run(args: List[String]): IO[ExitCode] = createServer.use(_ => IO.never).as(ExitCode.Success)
}
