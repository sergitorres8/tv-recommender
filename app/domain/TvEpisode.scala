package domain

import play.api.libs.json.Json

case class TvEpisode(originalName: String)

object TvEpisode {
  implicit val tvEpisodeReads = Json.reads[TvEpisode]
}
