package tlschannel

import java.nio.ByteBuffer
import java.nio.channels.ByteChannel

class ChunkingByteChannel(impl: ByteChannel, chunkSize: Int) extends ByteChannel {
  
  def close() = impl.close()
  def isOpen() = impl.isOpen()

  def read(in: ByteBuffer): Int = {
    if (!in.hasRemaining)
      return 0
    val oldLimit = in.limit
    try {
      val readSize = math.min(chunkSize, in.remaining)
      in.limit(in.position + readSize)
      impl.read(in)
    } finally {
      in.limit(oldLimit)
    }
  }

  def write(out: ByteBuffer): Int = {
    if (!out.hasRemaining)
      return 0
    val oldLimit = out.limit
    try {
      val writeSize = math.min(chunkSize, out.remaining)
      out.limit(out.position + writeSize)
      impl.write(out)
    } finally {
      out.limit(oldLimit)
    }
  }
  
}