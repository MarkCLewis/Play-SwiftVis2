package jvmplots

import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import scala.Vector
import java.io.BufferedInputStream
import java.io.InputStream

object CartAndRad {
  def read(file:File, ymin:Double = Double.MinValue, ymax:Double = Double.MaxValue):IndexedSeq[Array[Double]] = {
    readStream(new FileInputStream(file))
  }

  def readStream(is: InputStream, ymin:Double = Double.MinValue, ymax:Double = Double.MaxValue):IndexedSeq[Array[Double]] = {
    val dis = new DataInputStream(new BufferedInputStream(is))
    val num = readInt(dis)
    val parts = (0 until num).view.map(i => readCart(dis, i)).filter(p => p(1) > ymin && p(1) < ymax).force.toArray
    var j = 0
    for(i <- parts.indices) yield {
      while(j < parts(i)(6)) {
        readDouble(dis)  // Skip particle with lower indices
        j += 1
      }
      parts(i)(6) = readDouble(dis)
      j += 1
    }
    dis.close
    parts
  }
  
  
  private def readCart(dis:DataInputStream, i:Double):Array[Double] = {
    Array(readDouble(dis),readDouble(dis),readDouble(dis),readDouble(dis),readDouble(dis),readDouble(dis), i)
  }

  val FileRegex = """.*CartAndRad.(\d+).bin""".r
  def findAllInDir(dir: File): Array[(String, Int)] = {
    for (fname@FileRegex(step) <- dir.list()) yield (fname, step.toInt)
  }

  def readInt(dis: DataInputStream) = Integer.reverseBytes(dis.readInt())

  def readDouble(dis: DataInputStream) = java.lang.Double.longBitsToDouble(java.lang.Long.reverseBytes(dis.readLong))

//  val data = read(new File("/home/mlewis/Rings/JoshCDAP/a=100000:q=2.8:min=2e-9:max=2e-8:rho=0.7:sigma=200/CartAndRad.1020.bin"))
//  println(data(0))
//  println(data(1))
//  println(data(2))
}