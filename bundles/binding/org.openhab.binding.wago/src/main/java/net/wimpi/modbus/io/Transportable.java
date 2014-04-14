package net.wimpi.modbus.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Interface defining a transportable class.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public interface Transportable {

  /**
   * Returns the number of bytes that will
   * be written by {@link #writeTo(DataOutput)}.
   *
   * @return the number of bytes that will be written as <tt>int</tt>.
   */
  public int getOutputLength();

  /**
   * Writes this <tt>Transportable</tt> to the
   * given <tt>DataOutput</tt>.
   *
   * @param dout the <tt>DataOutput</tt> to write to.
   * @throws java.io.IOException if an I/O error occurs.
   */
  public void writeTo(DataOutput dout)
      throws IOException;

  /**
   * Reads this <tt>Transportable</tt> from the given
   * <tt>DataInput</tt>.
   *
   * @param din the <tt>DataInput</tt> to read from.
   * @throws java.io.IOException if an I/O error occurs or the data
   *         is invalid.
   */
  public void readFrom(DataInput din)
      throws IOException;

}//interface Transportable
