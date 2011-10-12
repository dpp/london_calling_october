package code.comet

import net.liftweb.actor.LiftActor
import xml.NodeSeq
import java.util.Date

/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 10/12/11
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */

object BlogPostManager {
  private var managers: Map[String, BlogPostManager] = Map()
  def find(name: String) = synchronized {
    managers.get(name) match {
      case Some(m) => m
      case _ =>
        val ret = new BlogPostManager(name)
        managers += name -> ret
        ret
    }
  }
}

case class Entry(who: String, content: String)

class BlogPostManager(val name: String) extends LiftActor {
  private var entries: Vector[Entry] = Vector()
  private var listeners: Vector[WaitEntries] = Vector()

  def messageHandler = {
    case GetEntries(f, after) => f(0, entries.drop(after))

    case w@WaitEntries(f, cnt, _) => if (entries.length > cnt) {
      f(cnt, entries.drop(cnt))
    } else {
      listeners :+= w
    }

    case Unlisten(guid) =>
      listeners = listeners.filter {
        case WaitEntries(_, _, g2) => g2 != guid
      }

    case e: Entry => entries :+= e
      var nl: Vector[WaitEntries] = Vector()
      listeners.foreach{
        case w@WaitEntries(f, cnt, _) => if (entries.length > cnt) {
          f(cnt, entries.drop(cnt))
        } else {
          nl :+= w
        }
      }
      listeners = nl
  }
}

case class GetEntries(f: (Int, Vector[Entry]) => Unit, after: Int = 0)
case class WaitEntries(f: (Int, Vector[Entry]) => Unit, after: Int, guid: String)
case class Unlisten(guid: String)