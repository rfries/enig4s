
ThisBuild / scalaVersion     := "3.0.0"
ThisBuild / version          := "0.2.0-SNAPSHOT"
ThisBuild / organization     := "org.somecode"
ThisBuild / organizationName := "enig4s"

val v = new {
  val cats                  = "2.6.1"
  val munit                 = "0.7.26"
  val scalatest             = "3.2.9"
  val scalatest_scalacheck  = "3.2.9.0"
}

lazy val root = (project in file("."))
  .settings(
    name := "enig4s",
    libraryDependencies ++= Seq(
      "org.typelevel"     %% "cats-core"        % v.cats,
      "org.scalactic"     %% "scalactic"        % v.scalatest,
      "org.scalatest"     %% "scalatest"        % v.scalatest % Test,
      "org.scalatestplus" %% "scalacheck-1-15"  % v.scalatest_scalacheck % Test,
      "org.scalameta"     %% "munit"            % v.munit   % Test,
      "org.scalameta"     %% "munit-scalacheck" % v.munit   % Test
    )
  )

