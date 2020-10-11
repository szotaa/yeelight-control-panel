package pl.szotaa.ycp.domain

import io.circe.syntax._
import pl.szotaa.ycp.domain.CommandJson._

class LedControlService[F[_]](ledClient: LedClientAlgebra[F]) {

  def command(method: Method): F[String] =
    ledClient.write(buildCommandAsJson(method))

  //TODO: id!!
  private def buildCommandAsJson(method: Method): Array[Byte] = (Command(1, method).asJson.noSpaces + "\r\n").getBytes
}

object LedControlService {
  def apply[F[_]](ledClient: LedClientAlgebra[F]): LedControlService[F] = new LedControlService(ledClient)
}