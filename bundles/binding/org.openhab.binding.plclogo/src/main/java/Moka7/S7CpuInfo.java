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
public class S7CpuInfo {
    
    private final int BufSize = 256;
    protected byte[] Buffer = new byte[BufSize];       

    protected void Update(byte[] Src, int Pos)
    {
        System.arraycopy(Src, Pos, Buffer, 0, BufSize);
    }   
    
    public String ModuleTypeName()
    {
        return S7.GetStringAt(Buffer,172,32);
    }
    public String SerialNumber()
    {
        return S7.GetStringAt(Buffer,138,24);
    }
    public String ASName()
    {
        return S7.GetStringAt(Buffer,2,24);
    }
    public String Copyright()
    {
        return S7.GetStringAt(Buffer,104,26);
    }
    public String ModuleName()
    {
        return S7.GetStringAt(Buffer,36,24);
    }
}
