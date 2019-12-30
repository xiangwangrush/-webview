package rush.com.base.plugintest;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import common.rush.com.annotation.RushBridge;
import rush.com.base.cmd.Command;
import rush.com.base.cmd.ResultCallback;

/**
 * Created by USER on 2019/12/14.
 */
@RushBridge()
public class CallBackTestPlugin implements Command{
    @Override
    public String name() {
        return "callBackTest";
    }

    @Override
    public void exec(Context context, String params, ResultCallback callback) {
        if(params != null && !TextUtils.isEmpty(params)){
            try {
                JSONObject paramObj = new JSONObject(params);
                String successCallBack = paramObj.optString("successCallBack");
                String errorCallBack = paramObj.optString("failCallBack");
                callback.onResult(1,successCallBack,"native to html 回调数据");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
