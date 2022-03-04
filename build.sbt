Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / organization     := "org.somecode"
ThisBuild / organizationName := "Some Code"
ThisBuild / scalaVersion     := "3.1.1"
ThisBuild / version          := "0.2.1-SNAPSHOT"

ThisBuild / Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDS")

ThisBuild / buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)

val v = new {
  val cats                  = "2.7.0"
  val catsEffect            = "3.3.5"
  val scalatest             = "3.2.10"
  val scalatest_scalacheck  = "3.2.10.0"

  val circe                 = "0.14.1"
  val http4s                = "1.0.0-M30"
  val fs2                   = "3.2.5"
  val monocle               = "3.1.0"
  val scalajs               = "1.8.0" // not used directly (appears in plugins.sbt)
  val scalajsCss            = "1.0.0"
  val scalajsDom            = "2.1.0"
  val scalajsReact          = "2.0.1"
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
    "org.http4s"                  %% "http4s-dsl"           % v.http4s
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
  .enablePlugins(NoPublishPlugin)
  .aggregate(rootJVM, rootJS)

lazy val rootJVM = project
  .enablePlugins(NoPublishPlugin)
  .aggregate(core.jvm, jsapi.jvm, server)

lazy val rootJS = project
  .enablePlugins(NoPublishPlugin)
  .aggregate(core.js, jsapi.js, client)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "enig4s-core",
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
    buildInfoPackage := "org.somecode.enig4s.server",
    commonLibs
  )
  .dependsOn(core.jvm, jsapi.jvm)

lazy val client = project.in(file("client"))
  .enablePlugins(NoPublishPlugin,ScalaJSPlugin)
  .settings(
    name := "enig4s-client",
    scalaJSUseMainModuleInitializer := true,
    commonLibs
  )
  .dependsOn(core.js, jsapi.js)
