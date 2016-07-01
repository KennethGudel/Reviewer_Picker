package reviewerPicker.reviewerRater

import org.scalatest.{MustMatchers, PropSpec}
import reviewerPicker.model._

class reviewerRaterSpec extends PropSpec with MustMatchers with reviewerRaterUtils{
  property("must properly rate list of reviewers, good") {
    val reviewers = List(Reviewer("-1", 10), Reviewer("1", 80))
    scoreRatings(reviewers, "good") mustBe List(Reviewer("-1", -1), Reviewer("1", 1))
  }

  property("must properly rate list of reviewers, bad") {
    val reviewers = List(Reviewer("-1", 80), Reviewer("1", 1))
    scoreRatings(reviewers, "bad") mustBe List(Reviewer("-1", -1), Reviewer("1", 1))
  }

  property("must properly rate list of reviewers, ok") {
    val reviewers = List(Reviewer("-1", 15), Reviewer("1", 50), Reviewer("-1", 80))
    scoreRatings(reviewers, "ok") mustBe List(Reviewer("-1", -1), Reviewer("1", 1), Reviewer("-1", -1))
  }

  property("must flatten 2 raters add") {
    val reviewers = List(Reviewer("banana", 1), Reviewer("banana", 1))
    flattenRatings(reviewers) mustBe List(Reviewer("banana", 2))
  }

  property("must flatten 2 raters sub") {
    val reviewers = List(Reviewer("banana", 1), Reviewer("banana", -1))
    flattenRatings(reviewers) mustBe List(Reviewer("banana", 0))
  }

  property("must flatten 2 raters and ignore 1") {
    val reviewers = List(Reviewer("banana", 1), Reviewer("banana", -1), Reviewer("egg", 1))
    flattenRatings(reviewers) mustBe List(Reviewer("banana", 0), Reviewer("egg", 1))
  }

}
