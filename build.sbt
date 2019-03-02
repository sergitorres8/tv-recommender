name := """tv-recommender"""
organization := "com"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"
val scalazVersion = "7.3.0-M22"

libraryDependencies ++= Seq(
  guice,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.1" % Test,
  "org.mockito" % "mockito-core" % "2.11.0" % Test,
  "org.scalaz" %% "scalaz-core" % scalazVersion,
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.binders._"