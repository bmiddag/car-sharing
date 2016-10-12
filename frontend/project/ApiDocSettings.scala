import sbt._
import Keys._
import play.Project._

// Adapted from: https://github.com/yesnault/Play20StartApp
object ApiDocSettings {

  val apiDocTask = TaskKey[Unit]("api-doc", "run scaladoc and javadoc, placing results in target/api") <<= (fullClasspath in Test, compilers, streams) map { (classpath, cs, s) =>

  val apiDir = "target/doc/api"

  IO.delete(file(apiDir))

  // Scaladoc
  var scalaVersionForSbt = Option(System.getProperty("scala.version")).getOrElse("2.10.0")

  val sourceFiles =
    (file("app") ** "*.scala").get ++
    (file("test") ** "*.scala").get ++
    (file("target/scala-" + scalaVersionForSbt + "/src_managed/main/views/html") ** "*.scala").get
  new Scaladoc(10, cs.scalac)("Scala API", sourceFiles, classpath.map(_.data), file(apiDir + "/scala"), Nil, s.log)

  // Javadoc App
  val javaSourcesApp = file("app")
  val javaApiTargetApp = file(apiDir + "/javadoc-app")
  val javaClasspathApp = classpath.map(_.data).mkString(":")
  val javaPackagesApp = "controllers:executors:models:utils:views"

  val cmdApp = <x>javadoc -linksource -sourcepath {javaSourcesApp} -d {javaApiTargetApp} -subpackages {javaPackagesApp} -classpath {javaClasspathApp}</x>
  //println("Executing: "+cmd.text)
  cmdApp ! s.log

  println("(app) API documentation in " + apiDir)

  // Javadoc DBModule
  val javaSourcesDBModule = file("modules/DBModule/src/")
  val javaApiTargetDBModule = file(apiDir + "/javadoc-dbmodule")
  val javaClasspathDBModule = classpath.map(_.data).mkString(":")
  val javaPackagesDBModule = "exceptions:interfaces:jdbc:objects"

  val cmdDBModule = <x>javadoc -linksource -sourcepath {javaSourcesDBModule} -d {javaApiTargetDBModule} -subpackages {javaPackagesDBModule} -classpath {javaClasspathDBModule}</x>
  //println("Executing: "+cmd.text)
  cmdDBModule ! s.log

  println("(dbmodule) API documentation in " + apiDir)


  // Javadoc AuthModule
  val javaSourcesAuthModule = file("modules/AuthModule/src/")
  val javaApiTargetAuthModule = file(apiDir + "/javadoc-authmodule")
  val javaClasspathAuthModule = classpath.map(_.data).mkString(":")
  val javaPackagesAuthModule = "edran"

  val cmdAuthModule = <x>javadoc -linksource -sourcepath {javaSourcesAuthModule} -d {javaApiTargetAuthModule} -subpackages {javaPackagesAuthModule} -classpath {javaClasspathAuthModule}</x>
  //println("Executing: "+cmd.text)
  cmdAuthModule ! s.log

  println("(authmodule) API documentation in " + apiDir)


  // Javadoc tests
  val javaSourcesTests = file("test")
  val javaApiTargetTests = file(apiDir + "/javadoc-tests")
  val javaClasspathTests = classpath.map(_.data).mkString(":")
  val javaPackagesTests = "java"

  val cmdTests = "cd test && javadoc -linksource  -d ../" + {javaApiTargetTests} + " -classpath " + {javaClasspathTests} + " *.java"
  //println("Executing: "+cmd.text)
  Process(Seq("bash","-c",cmdTests)).!

  println("(tests) API documentation in " + apiDir)
  }
}
