package reviewerPicker.model

case class Reviewer(name: String, score: Int)

case class Reviewers(reviewers: List[Reviewer])
