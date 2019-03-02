package services

import domain.{TvShow, TvShowResponse}
import domain.errors.TvShowError
import javax.inject.Inject
import play.api.libs.json._
import play.api.libs.ws._
import play.mvc.Http.Status._
import scalaz.{-\/, \/, \/-}

import scala.concurrent.{ExecutionContext, Future}

class TvShowService @Inject()(wSClient: WSClient)(implicit executionContext: ExecutionContext) {
  def getTvShowFor(url: String): Future[TvShowError \/ TvShowResponse] = {
    buildRequest(url)
      .get()
      .map(response => parseResponse(response))
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

  private def parseResponse(response: WSResponse): TvShowError \/ TvShowResponse = {
    response.status match {
      case OK => \/-(readResponse(response))
      case BAD_REQUEST => -\/(TvShowError())
    }
  }

  private def readResponse(response: WSResponse) = {
    response.json.validate[TvShowResponse] match {
      case s: JsSuccess[TvShowResponse] => s.get
    }
  }
}
