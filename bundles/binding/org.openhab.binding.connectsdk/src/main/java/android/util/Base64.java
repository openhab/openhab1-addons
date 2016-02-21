package android.util;
public class Base64
{
Base64() { throw new RuntimeException("Stub!"); }
public static  byte[] decode(java.lang.String str, int flags) { throw new RuntimeException("Stub!"); }
public static  byte[] decode(byte[] input, int flags) { throw new RuntimeException("Stub!"); }
public static  byte[] decode(byte[] input, int offset, int len, int flags) { throw new RuntimeException("Stub!"); }
public static  java.lang.String encodeToString(byte[] input, int flags) { return java.util.Base64.getEncoder().encodeToString(input); }
public static  java.lang.String encodeToString(byte[] input, int offset, int len, int flags) { throw new RuntimeException("Stub!"); }
public static  byte[] encode(byte[] input, int flags) { throw new RuntimeException("Stub!"); }
public static  byte[] encode(byte[] input, int offset, int len, int flags) { throw new RuntimeException("Stub!"); }
public static final int DEFAULT = 0;
public static final int NO_PADDING = 1;
public static final int NO_WRAP = 2;
public static final int CRLF = 4;
public static final int URL_SAFE = 8;
public static final int NO_CLOSE = 16;
}
