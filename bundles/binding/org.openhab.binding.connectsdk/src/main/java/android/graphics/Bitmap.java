package android.graphics;
public final class Bitmap
  implements android.os.Parcelable
{
public static enum Config
{
ALPHA_8(),
ARGB_4444(),
ARGB_8888(),
RGB_565();
}
public static enum CompressFormat
{
JPEG(),
PNG(),
WEBP();
}
Bitmap() { throw new RuntimeException("Stub!"); }
public  int getDensity() { throw new RuntimeException("Stub!"); }
public  void setDensity(int density) { throw new RuntimeException("Stub!"); }
public  void recycle() { throw new RuntimeException("Stub!"); }
public final  boolean isRecycled() { throw new RuntimeException("Stub!"); }
public  int getGenerationId() { throw new RuntimeException("Stub!"); }
public  void copyPixelsToBuffer(java.nio.Buffer dst) { throw new RuntimeException("Stub!"); }
public  void copyPixelsFromBuffer(java.nio.Buffer src) { throw new RuntimeException("Stub!"); }
public  android.graphics.Bitmap copy(android.graphics.Bitmap.Config config, boolean isMutable) { throw new RuntimeException("Stub!"); }
public static  android.graphics.Bitmap createScaledBitmap(android.graphics.Bitmap src, int dstWidth, int dstHeight, boolean filter) { throw new RuntimeException("Stub!"); }
public static  android.graphics.Bitmap createBitmap(android.graphics.Bitmap src) { throw new RuntimeException("Stub!"); }
public static  android.graphics.Bitmap createBitmap(android.graphics.Bitmap source, int x, int y, int width, int height) { throw new RuntimeException("Stub!"); }
//public static  android.graphics.Bitmap createBitmap(android.graphics.Bitmap source, int x, int y, int width, int height, android.graphics.Matrix m, boolean filter) { throw new RuntimeException("Stub!"); }
public static  android.graphics.Bitmap createBitmap(int width, int height, android.graphics.Bitmap.Config config) { throw new RuntimeException("Stub!"); }
public static  android.graphics.Bitmap createBitmap(int[] colors, int offset, int stride, int width, int height, android.graphics.Bitmap.Config config) { throw new RuntimeException("Stub!"); }
public static  android.graphics.Bitmap createBitmap(int[] colors, int width, int height, android.graphics.Bitmap.Config config) { throw new RuntimeException("Stub!"); }
public  byte[] getNinePatchChunk() { throw new RuntimeException("Stub!"); }
public  boolean compress(android.graphics.Bitmap.CompressFormat format, int quality, java.io.OutputStream stream) { throw new RuntimeException("Stub!"); }
public final  boolean isMutable() { throw new RuntimeException("Stub!"); }
public final  int getWidth() { throw new RuntimeException("Stub!"); }
public final  int getHeight() { throw new RuntimeException("Stub!"); }
//public  int getScaledWidth(android.graphics.Canvas canvas) { throw new RuntimeException("Stub!"); }
//public  int getScaledHeight(android.graphics.Canvas canvas) { throw new RuntimeException("Stub!"); }
//public  int getScaledWidth(android.util.DisplayMetrics metrics) { throw new RuntimeException("Stub!"); }
//public  int getScaledHeight(android.util.DisplayMetrics metrics) { throw new RuntimeException("Stub!"); }
public  int getScaledWidth(int targetDensity) { throw new RuntimeException("Stub!"); }
public  int getScaledHeight(int targetDensity) { throw new RuntimeException("Stub!"); }
public final  int getRowBytes() { throw new RuntimeException("Stub!"); }
public final  int getByteCount() { throw new RuntimeException("Stub!"); }
public final  android.graphics.Bitmap.Config getConfig() { throw new RuntimeException("Stub!"); }
public final  boolean hasAlpha() { throw new RuntimeException("Stub!"); }
public  void setHasAlpha(boolean hasAlpha) { throw new RuntimeException("Stub!"); }
public  void eraseColor(int c) { throw new RuntimeException("Stub!"); }
public  int getPixel(int x, int y) { throw new RuntimeException("Stub!"); }
public  void getPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) { throw new RuntimeException("Stub!"); }
public  void setPixel(int x, int y, int color) { throw new RuntimeException("Stub!"); }
public  void setPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) { throw new RuntimeException("Stub!"); }
public  int describeContents() { throw new RuntimeException("Stub!"); }
//public  void writeToParcel(android.os.Parcel p, int flags) { throw new RuntimeException("Stub!"); }
public  android.graphics.Bitmap extractAlpha() { throw new RuntimeException("Stub!"); }
//public  android.graphics.Bitmap extractAlpha(android.graphics.Paint paint, int[] offsetXY) { throw new RuntimeException("Stub!"); }
public  boolean sameAs(android.graphics.Bitmap other) { throw new RuntimeException("Stub!"); }
public  void prepareToDraw() { throw new RuntimeException("Stub!"); }
public static final int DENSITY_NONE = 0;
public static final android.os.Parcelable.Creator<android.graphics.Bitmap> CREATOR;
static { CREATOR = null; }
}
