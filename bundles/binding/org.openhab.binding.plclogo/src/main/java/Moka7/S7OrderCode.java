/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Moka7;

/**
 *
 * @author Davide
 */
public class S7OrderCode {
  
    public int V1;
    public int V2;
    public int V3;
    protected byte[] Buffer = new byte[1024];       

    protected void Update(byte[] Src, int Pos, int Size)
    {
        System.arraycopy(Src, Pos, Buffer, 0, Size);
        V1 = (byte) Src[Size-3];
        V2 = (byte) Src[Size-2];
        V3 = (byte) Src[Size-1];
    }   

    public String Code()
    {
        return S7.GetStringAt(Buffer, 2, 20);
    }
}
