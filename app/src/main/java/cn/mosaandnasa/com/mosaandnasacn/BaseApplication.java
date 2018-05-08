package cn.mosaandnasa.com.mosaandnasacn;

import android.app.Application;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

/**
 * Created by augus on 2018/2/14.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //
        instanceHockey();
    }

    private void instanceHockey() {
        CrashManager.register(this, "372bfb60e6764707a44648a70d25fd6a", new CrashManagerListener() {
            @Override
            public boolean shouldAutoUploadCrashes() {
                return true;
            }
        });
    }
}
