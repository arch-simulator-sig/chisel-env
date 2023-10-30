val chiselVersion = "3.5.6"

lazy val commonSettings = Seq(
  scalaVersion := "2.13.10",
  scalacOptions ++= Seq(
    "-deprecation",
    "-unchecked"
  ),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.json4s" %% "json4s-jackson" % "4.0.6",
    "org.scalatest" %% "scalatest" % "3.2.14" % "test"
  )
)

lazy val chiselSettings = Seq(
  libraryDependencies ++= Seq(
    "edu.berkeley.cs" %% "chisel3" % chiselVersion,
    "edu.berkeley.cs" %% "chiseltest" % "0.5.5"
  ),
  addCompilerPlugin(("edu.berkeley.cs" % "chisel3-plugin" % chiselVersion).cross(CrossVersion.full))
)

lazy val difftest = (project in file("difftest"))
  .settings(commonSettings, chiselSettings)

lazy val Nanshan = (project in file("."))
  .settings(commonSettings, chiselSettings)
  .dependsOn(difftest)
