package rush.com.base.cmd;

import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.webkit.WebView;

import rush.com.base.connector.MainProcessConnector;
import rush.com.webkit.ICallBackFromMainToWeb;
import rush.com.webkit.IWebToMain;

/**
 * 命令分发器
 */
public class CommandDispatcher {
    private static CommandDispatcher instance;
    private Context mContext;
    protected IWebToMain webAidlInterface;

    public static CommandDispatcher getInstance() {
        if (instance == null) {
            synchronized (CommandDispatcher.class) {
                if (instance == null) {
                    instance = new CommandDispatcher();
                }
            }
        }
        return instance;
    }

    public void initAIDLConnect(final Context context) {
        if (webAidlInterface != null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                webAidlInterface = IWebToMain.Stub.asInterface(MainProcessConnector.getInstance(context).getIWebAidlInterface());
            }
        }).start();
    }

    public void exec(String cmd, String params, final WebView webView) {
        if (webAidlInterface != null) {
            try {
                webAidlInterface.handleWebAction(cmd, params, new ICallBackFromMainToWeb.Stub() {
                    @Override
                    public void onResult(int responseCode, String action, String response) throws RemoteException {
                        //收到执行的返回结果
                        handleCallBack(action, response, webView);
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler handler = new Handler(Looper.getMainLooper());
    private void handleCallBack(String callBackFun, String result,final  WebView webView) {
        final String js = String.format("javascript:%s('%s')", callBackFun, result);
        handler.post(new Runnable() {
            @Override
            public void run() {
                //webview 回调给h5
                webView.loadUrl(js);
            }
        });
    }

}
