organization  := "com.reviewerFinder"

version       := "0.1.0"

scalaVersion  := "2.11.6"

resolvers += "Elasticsearch releases" at "http://maven.elasticsearch.org/releases/"

libraryDependencies ++= {
  val akkaV = "2.4.6"
  val sprayV = "1.3.3"
  Seq(
    "io.spray"            %%  "spray-can"               % sprayV,
    "com.typesafe.akka"   %%  "akka-actor"              % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"            % akkaV   % "test",
    "com.typesafe.akka"   %%  "akka-http-core"          % akkaV,
    "com.typesafe.akka"   %%  "akka-http-experimental"  % akkaV,
    "com.typesafe.akka"   %%  "akka-http-spray-json-experimental" % akkaV,
    "org.specs2"          %%  "specs2-core"             % "2.3.11" % "test"
  )
}

Revolver.settings
