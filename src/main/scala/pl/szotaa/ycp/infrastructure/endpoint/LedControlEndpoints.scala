package pl.szotaa.ycp.infrastructure.endpoint

import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class LedControlEndpoints[F[_]: Sync] extends Http4sDsl[F] {
  def endpoints(): HttpRoutes[F] = ???
}

object LedControlEndpoints {
  def endpoints[F[_]: Sync](): HttpRoutes[F] = new LedControlEndpoints[F].endpoints()
}