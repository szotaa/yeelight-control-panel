package pl.szotaa.ycp.domain

trait LedClientAlgebra[F[_]] {
  def write(message: Array[Byte]): F[String]
}
