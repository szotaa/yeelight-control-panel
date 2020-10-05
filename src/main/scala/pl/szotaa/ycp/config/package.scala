package pl.szotaa.ycp

import io.circe.Decoder
import io.circe.generic.semiauto._

package object config {
  implicit val serverDecoder: Decoder[ServerConfig] = deriveDecoder
  implicit val ycpDecoder: Decoder[YcpConfig] = deriveDecoder
}
