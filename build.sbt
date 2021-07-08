
ThisBuild / scalaVersion     := "3.0.0"
ThisBuild / version          := "0.2.0-SNAPSHOT"
ThisBuild / organization     := "org.somecode"
ThisBuild / organizationName := "enig4s"

ThisBuild / Test / testOptions += Tests.Argument("-oD")


val v = new {
  val cats                  = "2.6.1"
  val munit                 = "0.7.26"
  val scalatest             = "3.2.9"
  val stp_scalacheck        = "3.2.9.0"
}

lazy val root = (project in file("."))
  .settings(
    name := "enig4s",
    Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDS"),
    libraryDependencies ++= Seq(
      "org.typelevel"     %% "cats-core"          % v.cats,
      "org.scalactic"     %% "scalactic"          % v.scalatest,
      "org.scalatest"     %% "scalatest"          % v.scalatest       % Test,
      "org.scalatestplus" %% "scalacheck-1-15"    % v.stp_scalacheck  % Test,
      "org.scalameta"     %% "munit"              % v.munit           % Test,
      "org.scalameta"     %% "munit-scalacheck"   % v.munit           % Test
    )
  )

