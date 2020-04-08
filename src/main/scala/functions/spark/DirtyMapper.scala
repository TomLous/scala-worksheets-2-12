package functions.spark

import org.apache.spark.sql.Row

import scala.util.Try

object DirtyMapper extends Serializable {
      val mapRowsToResults: Row => Option[DirtyResult] = (row: Row) => {
        Try {
          DirtyResult(
            row.getString(1).toInt,
            row.getString(2).toInt,
            row.getString(3),
            row.getString(4).toDouble
          )
        }.toOption
      }

}
