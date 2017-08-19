package loupai.zick.personal;

import android.app.Application;
import android.content.Context;

import loupai.zick.personal.di.component.AppComponent;
import loupai.zick.personal.di.component.DaggerAppComponent;
import loupai.zick.personal.di.module.AppModule;
import loupai.zick.personal.di.module.HttpModule;


/**
 * Created by Administrator on 2017/7/3.
 */

public class AppApplication extends Application {

    AppComponent mAppComponent;

    public AppComponent getDaggerAppComponent(){

        return mAppComponent;
    }

    public static AppApplication get(Context context){
        return (AppApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .httpModule(new HttpModule()).build();
    }
}
