import sbtcrossproject.{crossProject, CrossType}

lazy val server = (project in file("server")).settings(commonSettings).settings(
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(digest, gzip),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  libraryDependencies ++= Seq(
    "com.vmunier" %% "scalajs-scripts" % "1.1.2",
    guice,
		"org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
    "edu.trinity" %% "swiftvis2swing" % "0.1.0-SNAPSHOT",
    "edu.trinity" %% "swiftvis2fx" % "0.1.0-SNAPSHOT",
    specs2 % Test
  ),
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
  EclipseKeys.preTasks := Seq(compile in Compile)
).enablePlugins(PlayScala).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(commonSettings).settings(
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.7",
		"me.shadaj" %%% "slinky-core" % "0.6.3",
    "me.shadaj" %%% "slinky-web" % "0.6.3",
    "edu.trinity" %%% "swiftvis2js" % "0.1.0-SNAPSHOT"
  ),
  scalacOptions += "-P:scalajs:sjsDefinedByDefault"
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(commonSettings)
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val commonSettings = Seq(
  scalaVersion := "2.12.11",
  organization := "edu.trinity",
  libraryDependencies ++= Seq(
		"com.typesafe.play" %%% "play-json" % "2.8.1"
  )
)

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen {s: State => "project server" :: s}
