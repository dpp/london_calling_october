package code.snippet

import net.liftweb.http.SHtml
import net.liftweb._
import http.js.JsCmds.SetValById
import util._
import common._
import Helpers._
/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 10/12/11
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */

import code.comet._
import code.model._
import scala.xml._

class EnterIt {
  def render = {
    var name = ""
    var author = ""

    def postIt(content: String) = {
      CurViewer.foreach {
        cv =>
          println("Sending to "+cv)
          cv ! SetName(name)
      }
      val mgr = BlogPostManager.find(name)

      mgr ! Entry(author, content)
      SetValById("content", "")
    }

    "@name" #> SHtml.text("", name = _) &
    "@author" #> SHtml.text("", author = _) &
    "#content" #> SHtml.textarea("", postIt _)
  }
}