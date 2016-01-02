package android.graphics.drawable;
public abstract class Drawable
{
public static interface Callback
{
public abstract  void invalidateDrawable(android.graphics.drawable.Drawable who);
public abstract  void scheduleDrawable(android.graphics.drawable.Drawable who, java.lang.Runnable what, long when);
public abstract  void unscheduleDrawable(android.graphics.drawable.Drawable who, java.lang.Runnable what);
}
public abstract static class ConstantState
{
public  ConstantState() { throw new RuntimeException("Stub!"); }
public abstract  android.graphics.drawable.Drawable newDrawable();
//public  android.graphics.drawable.Drawable newDrawable(android.content.res.Resources res) { throw new RuntimeException("Stub!"); }
public abstract  int getChangingConfigurations();
}
public  Drawable() { throw new RuntimeException("Stub!"); }
//public abstract  void draw(android.graphics.Canvas canvas);
public  void setBounds(int left, int top, int right, int bottom) { throw new RuntimeException("Stub!"); }
//public  void setBounds(android.graphics.Rect bounds) { throw new RuntimeException("Stub!"); }
//public final  void copyBounds(android.graphics.Rect bounds) { throw new RuntimeException("Stub!"); }
//public final  android.graphics.Rect copyBounds() { throw new RuntimeException("Stub!"); }
//public final  android.graphics.Rect getBounds() { throw new RuntimeException("Stub!"); }
public  void setChangingConfigurations(int configs) { throw new RuntimeException("Stub!"); }
public  int getChangingConfigurations() { throw new RuntimeException("Stub!"); }
public  void setDither(boolean dither) { throw new RuntimeException("Stub!"); }
public  void setFilterBitmap(boolean filter) { throw new RuntimeException("Stub!"); }
public final  void setCallback(android.graphics.drawable.Drawable.Callback cb) { throw new RuntimeException("Stub!"); }
public  android.graphics.drawable.Drawable.Callback getCallback() { throw new RuntimeException("Stub!"); }
public  void invalidateSelf() { throw new RuntimeException("Stub!"); }
public  void scheduleSelf(java.lang.Runnable what, long when) { throw new RuntimeException("Stub!"); }
public  void unscheduleSelf(java.lang.Runnable what) { throw new RuntimeException("Stub!"); }
public abstract  void setAlpha(int alpha);
//public abstract  void setColorFilter(android.graphics.ColorFilter cf);
//public  void setColorFilter(int color, android.graphics.PorterDuff.Mode mode) { throw new RuntimeException("Stub!"); }
public  void clearColorFilter() { throw new RuntimeException("Stub!"); }
public  boolean isStateful() { throw new RuntimeException("Stub!"); }
public  boolean setState(int[] stateSet) { throw new RuntimeException("Stub!"); }
public  int[] getState() { throw new RuntimeException("Stub!"); }
public  void jumpToCurrentState() { throw new RuntimeException("Stub!"); }
public  android.graphics.drawable.Drawable getCurrent() { throw new RuntimeException("Stub!"); }
public final  boolean setLevel(int level) { throw new RuntimeException("Stub!"); }
public final  int getLevel() { throw new RuntimeException("Stub!"); }
public  boolean setVisible(boolean visible, boolean restart) { throw new RuntimeException("Stub!"); }
public final  boolean isVisible() { throw new RuntimeException("Stub!"); }
public abstract  int getOpacity();
public static  int resolveOpacity(int op1, int op2) { throw new RuntimeException("Stub!"); }
//public  android.graphics.Region getTransparentRegion() { throw new RuntimeException("Stub!"); }
protected  boolean onStateChange(int[] state) { throw new RuntimeException("Stub!"); }
protected  boolean onLevelChange(int level) { throw new RuntimeException("Stub!"); }
//protected  void onBoundsChange(android.graphics.Rect bounds) { throw new RuntimeException("Stub!"); }
public  int getIntrinsicWidth() { throw new RuntimeException("Stub!"); }
public  int getIntrinsicHeight() { throw new RuntimeException("Stub!"); }
public  int getMinimumWidth() { throw new RuntimeException("Stub!"); }
public  int getMinimumHeight() { throw new RuntimeException("Stub!"); }
//public  boolean getPadding(android.graphics.Rect padding) { throw new RuntimeException("Stub!"); }
public  android.graphics.drawable.Drawable mutate() { throw new RuntimeException("Stub!"); }
public static  android.graphics.drawable.Drawable createFromStream(java.io.InputStream is, java.lang.String srcName) { throw new RuntimeException("Stub!"); }
//public static  android.graphics.drawable.Drawable createFromResourceStream(android.content.res.Resources res, android.util.TypedValue value, java.io.InputStream is, java.lang.String srcName) { throw new RuntimeException("Stub!"); }
//public static  android.graphics.drawable.Drawable createFromResourceStream(android.content.res.Resources res, android.util.TypedValue value, java.io.InputStream is, java.lang.String srcName, android.graphics.BitmapFactory.Options opts) { throw new RuntimeException("Stub!"); }
//public static  android.graphics.drawable.Drawable createFromXml(android.content.res.Resources r, org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException { throw new RuntimeException("Stub!"); }
//public static  android.graphics.drawable.Drawable createFromXmlInner(android.content.res.Resources r, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException { throw new RuntimeException("Stub!"); }
//public static  android.graphics.drawable.Drawable createFromPath(java.lang.String pathName) { throw new RuntimeException("Stub!"); }
//public  void inflate(android.content.res.Resources r, org.xmlpull.v1.XmlPullParser parser, android.util.AttributeSet attrs) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException { throw new RuntimeException("Stub!"); }
public  android.graphics.drawable.Drawable.ConstantState getConstantState() { throw new RuntimeException("Stub!"); }
}
