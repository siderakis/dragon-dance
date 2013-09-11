import java.awt.event.InputEvent
import java.awt.Robot
import java.lang.Thread
import java.net.{Socket, ServerSocket}
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import scala.concurrent._
import scala.Predef._

object DragonDance extends Commands with Robotic {

  //Hide the Mac OS dock icon
  System.setProperty("apple.awt.UIElement", "true")

  val robot = new Robot
  val typer = new Keyboard(robot)

  val sendCamel = send(VariableCaseConverters.camelifyWords) _
  val sendHyphen = send(VariableCaseConverters.hyphenWords) _

  def dispatch(command: String): Unit = command match {
    case "copy-line" => tripleClick - copy
    case "paste-here" => click - paste
    case "camel" => DynamicDictationDispatcher(sendCamel)
    case "exit" => print("exiting...")
    case "click" => click
    case _ => println(s"unknown command '$command'")
  }


  def send(f: (String) => String)(dictatedText: String) {
    val processed = f(dictatedText)
    println(s"sending $dictatedText as $processed.")
    switchWindow()
    typer `type` processed
  }


  def main(args: Array[String]) {

    println("ready...")
    val server = new ServerSocket(1201)
    while (true) {
      val s: Socket = server.accept()
      Await.result(future(Server.handleClient(s, dispatch)), 1 minute) match {
        //      future(Server.handleClient(s, dispatch)).map{
        case "exit" => sys.exit()
        case _ =>
      }
    }
  }
}