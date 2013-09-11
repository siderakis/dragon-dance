import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent._

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 8/21/13
 * Time: 9:16 PM
 * To change this template use File | Settings | File Templates.
 */

trait Sender {
  def dictationCompleted(s: String)

  def receivedChar(c: Char)
}

trait IdleSender extends Sender {

  import ExecutionContext.Implicits.global

  /**
   * Initializes the terminal so that each character is received without buffering,
   * otherwise pressing the "return" key would be necessary.
   **/
  def unbufferTerminal() = {
    import sys.process._
    (Seq("sh", "-c", "stty raw < /dev/tty") !)
    //    (Seq("sh", "-c", "stty -icanon min 1 < /dev/tty") !)
  }

  def bufferTerminal() = {
    import sys.process._
    (Seq("sh", "-c", "stty sane < /dev/tty") !)
  }


  val input = new StringBuffer()
  val noChars = new AtomicInteger(0)

  override def receivedChar(c: Char) = {
    input append c
    noChars set 0
  }

  future {
    def idleLoop(): Unit = {
      println(s"idleLoop: ${noChars.get}")
      Thread sleep 100
      val dictationComplete = input.length != 0 && noChars.getAndIncrement > 10

      if (dictationComplete) {
        dictationCompleted(input.toString)
        input setLength 0
        noChars set 0
      } else {
        idleLoop()
      }

    }

    idleLoop()
  }


}


object DynamicDictationDispatcher {
  def apply(callback: (String) => Unit) = {
    val o = new DynamicDictationDispatcher(callback)

    o.eventLoop()
    o
  }
}

class DynamicDictationDispatcher private(callback: (String) => Unit) extends IdleSender {
  unbufferTerminal()

  var stop = false

  def eventLoop(): Unit =
    if (!stop)
      if (Console.in.ready) Console.in.read match {
        case c =>
          receivedChar(c.toChar)
          eventLoop()
        case _ =>
      } else {
        Thread sleep 100
        eventLoop()
      }

  eventLoop()

  def dictationCompleted(s: String) = {
    stop = true
    bufferTerminal()
    callback(s)
  }
}