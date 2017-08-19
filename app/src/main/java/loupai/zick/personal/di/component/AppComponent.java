package loupai.zick.personal.di.component;

import android.app.Activity;
import android.app.Application;


import javax.inject.Singleton;

import dagger.Component;
import loupai.zick.personal.di.module.AppModule;
import loupai.zick.personal.di.module.HttpModule;
import loupai.zick.personal.data.ApiService;

/**
 * Created by Administrator on 2017/7/3.
 */

@Singleton
@Component(modules = {AppModule.class, HttpModule.class})
public interface AppComponent {
    //在AppComponent中没有注入，只通过provideApiService往HttpModule.class中找实例
    public ApiService provideApiService();

    public void inject(Activity activity);
    public Application getApplication();

}
