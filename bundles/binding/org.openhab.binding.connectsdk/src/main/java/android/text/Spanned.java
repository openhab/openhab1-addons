package android.text;
public interface Spanned
  extends java.lang.CharSequence
{
public abstract <T> T[] getSpans(int start, int end, java.lang.Class<T> type);
public abstract  int getSpanStart(java.lang.Object tag);
public abstract  int getSpanEnd(java.lang.Object tag);
public abstract  int getSpanFlags(java.lang.Object tag);
public abstract  int nextSpanTransition(int start, int limit, java.lang.Class type);
public static final int SPAN_POINT_MARK_MASK = 51;
public static final int SPAN_MARK_MARK = 17;
public static final int SPAN_MARK_POINT = 18;
public static final int SPAN_POINT_MARK = 33;
public static final int SPAN_POINT_POINT = 34;
public static final int SPAN_PARAGRAPH = 51;
public static final int SPAN_INCLUSIVE_EXCLUSIVE = 17;
public static final int SPAN_INCLUSIVE_INCLUSIVE = 18;
public static final int SPAN_EXCLUSIVE_EXCLUSIVE = 33;
public static final int SPAN_EXCLUSIVE_INCLUSIVE = 34;
public static final int SPAN_COMPOSING = 256;
public static final int SPAN_INTERMEDIATE = 512;
public static final int SPAN_USER_SHIFT = 24;
public static final int SPAN_USER = -16777216;
public static final int SPAN_PRIORITY_SHIFT = 16;
public static final int SPAN_PRIORITY = 16711680;
}
