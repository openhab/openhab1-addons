package android.content;
public abstract class BroadcastReceiver
{
public static class PendingResult
{
PendingResult() { throw new RuntimeException("Stub!"); }
public final  void setResultCode(int code) { throw new RuntimeException("Stub!"); }
public final  int getResultCode() { throw new RuntimeException("Stub!"); }
public final  void setResultData(java.lang.String data) { throw new RuntimeException("Stub!"); }
public final  java.lang.String getResultData() { throw new RuntimeException("Stub!"); }
public final  void setResultExtras(android.os.Bundle extras) { throw new RuntimeException("Stub!"); }
public final  android.os.Bundle getResultExtras(boolean makeMap) { throw new RuntimeException("Stub!"); }
public final  void setResult(int code, java.lang.String data, android.os.Bundle extras) { throw new RuntimeException("Stub!"); }
public final  boolean getAbortBroadcast() { throw new RuntimeException("Stub!"); }
public final  void abortBroadcast() { throw new RuntimeException("Stub!"); }
public final  void clearAbortBroadcast() { throw new RuntimeException("Stub!"); }
public final  void finish() { throw new RuntimeException("Stub!"); }
}
public  BroadcastReceiver() { /*throw new RuntimeException("Stub!"); */}
public abstract  void onReceive(android.content.Context context, android.content.Intent intent);
public final  android.content.BroadcastReceiver.PendingResult goAsync() { throw new RuntimeException("Stub!"); }
public  android.os.IBinder peekService(android.content.Context myContext, android.content.Intent service) { throw new RuntimeException("Stub!"); }
public final  void setResultCode(int code) { throw new RuntimeException("Stub!"); }
public final  int getResultCode() { throw new RuntimeException("Stub!"); }
public final  void setResultData(java.lang.String data) { throw new RuntimeException("Stub!"); }
public final  java.lang.String getResultData() { throw new RuntimeException("Stub!"); }
public final  void setResultExtras(android.os.Bundle extras) { throw new RuntimeException("Stub!"); }
public final  android.os.Bundle getResultExtras(boolean makeMap) { throw new RuntimeException("Stub!"); }
public final  void setResult(int code, java.lang.String data, android.os.Bundle extras) { throw new RuntimeException("Stub!"); }
public final  boolean getAbortBroadcast() { throw new RuntimeException("Stub!"); }
public final  void abortBroadcast() { throw new RuntimeException("Stub!"); }
public final  void clearAbortBroadcast() { throw new RuntimeException("Stub!"); }
public final  boolean isOrderedBroadcast() { throw new RuntimeException("Stub!"); }
public final  boolean isInitialStickyBroadcast() { throw new RuntimeException("Stub!"); }
public final  void setOrderedHint(boolean isOrdered) { throw new RuntimeException("Stub!"); }
public final  void setDebugUnregister(boolean debug) { throw new RuntimeException("Stub!"); }
public final  boolean getDebugUnregister() { throw new RuntimeException("Stub!"); }
}
