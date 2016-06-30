package reviewerPicker.reviewerRater

import java.io.File
import scala.io.Source
import reviewerPicker.model._

object MovieData {


  val pathOrig = getClass.getResource("").getPath
  val pathSplit = pathOrig.split("/").dropRight(5).mkString("/")
  val pathFinal = pathSplit + "/resources/movies/"
  val movieFiles = (new File(pathFinal).list)
  val values = movieFiles.map(findValues(_))

  def findValues(file: String): MovieData = {
    val lines = Source.fromFile(file).getLines.toList
    val firstLine = lines.head.split('|').map(_.trim)
    val title = firstLine(0)
    val date = firstLine(1)

    val reviewers = {
      lines.tail.map { line =>
        val values = line.split('|').map(_.trim)
        val name = values(0)
        val score = values(1).toInt

        Reviewer(
          name,
          score
        )
      }.toList.asInstanceOf[Reviewers]
    }

    MovieData(
      title,
      date,
      reviewers
    )
  }
}


  case class MovieData(
                        title: String,
                        year: String,
                        reviewers: Reviewers
                      )

