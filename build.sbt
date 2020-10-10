organization := "pl.szotaa"
name := "yeelight-control-panel"
version := "0.1"

val scalaVersion = "2.13.3"
val catsVersion = "2.1.1"
val fs2Version = "2.4.4"
val http4sVersion = "0.21.6"
val circeVersion = "0.13.0"
val circeConfigVersion = "0.8.0"

libraryDependencies ++= Seq(
  "org.typelevel"     %% "cats-core"                    % catsVersion,
  "co.fs2"            %% "fs2-core"                     % fs2Version,
  "co.fs2"            %% "fs2-reactive-streams"         % fs2Version,
  "co.fs2"            %% "fs2-io"                       % fs2Version,
  "org.http4s"        %% "http4s-blaze-server"          % http4sVersion,
  "org.http4s"        %% "http4s-circe"                 % http4sVersion,
  "org.http4s"        %% "http4s-dsl"                   % http4sVersion,
  "io.circe"          %% "circe-core"                   % circeVersion,
  "io.circe"          %% "circe-generic"                % circeVersion,
  "io.circe"          %% "circe-parser"                 % circeVersion,
  "io.circe"          %% "circe-config"                 % circeConfigVersion,
)

scalacOptions ++= Seq(
  "-encoding", "utf8",
  "-Xfatal-warnings",
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps"
)