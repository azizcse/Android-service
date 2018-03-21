// IMyAidlInterface.aidl
package eyesky.com.androidservice;

// Declare any non-default types here with import statements
import eyesky.com.androidservice.IActivity;
interface IMyAidlInterface {
 void setForeground(in boolean value);
 void registerActivityCallback(in IActivity callback);
}
