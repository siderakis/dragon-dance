import java.net.Socket
import scala.io.Source

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 9/9/13
 * Time: 7:23 AM
 * To change this template use File | Settings | File Templates.
 */
object Server {


  def handleClient(s: Socket, dispatch: (String) => Unit): String = {
    //    while (s.isConnected) {
    //looping
    val ss = Source fromInputStream s.getInputStream

    ss.getLines.map {
      line =>
        println("line: " + line)
        ss.close()
        s.close()
        dispatch(line)
        line
    }.toStream.head

  }

  //  }

}
