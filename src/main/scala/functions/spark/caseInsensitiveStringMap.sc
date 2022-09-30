import org.apache.spark.sql.util.CaseInsensitiveStringMap
import scala.collection.JavaConverters._

val schemaOptions = new CaseInsensitiveStringMap(Map("caller_id" -> "a",
    "environment" -> "b",
    "schema_namespace" -> "c",
    "schema_name" -> "d",
    "schema_version" -> "e").asJava)

val option2 = Map("caller_id" -> "a",
  "environment" -> "b",
  "schema_namespace" -> "c",
  "schema_name" -> "d",
  "schema_version" -> "e").asJava
