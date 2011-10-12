package code.snippet

/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 10/12/11
 * Time: 1:29 PM
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb._
import util._
import Helpers._
import scala.xml._
import http._
import code.comet._
import js._
import JsCmds._

case class Item(name: String, price: Int)

class Items {
  def render = "li *" #> items.map(i =>
    Text(i.name + " " + i.price) ++
      SHtml.ajaxButton(<span>add to cart</span>,
      () => {
        println("**** Dude... got a "+i)
        MyCart.foreach(_ ! i)
        Noop
      }))

  def items = List(Item("Cat Food", 20),
    Item("Dog Food", 15))
}

class ChooseStock {
  def render(ns: NodeSeq) = <lift:comet type="Cart">{ns}</lift:comet>
}