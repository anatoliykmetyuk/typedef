val ScalaVer = "2.11.8"
val Paradise = "2.1.0"

scalaVersion := ScalaVer

lazy val commonSettings = Seq(
  scalaVersion := ScalaVer
, crossScalaVersions := Seq("2.10.2", "2.10.3", "2.10.4", "2.10.5", "2.10.6", "2.11.0", "2.11.1", "2.11.2", "2.11.3", "2.11.4", "2.11.5", "2.11.6", "2.11.7", "2.11.8")
, resolvers += Resolver.sonatypeRepo("snapshots")
, resolvers += Resolver.sonatypeRepo("releases")
, addCompilerPlugin("org.scalamacros" % "paradise" % Paradise cross CrossVersion.full)
)

lazy val example = (project in file("example"))
  .settings(commonSettings)
  .dependsOn(macros)

lazy val macros = (project in file("macros"))
  .settings(commonSettings)
  .settings(
    libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _),
    libraryDependencies ++= (
      if (scalaVersion.value.startsWith("2.10")) List("org.scalamacros" %% "quasiquotes" % Paradise)
      else Nil
    )
  , initialCommands := """
      import scala.reflect.runtime.universe._
    """
  )