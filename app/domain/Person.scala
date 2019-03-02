package domain

import play.api.libs.json.Json

case class Person(name: String)

object Person {
  implicit val personReads = Json.reads[Person]
}