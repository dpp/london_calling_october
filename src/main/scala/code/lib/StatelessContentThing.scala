package code.lib

import net.liftweb.http.rest.{RestContinuation, RestHelper}
import code.comet._


/**
 * Created by IntelliJ IDEA.
 * User: dpp
 * Date: 10/12/11
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */

import net.liftweb._
import util._
import http.LiftResponse
import json._
import JsonDSL._

object StatelessContentThing extends RestHelper {


  serve {
    case "live_blog" :: name :: Helpers.AsInt(start) :: Nil JsonGet _ =>
      RestContinuation.async {
        satisfyRequest => {

          def sendBack(cnt: Int, v: Vector[Entry]) {
           val jv = ("pos" -> cnt) ~
             ("entries" -> v.toList.map(e => ("author" -> e.who) ~
               ("content" -> e.content)))
            satisfyRequest(jv)
          }

          BlogPostManager.find(name) ! WaitEntries(sendBack,
          start, Helpers.randomString(10))
        }
        }
  }
}