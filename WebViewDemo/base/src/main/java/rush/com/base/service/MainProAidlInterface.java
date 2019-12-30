package rush.com.base.service;

import android.content.Context;
import android.os.RemoteException;

import rush.com.base.cmd.CommandManager;
import rush.com.base.cmd.ResultCallback;
import rush.com.webkit.ICallBackFromMainToWeb;
import rush.com.webkit.IWebToMain;

/**
 * Created by USER on 2019/12/8.
 */

public class MainProAidlInterface extends IWebToMain.Stub{
    private Context mContext;

    public MainProAidlInterface(Context context) {
        this.mContext = context;
    }

    @Override
    public void handleWebAction(String action, String params,final ICallBackFromMainToWeb callback) throws RemoteException {
        //main process 分发收到的cmd
        CommandManager.getInstance().executeCommand(mContext, action, params, new ResultCallback() {
            @Override
            public void onResult(int responseCode,String action, String result) {
                if(callback != null){
                    try {
                        callback.onResult(responseCode,action,result);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
