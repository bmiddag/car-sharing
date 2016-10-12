import sbt._
import Keys._
import play.Project._

object ModulesSettings {

  val modulesTask = TaskKey[Unit]("modules", "Compiles the modules.") := {
     Process(Seq("python","./compile_modules.py")).!
  }
}
