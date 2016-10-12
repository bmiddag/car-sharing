import play.Project._

name := "autodelen"

version := "1.0"

playJavaSettings

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  Resolver.url("Objectify Play Repository", url("http://schaloner.github.io/releases/"))(Resolver.ivyStylePatterns),
  Resolver.url("Objectify Play Snapshot Repository", url("http://schaloner.github.io/snapshots/"))(Resolver.ivyStylePatterns)
)

libraryDependencies ++= Seq(
  "com.typesafe" %% "play-plugins-mailer" % "2.1-RC2",
  "com.typesafe.akka" %% "akka-actor" % "2.2.0",
  "mysql" % "mysql-connector-java" % "5.1.29",
  "org.seleniumhq.selenium" % "selenium-java" % "2.40.0" % "test",
  "be.objectify" %% "deadbolt-java" % "2.2-RC4",
  "org.xhtmlrenderer" % "flying-saucer-pdf" % "9.0.4",
  "nu.validator.htmlparser" % "htmlparser" % "1.4",
  "org.jfree" % "jfreechart" % "1.0.17",
  "org.apache.poi" % "poi" % "3.8",
  "org.apache.poi" % "poi-ooxml" % "3.9"
)

