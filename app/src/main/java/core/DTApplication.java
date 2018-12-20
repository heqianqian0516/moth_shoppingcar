package core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;



/**
 * @author dingtao
 * @date 2018/12/6 18:49
 * qq:1940870847
 */
public class DTApplication extends Application {

    private static DTApplication instance;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mSharedPreferences = getSharedPreferences("application",
                Context.MODE_PRIVATE);
       /* JPushInterface.setDebugMode(true);
        JPushInterface.init(this);  */   		// 初始化 JPush
    }

    public static DTApplication getInstance() {
        return instance;
    }

    public SharedPreferences getShare() {
        return mSharedPreferences;
    }

}
