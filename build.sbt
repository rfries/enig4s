
ThisBuild / scalaVersion     := "3.0.0-RC2"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "org.somecode"
ThisBuild / organizationName := "enig4s"

val v = new {
  val scalatest = "3.2.7"
}

lazy val root = (project in file("."))
  .settings(
    name := "enig4s",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % v.scalatest
    )
  )

