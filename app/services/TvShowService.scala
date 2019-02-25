package services

import javax.inject.Inject
import play.api.libs.ws._

import scala.concurrent.{ExecutionContext, Future}

class TvShowService @Inject()(wSClient: WSClient)(implicit executionContext: ExecutionContext) {
  def getTvShowFor(url: String): Future[WSResponse] = {
    buildRequest(url)
      .get()

  }

  private def buildRequest(url: String) = {
    wSClient
      .url(url)
      .withHttpHeaders(("api_key", getApikey()))
      .withQueryStringParameters(("language", "en-US"), ("external_source", "imdb_id"))
  }

  private def getApikey(): String = {
    "some-key"
  }

}
