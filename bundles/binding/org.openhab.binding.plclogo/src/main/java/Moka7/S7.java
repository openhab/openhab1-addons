/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Moka7;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Calendar;

/**
 *
 * @author Davide
 */


// Step 7 Constants and Conversion helper class
public class S7 {
    // S7 ID Area (Area that we want to read/write)
    public static final int S7AreaPE = 0x81;
    public static final int S7AreaPA = 0x82;
    public static final int S7AreaMK = 0x83;
    public static final int S7AreaDB = 0x84;
    public static final int S7AreaCT = 0x1C;
    public static final int S7AreaTM = 0x1D;   
    // Connection types
    public static final byte PG = 0x01;
    public static final byte OP = 0x02;
    public static final byte S7_BASIC = 0x03;
    // Block type
    public static final int Block_OB   = 0x38;
    public static final int Block_DB   = 0x41;
    public static final int Block_SDB  = 0x42;
    public static final int Block_FC   = 0x43;
    public static final int Block_SFC  = 0x44;
    public static final int Block_FB   = 0x45;
    public static final int Block_SFB  = 0x46;
    // Sub Block Type
    public static final int SubBlk_OB  = 0x08;
    public static final int SubBlk_DB  = 0x0A;
    public static final int SubBlk_SDB = 0x0B;
    public static final int SubBlk_FC  = 0x0C;
    public static final int SubBlk_SFC = 0x0D;
    public static final int SubBlk_FB  = 0x0E;
    public static final int SubBlk_SFB = 0x0F;
    // Block languages
    public static final int BlockLangAWL       = 0x01;
    public static final int BlockLangKOP       = 0x02;
    public static final int BlockLangFUP       = 0x03;
    public static final int BlockLangSCL       = 0x04;
    public static final int BlockLangDB        = 0x05;
    public static final int BlockLangGRAPH     = 0x06;
    // PLC Status
    public static final int S7CpuStatusUnknown = 0x00;
    public static final int S7CpuStatusRun     = 0x08;
    public static final int S7CpuStatusStop    = 0x04;
    // Type Var
    public static final int S7TypeBool = 1;   
    public static final int S7TypeInt = 1;   
    
    // Returns the bit at Pos.Bit 
    public static boolean GetBitAt(byte[] Buffer, int Pos, int Bit)
    {
        int Value = Buffer[Pos] & 0x0FF;
        byte[] Mask = {
            (byte)0x01,(byte)0x02,(byte)0x04,(byte)0x08,
            (byte)0x10,(byte)0x10,(byte)0x40,(byte)0x80
        };   
        if (Bit<0) Bit=0;
        if (Bit>7) Bit=7;
        
        return (Value & Mask[Bit])!=0;    
    }  
    /**
     * Returns a 16 bit unsigned value : from 0 to 65535 (2^16-1)
     * @param Buffer
     * @param Pos start position
     * @return
     */
    public static int GetWordAt(byte[] Buffer, int Pos)
    {
        int hi = (Buffer[Pos] & 0x00FF);
        int lo = (Buffer[Pos+1] & 0x00FF);
        return (hi<<8)+lo;
    }  
    
    // Returns a 16 bit signed value : from -32768 to 32767
    public static int GetShortAt(byte[] Buffer, int Pos)
    {
        int hi = (Buffer[Pos]);
        int lo = (Buffer[Pos+1] & 0x00FF);
        return ((hi<<8)+lo);
    }  

    // Returns a 32 bit unsigned value : from 0 to 4294967295 (2^32-1)
    public static long GetDWordAt(byte[] Buffer, int Pos)
    {
        long Result;
        Result=(long)(Buffer[Pos] & 0x0FF);
        Result<<=8;
        Result+=(long)(Buffer[Pos+1] & 0x0FF);
        Result<<=8;
        Result+=(long)(Buffer[Pos+2] & 0x0FF);
        Result<<=8;
        Result+=(long)(Buffer[Pos+3] & 0x0FF);
        return Result;
    }  

    // Returns a 32 bit signed value : from 0 to 4294967295 (2^32-1)
    public static int GetDIntAt(byte[] Buffer, int Pos)
    {
        int Result;
        Result=  Buffer[Pos];
        Result<<=8;
        Result+=(Buffer[Pos+1] & 0x0FF);
        Result<<=8;
        Result+=(Buffer[Pos+2] & 0x0FF);
        Result<<=8;
        Result+=(Buffer[Pos+3] & 0x0FF);
        return Result;
    }  

    // Returns a 32 bit floating point
    public static float GetFloatAt(byte[] Buffer, int Pos)
    {
        int IntFloat = GetDIntAt(Buffer, Pos);
        return Float.intBitsToFloat(IntFloat);
    }

    // Returns an ASCII string
    public static String GetStringAt(byte[] Buffer, int Pos, int MaxLen)
    {
        byte[] StrBuffer = new byte[MaxLen];
        System.arraycopy(Buffer, Pos, StrBuffer, 0, MaxLen);
        String S;   
        try {
            S = new String(StrBuffer, "UTF-8"); // the charset is UTF-8
        } catch (UnsupportedEncodingException ex) {
            S = "";
        }
        return S;
    }
    
