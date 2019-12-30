package rush.com.base.connector;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.CountDownLatch;

import rush.com.base.service.MainProcessHandlerRemoteService;
import rush.com.webkit.IWebToMain;

/**
 * 连接管理器
 */

public class MainProcessConnector {
    private Context mContext;
    private IWebToMain mBinderPool;
    private static volatile MainProcessConnector instance;
    private CountDownLatch mConnectBinderPoolCountDownLatch;

    private MainProcessConnector(Context context){
        mContext = context.getApplicationContext();
        connectToMainProcessService();
    }

    public static MainProcessConnector getInstance(Context context){
        if(instance == null){
            synchronized (MainProcessConnector.class){
                if(instance == null){
                    instance = new MainProcessConnector(context);
                }
            }
        }
        return instance;
    }

    private synchronized void connectToMainProcessService(){
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext,MainProcessHandlerRemoteService.class);
        mContext.bindService(service,mBinderPoolConnection,Context.BIND_AUTO_CREATE);
        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public IBinder getIWebAidlInterface(){
        return mBinderPool.asBinder();
    }

    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IWebToMain.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient,0);
            mBinderPool = null;
            connectToMainProcessService();
        }
    };
}
