import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._


val Organization = "fr.iscpif"
val Name = "simparc-UI"
val Version = "0.1.0-SNAPSHOT"
val ScalaVersion = "2.12.4"
val scalatraVersion = "2.5.1"
val jettyVersion = "9.4.6.v20170531"
val json4sVersion = "3.5.2"
val scalatagsVersion = "0.6.7"
val autowireVersion = "0.2.6"
val boopickleVersion = "1.2.6"
val rxVersion = "0.4.0"
val scaladgetVersion = "1.2.1"
val scalajsDomVersion = "0.9.3"
val betterFilesVersion = "3.2.0"
val Resolvers = Seq(Resolver.sonatypeRepo("snapshots"),
    Resolver.sonatypeRepo("releases"),
    Resolver.bintrayRepo("definitelyscala", "maven"),
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)

def viabilitreeVersion = "2.0"

lazy val shared = project.in(file("shared")).settings(
  scalaVersion := ScalaVersion
) enablePlugins (ScalaJSPlugin)

lazy val client = project.in(file("client")) settings(
  version := Version,
  scalaVersion := ScalaVersion,
  resolvers in ThisBuild ++= Resolvers,
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "autowire" % autowireVersion,
    "io.suzaku" %%% "boopickle" % boopickleVersion,
    "com.lihaoyi" %%% "scalatags" % scalatagsVersion,
    "com.lihaoyi" %% "scalarx" % rxVersion,
    "fr.iscpif" %% "scaladget" % scaladgetVersion,
    "org.scala-js" %%% "scalajs-dom" % scalajsDomVersion,
    "org.json4s" %% "json4s-jackson" % json4sVersion,
    "fr.iscpif.scaladget" %%% "bootstrapnative" % scaladgetVersion,
    "fr.iscpif.scaladget" %%% "tools" % scaladgetVersion,
    "fr.iscpif.scaladget" %%% "bootstrapslider" % scaladgetVersion,
    "com.definitelyscala" %%% "scala-js-plotlyjs" % "1.1.8",
    "com.lihaoyi" %%% "sourcecode" % "0.1.2"
  )
) dependsOn (shared) enablePlugins (ScalaJSPlugin)

lazy val server = project.in(file("server")) settings(
  organization := Organization,
  name := Name,
  version := Version,
  scalaVersion := ScalaVersion,
  resolvers ++= Resolvers,
  libraryDependencies ++= Seq(
    "com.lihaoyi" %% "autowire" % autowireVersion,
    "io.suzaku" %% "boopickle" % boopickleVersion,
    "com.lihaoyi" %% "scalatags" % scalatagsVersion,
    "org.scalatra" %% "scalatra" % scalatraVersion,
    //"se.scalablesolutions.akka" %% "akka-util" % "0.8.1",
    "ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
    "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
    "org.eclipse.jetty" % "jetty-webapp" % jettyVersion,
    "org.eclipse.jetty" % "jetty-server" % jettyVersion,
    "fr.iscpif.viabilitree" %% "export" % viabilitreeVersion,
    "fr.iscpif.viabilitree" %% "viability" % viabilitreeVersion,
    "com.github.pathikrit" %% "better-files" % betterFilesVersion
  )
) dependsOn (shared) enablePlugins (ScalatraPlugin)

lazy val go = taskKey[Unit]("go")

lazy val bootstrap = project.in(file("target/bootstrap")) settings(
  version := Version,
  scalaVersion := ScalaVersion,
  go := {

    val demoTarget = (target in server in Compile).value
    val demoResource = (resourceDirectory in client in Compile).value
    val webappTarget = demoTarget / "webapp"
    val demoJS = (fullOptJS in client in Compile).value

    IO.copyFile(demoJS.data, webappTarget / "js/viaduc.js")
    IO.copyFile(dependencyFile.value, webappTarget / "js/deps.js")
    IO.copyDirectory(cssFile.value, webappTarget / "css/")
    IO.copyDirectory(demoResource, demoTarget)
  }
) dependsOn(client, server) enablePlugins (ExecNpmPlugin)