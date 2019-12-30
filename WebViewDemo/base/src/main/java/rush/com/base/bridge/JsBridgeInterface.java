package rush.com.base.bridge;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;

/**
 * Created by USER on 2019/12/2.
 */

public class JsBridgeInterface {
    private Context mContext;
    private Handler mHandler = new Handler();
    private JavaScriptCommand mJavaScriptCommand;

    public JsBridgeInterface() {
    }

    public JsBridgeInterface(Context context) {
        this.mContext = context;
    }

    public void setJavaScriptCommand(JavaScriptCommand jsCommand){
        mJavaScriptCommand = jsCommand;
    }

    @JavascriptInterface
    public void post(final String cmd, final String params) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mJavaScriptCommand != null) {
                    try {
                        mJavaScriptCommand.exec(mContext, cmd, params);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }


    public interface JavaScriptCommand {
        void exec(Context context, String cmd, String params);
    }
}
