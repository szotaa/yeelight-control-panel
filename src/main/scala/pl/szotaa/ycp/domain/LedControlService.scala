package pl.szotaa.ycp.domain

import io.circe.syntax._
import pl.szotaa.ycp.domain.CommandJson._

class LedControlService[F[_]](ledClient: LedClientAlgebra[F]) {

  def setRgb(rgb: Int, effect: String, duration: Int): F[Unit] =
    ledClient.write(buildCommandAsJson(SetRgb(rgb, effect, duration)))

  //TODO: id!!
  private def buildCommandAsJson(method: Method): Array[Byte] = Command(1, method).asJson.noSpaces.getBytes
}

object LedControlService {
  def apply[F[_]](ledClient: LedClientAlgebra[F]): LedControlService[F] = new LedControlService(ledClient)
}