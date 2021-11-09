
ThisBuild / scalaVersion     := "3.0.2"
ThisBuild / version          := "0.2.0-SNAPSHOT"
ThisBuild / organization     := "org.somecode"
ThisBuild / organizationName := "enig4s"

val v = new {
  val cats                  = "2.6.1"
  val catsEffect            = "3.2.9"
  val scalatest             = "3.2.10"
  val scalatest_scalacheck  = "3.2.10.0"

  val circe                 = "0.14.1"
  val http4s                = "1.0.0-M29"
  val fs2                   = "3.1.5"
  val monocle               = "3.1.0"
  val scalajs               = "1.7.1" // not used directly
  val scalajsCss            = "1.0.0"
  val scalajsDom            = "2.0.0"
  val scalajsReact          = "2.0.0"
}

lazy val commonLibs = Seq(
  libraryDependencies ++= Seq(
    // actual core libraries
    "org.typelevel"               %%% "cats-core"           % v.cats,
    "org.typelevel"               %%% "cats-effect"         % v.catsEffect,
    "org.scalatest"               %%% "scalatest"           % v.scalatest             % Test,
    "org.scalatestplus"           %%% "scalacheck-1-15"     % v.scalatest_scalacheck  % Test,
    // added for web API (circe codecs, etc)
    "dev.optics"                  %%% "monocle-core"        % v.monocle,
    "co.fs2"                      %%% "fs2-core"            % v.fs2,
    "io.circe"                    %%% "circe-core"          % v.circe,
    "io.circe"                    %%% "circe-generic"       % v.circe,
    "io.circe"                    %%% "circe-parser"        % v.circe
  )
)

lazy val jvmLibs = Seq(
  libraryDependencies ++= Seq(
    //"co.fs2"                      %% "fs2-io"               % v.fs2,
    "dev.optics"                  %% "monocle-macro"        % v.monocle,
    "org.http4s"                  %% "http4s-blaze-server"  % v.http4s,
    "org.http4s"                  %% "http4s-blaze-client"  % v.http4s,
    "org.http4s"                  %% "http4s-circe"         % v.http4s,
    "org.http4s"                  %% "http4s-dsl"           % v.http4s,
  )
)

lazy val jsLibs = Seq(
  libraryDependencies ++= Seq(
    "com.github.japgolly.scalajs-react"   %%% "core"          % v.scalajsReact,
    "com.github.japgolly.scalajs-react"   %%% "extra"         % v.scalajsReact,
    "com.github.japgolly.scalacss"        %%% "core"          % v.scalajsCss,
    "com.github.japgolly.scalacss"        %%% "ext-react"     % v.scalajsCss,
    "org.scala-js"                        %%% "scalajs-dom"   % v.scalajsDom
  )
)

lazy val root = project.in(file("."))
  .aggregate(enig4s.js, enig4s.jvm)
  .settings(
    publish := {},
    publishLocal := {},
  )

lazy val enig4s = crossProject(JSPlatform, JVMPlatform)
  .in(file("."))
  .settings(
    name := "enig4s",
    scalaVersion := "3.1.0",
    Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDS"),
    commonLibs
  )
  .jvmConfigure(_.in(file("server")).enablePlugins(BuildInfoPlugin))
  .jvmSettings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "org.somecode.enig4s.server",
    jvmLibs
  )
  .jsConfigure(_.in(file("client")).enablePlugins(ScalaJSBundlerPlugin))
  .jsSettings(
    //scalaVersion := "2.13.6", // scala 2 for now
    scalaJSUseMainModuleInitializer := true,
    jsLibs
  )

lazy val server = enig4s.jvm

lazy val client = enig4s.js
