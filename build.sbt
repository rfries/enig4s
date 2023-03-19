Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / organization     := "org.somecode"
ThisBuild / organizationName := "SomeCode"
ThisBuild / scalaVersion     := "3.2.2"
ThisBuild / version          := "0.9.3-SNAPSHOT"

ThisBuild / scalacOptions    += "--deprecation"

ThisBuild / Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDS")

val v = new {
  val cats                  = "2.9.0"
  val catsEffect            = "3.4.6"
  val ciString              = "1.2.0"
  val circe                 = "0.14.3"
  val fs2                   = "3.6.0"
  val http4s                = "1.0.0-M39"
  val scalatest             = "3.2.15"
  val scalatest_scalacheck  = "3.2.15.0"
  // note that the scalajs version is defined in plugins.sbt
}

lazy val commonLibs = Seq(
  libraryDependencies ++= Seq(
    // actual core libraries
    "org.typelevel"               %%% "cats-core"           % v.cats,
    "org.typelevel"               %%% "cats-effect"         % v.catsEffect,
    "org.typelevel"               %%% "case-insensitive"    % v.ciString,
    "org.scalatest"               %%% "scalatest"           % v.scalatest             % Test,
    "org.scalatestplus"           %%% "scalacheck-1-17"     % v.scalatest_scalacheck  % Test,
    // added for web API (circe codecs, etc)
    "co.fs2"                      %%% "fs2-core"            % v.fs2,
    "io.circe"                    %%% "circe-core"          % v.circe,
    "io.circe"                    %%% "circe-generic"       % v.circe,
    "io.circe"                    %%% "circe-parser"        % v.circe
  )
)

lazy val jvmLibs = Seq(
  libraryDependencies ++= Seq(
    "org.http4s"                  %% "http4s-ember-server"  % v.http4s,
    "org.http4s"                  %% "http4s-ember-client"  % v.http4s,
    "org.http4s"                  %% "http4s-circe"         % v.http4s,
    "org.http4s"                  %% "http4s-dsl"           % v.http4s
  )
)

lazy val root = project.in(file("."))
  .enablePlugins(NoPublishPlugin)
  .aggregate(core.jvm, core.js, jsapi.jvm, jsapi.js, server)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "enig4s-core",
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "org.somecode.enig4s",
    commonLibs
  )
  .jvmSettings(jvmLibs)
  .jsConfigure(_.enablePlugins(ScalaJSBundlerPlugin))

lazy val jsapi = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("jsapi"))
  .enablePlugins(BuildInfoPlugin)
  .dependsOn(core)
  .settings(
    name := "enig4s-jsapi",
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "org.somecode.enig4s.jsapi",
    commonLibs
  )
  .jvmSettings(jvmLibs)
  .jsConfigure(_.enablePlugins(ScalaJSBundlerPlugin))

lazy val server = project.in(file("server"))
  .enablePlugins(NoPublishPlugin, BuildInfoPlugin)
  .settings(
    name := "enig4s-server",
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "org.somecode.enig4s.server",
    commonLibs
  )
  .dependsOn(core.jvm, jsapi.jvm)
