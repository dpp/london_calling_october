package code.lib

import net.liftweb._
import common._
import http._
import http.rest.{XmlSelect, JsonSelect, RestHelper}
import util._
import Helpers._

/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 10/12/11
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */

import code.model._
import code.snippet._
// object

object API extends RestHelper {

    implicit def userToResp: JxCvtPF[User] = {
    case (JsonSelect, c, _) => c.asJs
    case (XmlSelect, c, _) => c.toXml
  }
  
  serveJx[User] {
    case "api" :: User(u) :: _ Get _ => u
  }

  serve {
    case Get("template" :: AsInt(n) :: _, _) =>
      Foo.set(n)
      XhtmlResponse(S.runTemplate(List("index")) match {
        case Full(n: scala.xml.Node) => n
        case Full(n) => n(0)
        case _ => <b>Darn</b>
      }, Empty, Nil, Nil, 200, false)
      
    case XmlPost("api" :: Nil, xml -> req) => <a>foo</a>
      
    case  XmlGet("api" :: "stuff" :: s :: "morestuff" :: _ , req) =>
      for {
        n <- asInt(s) ?~! "Not even a number, dude"
        user <- User.find(n) ?~ "User Not Found" ~> 401
      } yield <name>{user.firstName}</name>

  }
}