package com.reviewerFinder

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import scala.io.StdIn
import akka.Done
import akka.http.scaladsl.model.StatusCodes



object WebServer {

  case class movies(movies: Map[String, Int])
  case class reviewers(reviewers: Map[String, Int])

  implicit val moviesFormat = jsonFormat1(movies)
  implicit val reviewersFormat = jsonFormat1(reviewers)

  def main(args: Array[String]) {

    implicit val system = ActorSystem("system")
    implicit val materializer = ActorMaterializer()

    implicit val executionContext = system.dispatcher

    val route =
      get {
        pathPrefix("movies") {
          pathEnd {
            complete("")
          }
          path(Segment) { movies =>
            complete(reviewers(Map("Ebert" -> 79, "Banana Sam" -> 100)))
          }
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  }
}
