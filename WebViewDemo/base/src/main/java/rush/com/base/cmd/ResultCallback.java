package rush.com.base.cmd;

/**
 * Created by USER on 2019/12/7.
 */

public interface ResultCallback {
    void onResult(int responseCode,String action,String result);
}