    public static String GetPrintableStringAt(byte[] Buffer, int Pos, int MaxLen)
    {
        byte[] StrBuffer = new byte[MaxLen];
        System.arraycopy(Buffer, Pos, StrBuffer, 0, MaxLen);
        for (int c = 0; c < MaxLen; c++)
        {
            if ((StrBuffer[c]<31) || (StrBuffer[c]>126))
                StrBuffer[c]=46; // '.'
        }
        String S;   
        try {
            S = new String(StrBuffer, "UTF-8"); // the charset is UTF-8
        } catch (UnsupportedEncodingException ex) {
            S = "";
        }
        return S;
    }

    public static Date GetDateAt(byte[] Buffer, int Pos)
    {
        int Year, Month, Day, Hour, Min, Sec;
        Calendar S7Date = Calendar.getInstance();

        Year = S7.BCDtoByte(Buffer[Pos]);
        if (Year<90)
            Year+=2000;
        else
            Year+=1900;

        Month=S7.BCDtoByte(Buffer[Pos+1])-1;
        Day  =S7.BCDtoByte(Buffer[Pos+2]);
        Hour =S7.BCDtoByte(Buffer[Pos+3]);
        Min  =S7.BCDtoByte(Buffer[Pos+4]);
        Sec  =S7.BCDtoByte(Buffer[Pos+5]);

        S7Date.set(Year, Month, Day, Hour, Min, Sec);                   
        
        return S7Date.getTime();
    }
        
    public static void SetBitAt(byte[] Buffer, int Pos, int Bit, boolean Value)
    {              
        byte[] Mask = {
            (byte)0x01,(byte)0x02,(byte)0x04,(byte)0x08,
            (byte)0x10,(byte)0x20,(byte)0x40,(byte)0x80
        };   
        if (Bit<0) Bit=0;
        if (Bit>7) Bit=7;
  
        if (Value)
            Buffer[Pos]= (byte) (Buffer[Pos] | Mask[Bit]);
        else
            Buffer[Pos]= (byte) (Buffer[Pos] & ~Mask[Bit]);
    }  

    public static void SetWordAt(byte[] Buffer, int Pos, int Value)
    {
        int Word = Value & 0x0FFFF;
        Buffer[Pos]   = (byte) (Word >> 8);
        Buffer[Pos+1] = (byte) (Word & 0x00FF);
    }   
    
    public static void SetShortAt(byte[] Buffer, int Pos, int Value)
    {
        Buffer[Pos]   = (byte) (Value >> 8);
        Buffer[Pos+1] = (byte) (Value & 0x00FF);
    }   
    public static void SetDWordAt(byte[] Buffer, int Pos, long Value)
    {
        long DWord = Value &0x0FFFFFFFF;
        Buffer[Pos+3] = (byte) (DWord &0xFF);
        Buffer[Pos+2] = (byte) ((DWord >> 8) &0xFF);
        Buffer[Pos+1] = (byte) ((DWord >> 16) &0xFF);
        Buffer[Pos]   = (byte) ((DWord >> 24) &0xFF);
    }
    
    public static void SetDIntAt(byte[] Buffer, int Pos, int Value)
    {       
        Buffer[Pos+3] = (byte) (Value &0xFF);
        Buffer[Pos+2] = (byte) ((Value >> 8) &0xFF);
        Buffer[Pos+1] = (byte) ((Value >> 16) &0xFF);
        Buffer[Pos]   = (byte) ((Value >> 24) &0xFF);
    }

    public static void SetFloatAt(byte[] Buffer, int Pos, float Value)
    {
        int DInt = Float.floatToIntBits(Value);
        SetDIntAt(Buffer, Pos, DInt);
    }
   
    public static void SetDateAt(byte[] Buffer, int Pos, Date DateTime)
    {        
        int Year, Month, Day, Hour, Min, Sec, Dow;
        Calendar S7Date = Calendar.getInstance();
        S7Date.setTime(DateTime);
        
        Year  = S7Date.get(Calendar.YEAR);
        Month = S7Date.get(Calendar.MONTH)+1;
        Day   = S7Date.get(Calendar.DAY_OF_MONTH);
        Hour  = S7Date.get(Calendar.HOUR_OF_DAY);
        Min   = S7Date.get(Calendar.MINUTE);
        Sec   = S7Date.get(Calendar.SECOND);
        Dow   = S7Date.get(Calendar.DAY_OF_WEEK);
        
        if (Year>1999)
            Year-=2000;
        
        Buffer[Pos]  =ByteToBCD(Year);
        Buffer[Pos+1]=ByteToBCD(Month);
        Buffer[Pos+2]=ByteToBCD(Day);
        Buffer[Pos+3]=ByteToBCD(Hour);
        Buffer[Pos+4]=ByteToBCD(Min);
        Buffer[Pos+5]=ByteToBCD(Sec);
        Buffer[Pos+6]=0;        
        Buffer[Pos+7]=ByteToBCD(Dow);        
    }

    public static int BCDtoByte(byte B)
    {
        return ((B >> 4) * 10) + (B & 0x0F);        
    }
 
    public static byte ByteToBCD(int Value)
    {
        return (byte) (((Value / 10) << 4) | (Value % 10));        
    }
    
}
