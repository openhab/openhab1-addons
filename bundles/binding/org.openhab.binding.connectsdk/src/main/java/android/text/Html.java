package android.text;
public class Html
{
public static interface ImageGetter
{
//public abstract  android.graphics.drawable.Drawable getDrawable(java.lang.String source);
}
public static interface TagHandler
{
//public abstract  void handleTag(boolean opening, java.lang.String tag, android.text.Editable output, org.xml.sax.XMLReader xmlReader);
}
Html() { throw new RuntimeException("Stub!"); }
public static  android.text.Spanned fromHtml(java.lang.String source) { throw new RuntimeException("Stub!"); }
public static  android.text.Spanned fromHtml(java.lang.String source, android.text.Html.ImageGetter imageGetter, android.text.Html.TagHandler tagHandler) { throw new RuntimeException("Stub!"); }
public static  java.lang.String toHtml(android.text.Spanned text) { throw new RuntimeException("Stub!"); }
public static  java.lang.String escapeHtml(java.lang.CharSequence text) { throw new RuntimeException("Stub!"); }
}
