Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / organization     := "org.somecode"
ThisBuild / organizationName := "Some Code"
ThisBuild / scalaVersion     := "3.2.1"
ThisBuild / version          := "0.9.2-SNAPSHOT"

ThisBuild / scalacOptions    += "--deprecation"

ThisBuild / Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDS")

val v = new {
  val cats                  = "2.9.0"
  val catsEffect            = "3.3.14"
  val ciString              = "1.2.0"
  val circe                 = "0.14.3"
  val fs2                   = "3.3.0"
  val http4s                = "1.0.0-M37"
  val scalatest             = "3.2.13"
  val scalatest_scalacheck  = "3.2.13.0"
  val scalajs               = "1.12.0"      // not used directly (appears in plugins.sbt)
  val scalajsCss            = "1.0.0"
  val scalajsDom            = "2.3.0"
}

lazy val commonLibs = Seq(
  libraryDependencies ++= Seq(
    // actual core libraries
    "org.typelevel"               %%% "cats-core"           % v.cats,
    "org.typelevel"               %%% "cats-effect"         % v.catsEffect,
    "org.typelevel"               %%% "case-insensitive"    % v.ciString,
    "org.scalatest"               %%% "scalatest"           % v.scalatest             % Test,
    "org.scalatestplus"           %%% "scalacheck-1-16"     % v.scalatest_scalacheck  % Test,
    // added for web API (circe codecs, etc)
    "co.fs2"                      %%% "fs2-core"            % v.fs2,
    "io.circe"                    %%% "circe-core"          % v.circe,
    "io.circe"                    %%% "circe-generic"       % v.circe,
    "io.circe"                    %%% "circe-parser"        % v.circe,
  )
)

lazy val jvmLibs = Seq(
  libraryDependencies ++= Seq(
    //"co.fs2"                      %% "fs2-io"               % v.fs2,
    "org.http4s"                  %% "http4s-ember-server"  % v.http4s,
    "org.http4s"                  %% "http4s-ember-client"  % v.http4s,
    "org.http4s"                  %% "http4s-circe"         % v.http4s,
    "org.http4s"                  %% "http4s-dsl"           % v.http4s
  )
)

lazy val jsLibs = Seq(
  libraryDependencies ++= Seq(
    "com.github.japgolly.scalacss"        %%% "core"          % v.scalajsCss,
    "com.github.japgolly.scalacss"        %%% "ext-react"     % v.scalajsCss,
    "org.scala-js"                        %%% "scalajs-dom"   % v.scalajsDom
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
  .jsSettings(jsLibs)

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
  .jsSettings(jsLibs)

lazy val server = project.in(file("server"))
  .enablePlugins(NoPublishPlugin, BuildInfoPlugin)
  .settings(
    name := "enig4s-server",
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "org.somecode.enig4s.server",
    commonLibs
  )
  .dependsOn(core.jvm, jsapi.jvm)
