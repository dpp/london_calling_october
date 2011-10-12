package code.comet

/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 10/12/11
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */

import net.liftweb._
import http._
import common._

import code.snippet._

object MyCart extends SessionVar[Box[Cart]](Empty)

class Cart extends CometActor {
  MyCart.set(Full(this))

  override def localSetup() {
    // register with QUEUE
  }

  override def localShutdown() {
    // unregister from Queue
  }

  private var cart: Vector[Item] = Vector()

  override def lowPriority = {
    case i: Item => cart :+= i ; reRender()
  }

  def render = "li *" #> cart.map(_.toString)
}