package in.ahadi.luci.ahadi;

/**
 * Created by Chiranjit on 7/11/2016.
 */
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


public class MyApplication extends Application {

    private static MyApplication mInstance;




    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }



}


