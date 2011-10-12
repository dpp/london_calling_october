package code.comet

/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 10/12/11
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */

import net.liftweb._
import http._
import common._
import util._
import Helpers._

object CurViewer extends SessionVar[Box[CometActor]](Empty)

class EntryViewer extends CometActor {
  CurViewer.set(Full(this))

  private var myName = ""
  private var myEntries: Vector[Entry] = Vector()

  def register() {
    BlogPostManager.find(myName) ! WaitEntries((cnt, v) => {
      this ! (cnt -> v)
    },
    myEntries.length, Helpers.randomString(10))
  }

  override def lowPriority = {
    case SetName(name) =>
      println("*** in set name "+name+" and myName "+myName)
      if (name != myName) {
        myName = name
      myEntries = Vector()
      register()
        // note deregister old
    }

    case (cnt: Int, v: Vector[Entry]) => myEntries = v; reRender()
    register()
  }

  def render = "li *" #> myEntries.map{e => e.toString}
}

case class SetName(name: String)