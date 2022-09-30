package functions.spark

import java.sql.Timestamp
import java.time.Instant
import java.util.UUID
import scala.util.Try

case class CDFRecord[Out <: Product, In <: Convertable[Out]](value: In) {
  def convert: Option[Out] = {
    Try {
      val ingestedAt = Timestamp.from(Instant.now())
      val ingestId = UUID.randomUUID().toString
      value.convert(ingestedAt, ingestId)
    }.toOption
  }
}
