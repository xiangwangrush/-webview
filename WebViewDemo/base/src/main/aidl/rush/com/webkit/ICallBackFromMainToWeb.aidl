// ICallBackFromMainToWeb.aidl
package rush.com.webkit;

// Declare any non-default types here with import statements

interface ICallBackFromMainToWeb {

    void onResult(int responseCode,String action,String response);
}
