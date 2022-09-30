package functions.spark

import java.sql.Timestamp

case class InItem(
    RAW_NAME: String,
    RAW_VALUE: Int,
    OPTIONAL_RAW_DOUBLE: Option[Double],
    SOME_TIME: Timestamp
) extends Convertable [OutItem] {

  override def convert(ingestedAt: Timestamp, ingestedId: String): OutItem =  OutItem(
    RAW_NAME,
    RAW_VALUE,
    OPTIONAL_RAW_DOUBLE.map(BigDecimal.valueOf).getOrElse(0.0),
    SOME_TIME.getTime,
    ingestedAt.getTime,
    ingestedId
  )
}
