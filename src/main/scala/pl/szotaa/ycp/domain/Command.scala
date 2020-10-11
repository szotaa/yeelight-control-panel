package pl.szotaa.ycp.domain

import io.circe.{Encoder, Json}

case class Command(id: Int, method: Method)

object CommandJson {
  implicit val commandEncoder: Encoder[Command] = (command: Command) => Json.obj(
    ("id", Json.fromInt(command.id)),
    ("method", Json.fromString(command.method.name)),
    ("params", Json.arr(command.method.params.map {
      case IntParam(value) => Json.fromInt(value)
      case StringParam(value) => Json.fromString(value)
    }: _*))
  )
}
