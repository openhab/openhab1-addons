package android.content.pm;
public class PackageItemInfo
{
public static class DisplayNameComparator
  implements java.util.Comparator<android.content.pm.PackageItemInfo>
{
public  DisplayNameComparator(android.content.pm.PackageManager pm) { throw new RuntimeException("Stub!"); }
public final  int compare(android.content.pm.PackageItemInfo aa, android.content.pm.PackageItemInfo ab) { throw new RuntimeException("Stub!"); }
}
public  PackageItemInfo() { /*throw new RuntimeException("Stub!");*/ }
public  PackageItemInfo(android.content.pm.PackageItemInfo orig) { throw new RuntimeException("Stub!"); }
protected  PackageItemInfo(android.os.Parcel source) { throw new RuntimeException("Stub!"); }
public  java.lang.CharSequence loadLabel(android.content.pm.PackageManager pm) { throw new RuntimeException("Stub!"); }
public  android.graphics.drawable.Drawable loadIcon(android.content.pm.PackageManager pm) { throw new RuntimeException("Stub!"); }
public  android.graphics.drawable.Drawable loadLogo(android.content.pm.PackageManager pm) { throw new RuntimeException("Stub!"); }
public  android.content.res.XmlResourceParser loadXmlMetaData(android.content.pm.PackageManager pm, java.lang.String name) { throw new RuntimeException("Stub!"); }
protected  void dumpFront(android.util.Printer pw, java.lang.String prefix) { throw new RuntimeException("Stub!"); }
protected  void dumpBack(android.util.Printer pw, java.lang.String prefix) { throw new RuntimeException("Stub!"); }
public  void writeToParcel(android.os.Parcel dest, int parcelableFlags) { throw new RuntimeException("Stub!"); }
public java.lang.String name;
public java.lang.String packageName;
public int labelRes;
public java.lang.CharSequence nonLocalizedLabel;
public int icon;
public int logo;
public android.os.Bundle metaData;
}
