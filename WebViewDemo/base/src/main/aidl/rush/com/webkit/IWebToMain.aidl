// IWebToMain.aidl
package rush.com.webkit;

import rush.com.webkit.ICallBackFromMainToWeb;
// Declare any non-default types here with import statements

interface IWebToMain {

    void handleWebAction(String action,String params,in ICallBackFromMainToWeb callback);
}
