package reviewerPicker.api

import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import reviewerPicker.reviewerRater.ReviewerRater._
import reviewerPicker.reviewerRater.ReviewerRater
import akka.http.scaladsl.model.StatusCodes
import reviewerPicker.model._
import akka.pattern.ask
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout
import akka.http.scaladsl.server.Directives._
import scala.concurrent.duration._
import akka.http.scaladsl.model._

import scala.util.{Failure, Success}

object WebServer {

  def main(args: Array[String]) {

    implicit val reviewerFormat = jsonFormat2(Reviewer)
    implicit val reviewersFormat = jsonFormat1(Reviewers)
    implicit val system = ActorSystem("system")
    implicit val materializer = ActorMaterializer()
    implicit val timeout = Timeout(5 seconds)

    implicit val executionContext = system.dispatcher

    val route =
      get {
        path("hello") {
          complete("hello")
        } ~
        path("movies" / "good" / Segment / "bad" / Segment / "ok" / Segment) { (good, bad, ok) => {
          val ratingActor = system.actorOf(ReviewerRater.props)
          val ratedReviewers = (ratingActor ? Rate(good, bad, ok)).mapTo[Reviewers]
          onComplete(ratedReviewers) {
            case Success(ratedReviewers) =>
              ratingActor ! Shutdown
              complete(ratedReviewers)
            case Failure(ratedReviewers) =>
              ratingActor ! Shutdown
              complete(HttpResponse(StatusCodes.InternalServerError))
          }
        }
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  }
}
