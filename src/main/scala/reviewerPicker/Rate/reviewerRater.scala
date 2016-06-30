package reviewerPicker.reviewerRater

import akka.actor.{Actor, Props }
import reviewerPicker.model._

object ReviewerRater {

  def props: Props =
    Props(new ReviewerRater)

  case class Rate(good: String, bad: String, ok: String)
  case class ReviewerRich(name: String, score: Int, count: Int)
  case class ReviewersRich(reviewers: Seq[ReviewerRich])
  case object Shutdown

}

class ReviewerRater extends Actor {
  import ReviewerRater._

  override def receive: Receive = {
    case Rate(good, bad, ok) => sender() ! rate(good, bad, ok)

    case Shutdown => context.stop(self)
  }

  def rate(good: String, bad: String, ok: String): Reviewers = {
    val goodReviews = scoreRatings(findReviewers(good), "good")
    val badReviews = scoreRatings(findReviewers(bad), "bad")
    val okReviews = scoreRatings(findReviewers(ok), "ok")
    val allReviews = goodReviews ++ badReviews ++ okReviews
    flattenRatings(allReviews).asInstanceOf[Reviewers]
  }

  def findReviewers(input: String): List[Reviewer] = {
    input.split(",")
      .map(movie => MovieData.values.find(x => x.title == movie).getOrElse(Nil))
      .asInstanceOf[List[MovieData]]
      .map(movieData => movieData.reviewers)
      .map(reviewers => reviewers.reviewers)
      .flatten
      .asInstanceOf[List[Reviewer]]
  }

  def scoreRatings(reviewers: List[Reviewer], score: String): List[Reviewer] = score match {
    case "good" => reviewers.map(x => if (x.score > 75) Reviewer(x.name, 1) else Reviewer(x.name, -1))
    case "ok" => reviewers.map(x => if (x.score <= 75 && x.score >= 50 ) Reviewer(x.name, 1) else Reviewer(x.name, -1))
    case "bad" => reviewers.map(x => if (x.score < 50) Reviewer(x.name, 1) else Reviewer(x.name, -1))
  }

  def flattenRatings(reviewers: List[Reviewer]): List[Reviewer] = {
    reviewers.groupBy(_.name)
      .mapValues(_.map(_.score))
      .mapValues(_.foldLeft(0)(_ + _))
      .map{case(key, value) => Reviewer(key,value)}
      .toList
  }

}
