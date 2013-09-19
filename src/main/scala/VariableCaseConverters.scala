/**
 * Created with IntelliJ IDEA.
 * User: Nick
 * Date: 8/19/13
 * Time: 6:19 PM
 * To change this template use File | Settings | File Templates.
 */
object VariableCaseConverters {
  def hyphenWords: (String) => String = words => words.toLowerCase.trim.replace(" ", "-")


  val camelifyWords: String => String = words => camelifyMethod(words.trim.toLowerCase.replaceAll(" ", "_"))


  /**
   * Turns a string of format "foo_bar" into camel case "FooBar"
   *
   * @param name the String to CamelCase
   *
   * @return the CamelCased string
   */
  def camelify(name: String): String = {
    def loop(x: List[Char]): List[Char] = (x: @unchecked) match {
      case '_' :: '_' :: rest => loop('_' :: rest)
      case '_' :: c :: rest => Character.toUpperCase(c) :: loop(rest)
      case '_' :: Nil => Nil
      case c :: rest => c :: loop(rest)
      case Nil => Nil
    }
    if (name == null)
      ""
    else
      loop('_' :: name.toList).mkString
  }

  /**
   * Turn a string of format "foo_bar" into camel case with the first letter in lower case: "fooBar"
   * This function is especially used to camelCase method names.
   *
   * @param name the String to CamelCase
   *
   * @return the CamelCased string
   */
  def camelifyMethod(name: String): String = {
    val tmp: String = camelify(name)
    if (tmp.length == 0)
      ""
    else
      tmp.substring(0, 1).toLowerCase + tmp.substring(1)
  }

  //http://scala-tools.org/mvnsites/liftweb-2.0/framework/scaladocs/net/liftweb/util/StringHelpers.scala.html
}
