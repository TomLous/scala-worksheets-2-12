package functions.spark

case class OutItem(
    name: String,
    int_value: Int,
    decimal: BigDecimal,
    timestamp: Long,
    ingested_at: Long,
    ingest_id: String
)
