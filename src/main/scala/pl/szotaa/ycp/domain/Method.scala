package pl.szotaa.ycp.domain

sealed trait Param extends Product with Serializable

final case class StringParam(value: String) extends Param

final case class IntParam(value: Int) extends Param

sealed abstract class Method(val params: List[Param]) extends Product with Serializable {
  val name: String
}

final case class SetRgb(rgb: Int, effect: String, duration: Int) extends Method (
  List(
    IntParam(rgb),
    StringParam(effect),
    IntParam(duration)
  )
) {
  override val name: String = "set_rgb"
}
