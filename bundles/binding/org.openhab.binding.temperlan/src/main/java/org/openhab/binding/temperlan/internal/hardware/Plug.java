package org.openhab.binding.temperlan.internal.hardware;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Plug
{
  public static final int READTIMEOUT = 2000;
  public static final int TIMEOUT = 5000;
  byte[] data = new byte[1000];
  private BufferedInputStream inputStream;
  private PrintStream outputStream;
  byte[] read = null;
  private Socket socket = new Socket();
  
  public Plug(String paramString, int paramInt)
    throws IOException
  {
    this.socket.connect(new InetSocketAddress(paramString, paramInt), 5000);
    this.socket.setSoTimeout(2000);
    this.inputStream = new BufferedInputStream(this.socket.getInputStream());
    this.outputStream = new PrintStream(this.socket.getOutputStream(), true);
  }
  
  public void close()
  {
    try
    {
      if (this.socket != null) {
        this.socket.close();
      }
      return;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }
  
  

  public byte[] send()
    throws IOException
  {

	byte[] getTempByteArray = new byte[]{(byte) 0xbb, (byte) 0x80, (byte) 0x00};
	  
    this.outputStream.write(getTempByteArray);
    this.outputStream.flush();
    int i = this.inputStream.read(this.data);
    if (i == -1) {
      i = 1;
    }
    this.read = new byte[i];
    for (int j = 0;; j++)
    {
      if (j >= i)
      {
        //("datareceive", bytesToHex(this.read));
        return this.read;
      }
      this.read[j] = this.data[j];
    }
  }
}