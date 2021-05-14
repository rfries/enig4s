
ThisBuild / scalaVersion     := "3.0.0-RC3"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "org.somecode"
ThisBuild / organizationName := "enig4s"

val v = new {
  val cats        = "2.6.0"
  val munit       = "0.7.26"
}

lazy val root = (project in file("."))
  .settings(
    name := "enig4s",
    libraryDependencies ++= Seq(
      "org.typelevel"   %% "cats-core"        % v.cats,
      "org.scalameta"   %% "munit"            % v.munit   % Test,
      "org.scalameta"   %% "munit-scalacheck" % v.munit   % Test
    )
  )

