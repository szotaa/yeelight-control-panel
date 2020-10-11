package pl.szotaa.ycp.domain

import cats.Functor
import cats.syntax.all._
import io.circe.parser.decode
import io.circe.syntax._
import pl.szotaa.ycp.domain.CommandJson._
import pl.szotaa.ycp.domain.LedResponseJson._

class LedControlService[F[_]: Functor](ledClient: LedClientAlgebra[F]) {

  def command(method: Method): F[LedResponse] =
    ledClient.write(buildCommandAsJson(method))
      .map(string => decode[LedResponse](string))
      .map(x => x.right.get)


  //TODO: id!!
  private def buildCommandAsJson(method: Method): Array[Byte] = (Command(1, method).asJson.noSpaces + "\r\n").getBytes
}

object LedControlService {
  def apply[F[_]: Functor](ledClient: LedClientAlgebra[F]): LedControlService[F] = new LedControlService(ledClient)
}