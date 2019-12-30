package rush.com.base.plugin;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import common.rush.com.annotation.RushBridge;
import rush.com.base.cmd.Command;
import rush.com.base.cmd.ResultCallback;

/**
 * 插件toast
 */
@RushBridge()
public class ToastPlugin implements Command {

    @Override
    public String name() {
        return "Toast";
    }

    @Override
    public void exec(final Context context, final String params, ResultCallback callback) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, params, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ToastPlugin() {
    }
}
