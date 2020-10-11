package pl.szotaa.ycp.infrastructure.endpoint

import cats.effect.Sync
import cats.implicits._
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}
import pl.szotaa.ycp.domain.{LedControlService, LedError, LedSuccess, SetRgb}


class LedCommandEndpoints[F[_]: Sync] extends Http4sDsl[F] {

  implicit val setRgbDecoder: EntityDecoder[F, SetRgb] = jsonOf
  implicit val ledSuccessDecoder: EntityDecoder[F, LedSuccess] = jsonOf
  implicit val ledErrorDecoder: EntityDecoder[F, LedError] = jsonOf

  //TODO: error handling
  private def setRgbEndpoint(service: LedControlService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / "setRgb" =>
        val action = for {
          method <- req.as[SetRgb]
          result <- service.command(method)
        } yield result

        action.flatMap {
          case success @ LedSuccess(_, _) => Ok(success)
          case error @ LedError(_, _) => BadRequest(error)
        }
    }

  def endpoints(service: LedControlService[F]): HttpRoutes[F] = setRgbEndpoint(service)
}

object LedCommandEndpoints {
  def endpoints[F[_]: Sync](service: LedControlService[F]): HttpRoutes[F] = new LedCommandEndpoints[F].endpoints(service)
}