package android.os;
public interface Parcelable
{
public static interface Creator<T>
{
//public abstract  T createFromParcel(android.os.Parcel source);
public abstract  T[] newArray(int size);
}
public static interface ClassLoaderCreator<T>
  extends android.os.Parcelable.Creator<T>
{
//public abstract  T createFromParcel(android.os.Parcel source, java.lang.ClassLoader loader);
}
public abstract  int describeContents();
//public abstract  void writeToParcel(android.os.Parcel dest, int flags);
public static final int PARCELABLE_WRITE_RETURN_VALUE = 1;
public static final int CONTENTS_FILE_DESCRIPTOR = 1;
}
