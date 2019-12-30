package rush.com.base.cmd;

import android.content.Context;

/**
 * Created by USER on 2019/12/7.
 */

public interface Command {
    String name();
    void exec(Context context,String params,ResultCallback callback);
}
