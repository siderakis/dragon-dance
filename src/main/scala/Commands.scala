import java.awt.event.InputEvent
import java.awt.event.KeyEvent.{VK_META => VK_COMMAND, _}
import java.awt.Robot

trait Robotic {
  val robot: Robot
}

trait Commands {

  this: Robotic =>

  def switchWindow = {
    robot keyPress VK_COMMAND
    robot keyPress VK_TAB
    robot keyRelease VK_COMMAND
    robot keyRelease VK_TAB
    Thread sleep 100
    Command
  }

  case class Command() {
    def -(c: Command): Command = this
  }

  def click = {
    robot.mousePress(InputEvent.BUTTON1_MASK)
    robot.mouseRelease(InputEvent.BUTTON1_MASK)
    Command()
  }

  def doubleClick = click - click

  def tripleClick = click - click - click

  def press(key: Int) = {
    robot keyPress key
    robot keyRelease key
  }

  def cut = {
    robot keyPress VK_COMMAND
    press(VK_X)
    robot keyRelease VK_COMMAND
    Command()
  }


  def paste = {
    robot keyPress VK_COMMAND
    robot keyPress VK_V
    robot keyRelease VK_V
    robot keyRelease VK_COMMAND
    Command()
  }

  def copy = {
    robot keyPress VK_COMMAND
    robot keyPress VK_C
    robot keyRelease VK_C
    robot keyRelease VK_COMMAND
    Command()
  }

}
