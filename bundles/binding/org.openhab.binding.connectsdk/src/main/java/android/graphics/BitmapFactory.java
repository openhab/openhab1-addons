package android.graphics;
public class BitmapFactory
{
public static class Options
{
public  Options() { throw new RuntimeException("Stub!"); }
public  void requestCancelDecode() { throw new RuntimeException("Stub!"); }
public android.graphics.Bitmap inBitmap;
@java.lang.SuppressWarnings(value={"UnusedDeclaration"})
public boolean inMutable;
public boolean inJustDecodeBounds;
public int inSampleSize;
public android.graphics.Bitmap.Config inPreferredConfig;
public boolean inDither;
public int inDensity;
public int inTargetDensity;
public int inScreenDensity;
public boolean inScaled;
public boolean inPurgeable;
public boolean inInputShareable;
public boolean inPreferQualityOverSpeed;
public int outWidth;
public int outHeight;
public java.lang.String outMimeType;
public byte[] inTempStorage = null;
public boolean mCancel;
}
public  BitmapFactory() { throw new RuntimeException("Stub!"); }
public static  android.graphics.Bitmap decodeFile(java.lang.String pathName, android.graphics.BitmapFactory.Options opts) { throw new RuntimeException("Stub!"); }
public static  android.graphics.Bitmap decodeFile(java.lang.String pathName) { throw new RuntimeException("Stub!"); }
//public static  android.graphics.Bitmap decodeResourceStream(android.content.res.Resources res, android.util.TypedValue value, java.io.InputStream is, android.graphics.Rect pad, android.graphics.BitmapFactory.Options opts) { throw new RuntimeException("Stub!"); }
//public static  android.graphics.Bitmap decodeResource(android.content.res.Resources res, int id, android.graphics.BitmapFactory.Options opts) { throw new RuntimeException("Stub!"); }
//public static  android.graphics.Bitmap decodeResource(android.content.res.Resources res, int id) { throw new RuntimeException("Stub!"); }
public static  android.graphics.Bitmap decodeByteArray(byte[] data, int offset, int length, android.graphics.BitmapFactory.Options opts) { throw new RuntimeException("Stub!"); }
public static  android.graphics.Bitmap decodeByteArray(byte[] data, int offset, int length) { throw new RuntimeException("Stub!"); }
//public static  android.graphics.Bitmap decodeStream(java.io.InputStream is, android.graphics.Rect outPadding, android.graphics.BitmapFactory.Options opts) { throw new RuntimeException("Stub!"); }
public static  android.graphics.Bitmap decodeStream(java.io.InputStream is) { throw new RuntimeException("Stub!"); }
//public static  android.graphics.Bitmap decodeFileDescriptor(java.io.FileDescriptor fd, android.graphics.Rect outPadding, android.graphics.BitmapFactory.Options opts) { throw new RuntimeException("Stub!"); }
public static  android.graphics.Bitmap decodeFileDescriptor(java.io.FileDescriptor fd) { throw new RuntimeException("Stub!"); }
}
