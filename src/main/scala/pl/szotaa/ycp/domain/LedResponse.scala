package pl.szotaa.ycp.domain

import cats.syntax.functor._
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder}

sealed trait LedResponse extends Product with Serializable

final case class LedSuccess(id: Int, result: List[String]) extends LedResponse

final case class LedError(id: Int, error: Error) extends LedResponse

final case class Error(code: Int, message: String)

object LedResponseJson {

  implicit val encodeLedResponse: Encoder[LedResponse] = Encoder.instance {
    case success @ LedSuccess(_, _) => success.asJson
    case error @ LedError(_, _) => error.asJson
  }

  implicit val decodeLedResponse: Decoder[LedResponse] = List[Decoder[LedResponse]](
    Decoder[LedSuccess].widen,
    Decoder[LedError].widen
  ).reduceLeft(_ or _)
}
