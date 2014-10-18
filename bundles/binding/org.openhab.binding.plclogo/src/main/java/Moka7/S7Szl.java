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
public class S7Szl {
    
    public int LENTHDR;
    public int N_DR;
    public int DataSize;
    public byte Data[];
    
    public S7Szl(int BufferSize)
    {
        Data = new byte[BufferSize];
    }
    protected void Copy(byte[] Src, int SrcPos, int DestPos, int Size)
    {
        System.arraycopy(Src, SrcPos, Data, DestPos, Size);
    }   
}
