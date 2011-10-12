package code 
package snippet 

import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import code.lib._
import Helpers._
import xml.{Unparsed, NodeSeq, Text}
import model.User
import net.liftweb.http.{SHtml, TransientRequestVar, SessionVar, RequestVar}

object Foo extends RequestVar[Int](Helpers.randomInt(50))

object Session extends SessionVar("Hi")
object Trans extends TransientRequestVar("TV")

object CurrentUserId extends SessionVar[Box[Long]](Full(1L))
object CurrentUser extends RequestVar(CurrentUserId.get.flatMap(User.find))
class Baz {
  def render = {
    Foo.set(5)
    "*" #> ("Foo <script>alert('hi');</script> is "+Foo.get)
  }
}

class Ajaxy {

  def render: NodeSeq => NodeSeq = {
    var x = 0
    
    SHtml.idMemoize(renderThingy =>
      "@Foo *" #> x &
        "@reset [onclick]" #> SHtml.ajaxInvoke(() => {
          x = 0
          renderThingy.setHtml()
        }) &
        "@add [onclick]" #> SHtml.ajaxInvoke(() =>{
          x += 1
          renderThingy.setHtml()
        }
        )
    )
  }

}

class HelloWorld {
  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  def render = <i>Hi</i>

  // replace the contents of the element with id "time" with the date
  def howdy = "#time *" #> date.map{d => Foo.atomicUpdate(_ + 1);
  d.toString+" "+Foo.get}

  def show = ".weasel" #> data.map(d => ("@name *" #> d.name &
    "@age *" #> d.age))

  def data = List(Record("David", 47), Record("Daniel", 7),
  Record("Annette", 29))
}

case class Record(name: String, age: Int)


