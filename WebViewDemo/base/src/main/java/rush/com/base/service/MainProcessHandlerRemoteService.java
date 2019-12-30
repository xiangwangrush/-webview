package rush.com.base.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by USER on 2019/12/7.
 */

public class MainProcessHandlerRemoteService extends Service{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MainProAidlInterface(this);
    }
}
