package rush.com.base.plugin;

import android.content.Context;

import common.rush.com.annotation.RushBridge;
import rush.com.base.cmd.Command;
import rush.com.base.cmd.ResultCallback;

/**
 * Created by USER on 2019/12/9.
 */
@RushBridge()
public class AAAPlugin implements Command{

    @Override
    public String name() {
        return "AAAPlugin";
    }

    @Override
    public void exec(Context context, String params, ResultCallback callback) {

    }
}
