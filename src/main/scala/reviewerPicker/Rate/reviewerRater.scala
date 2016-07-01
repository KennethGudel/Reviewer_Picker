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

class ReviewerRater extends Actor with reviewerRaterUtils{
  import ReviewerRater._

  override def receive: Receive = {
    case Rate(good, bad, ok) => sender() ! rate(good, bad, ok)

    case Shutdown => context.stop(self)
  }

}
