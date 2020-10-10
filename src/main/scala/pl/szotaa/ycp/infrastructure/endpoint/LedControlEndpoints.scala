package pl.szotaa.ycp.infrastructure.endpoint

import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._
import cats.implicits._
import org.http4s.dsl.Http4sDsl
import pl.szotaa.ycp.domain.LedControlService

class LedControlEndpoints[F[_]: Sync] extends Http4sDsl[F] {

  //TODO: this is a test endpoint, fix me
  private def ledControlEndpoint(service: LedControlService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / "turnOn" =>
        service.setRgb(444, "sudden", 500).flatMap(_ =>
          Ok()
        )
    }

  def endpoints(service: LedControlService[F]): HttpRoutes[F] = ledControlEndpoint(service)
}

object LedControlEndpoints {
  def endpoints[F[_]: Sync](service: LedControlService[F]): HttpRoutes[F] = new LedControlEndpoints[F].endpoints(service)
}