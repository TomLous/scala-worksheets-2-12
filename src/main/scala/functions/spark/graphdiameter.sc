import org.apache.spark.graphx.lib.ShortestPaths
import org.apache.spark.graphx._
import org.apache.spark.graphx.lib.ShortestPaths.SPMap
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions._

import scala.reflect.ClassTag


val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test2")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

val data1 = List(
  (6,4),
  (4,5),
  (3,4),
  (3,2),
  (1,5),
  (5,2),
  (1,2)
).toDF("src", "dst")

val vertices:RDD[(VertexId,Long)] = data1.select(explode(array('src,'dst))).distinct().map(r => (r.getInt(0).toLong,r.getInt(0).toLong)).rdd
val edges:RDD[Edge[String]] = data1.map(r=> Edge(r.getInt(0).toLong,r.getInt(1).toLong, r.getInt(0).toLong + " ->"  +r.getInt(1).toLong)).rdd

//vertices.toDF().show()
//edges.toDF().show()

val graph = Graph(vertices, edges, 0L)

val landmarks = vertices.map(_._1).collect().toSeq

//val sp = ShortestPaths.run(graph, landmarks)
//sp.vertices.toDF().show(false)

object Diameter extends Serializable {
  type SPMap = Map[VertexId, Int]

  def makeMap(x: (VertexId, Int)*) = Map(x: _*)

  def incrementMap(spmap: SPMap): SPMap = spmap.map { case (v, d) => v -> (d + 1) }

  def addMaps(spmap1: SPMap, spmap2: SPMap): SPMap = {
    (spmap1.keySet ++ spmap2.keySet).map {
      k => k -> math.min(spmap1.getOrElse(k, Int.MaxValue), spmap2.getOrElse(k, Int.MaxValue))
    }(collection.breakOut) // more efficient alternative to [[collection.Traversable.toMap]]
  }

  def run[VD, ED: ClassTag](graph: Graph[VD, ED]): Int = {
    val spGraph = graph.mapVertices { (vid, _) => makeMap(vid -> 0)  }

    val initialMessage:SPMap = makeMap()

    def vertexProgram(id: VertexId, attr: SPMap, msg: SPMap): SPMap = {
      addMaps(attr, msg)
    }

    def sendMessage(edge: EdgeTriplet[SPMap, _]): Iterator[(VertexId, SPMap)] = {
      val newSrcAttr = incrementMap(edge.dstAttr)
      val newDstAttr = incrementMap(edge.srcAttr)

      List(
       if (edge.srcAttr != addMaps(newSrcAttr, edge.srcAttr)) Some((edge.srcId, newSrcAttr)) else None,
       if (edge.dstAttr != addMaps(newDstAttr, edge.dstAttr)) Some((edge.dstId, newDstAttr)) else None
      ).flatten.toIterator
    }

    val pregel = Pregel(spGraph, initialMessage)(vertexProgram, sendMessage, addMaps)

    pregel.vertices.first()._2.values.max
  }
}

val sp2 = Diameter.run(graph)
println(sp2)

//type SPMap = Map[VertexId, Int]
//
//
//def incrementMap(spmap: SPMap): SPMap = spmap.map { case (v, d) => v -> (d + 1) }
//
//def addMaps(spmap1: SPMap, spmap2: SPMap): SPMap = {
//  (spmap1.keySet ++ spmap2.keySet).map {
//    k => k -> math.min(spmap1.getOrElse(k, Int.MaxValue), spmap2.getOrElse(k, Int.MaxValue))
//  }(collection.breakOut) // more efficient alternative to [[collection.Traversable.toMap]]
//}
//
//val spGraph = graph.mapVertices { (vid, attr) =>
//  Map(vid -> 0)
//}
//
//spGraph.vertices.toDF().show()
//
//
//def vertexProgram(id: VertexId, attr: SPMap, msg: SPMap): SPMap = {
//  attr ++ msg.map{
//    case (k,v) => k -> v.min(attr.getOrElse(k, Int.MaxValue))
//  }
//}
//
//def sendMessage(edge: EdgeTriplet[SPMap, _]): Iterator[(VertexId, SPMap)] = {
//  val newAttr = edge.dstAttr.mapValues(_ + 1)
//  if (edge.srcAttr != addMaps(newAttr, edge.srcAttr)) Iterator((edge.srcId, newAttr))
//  else Iterator.empty
//}
//
//val p = Pregel(spGraph, Map.empty[VertexId, Int], Int.MaxValue, EdgeDirection.Both)(vertexProgram, sendMessage, addMaps)
//
//p.vertices.toDF().show(false)

//Pregel(graph, 0, Int.MaxValue, EdgeDirection.Both)(vertexProgram, sendMessage, mergeMsg)
