package loupai.zick.personal.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import loupai.zick.personal.AppApplication;
import loupai.zick.personal.di.component.AppComponent;
import loupai.zick.personal.presenter.BasePresenter;

/**
 * Created by Administrator on 2017/7/3.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    private Unbinder mUnbinder;

    protected AppApplication mApplication;

    @Inject
    T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(setLayout());
        mUnbinder = ButterKnife.bind(this);
        //mApplication = AppApplication.get(getApplication());

        mApplication = AppApplication.get(getApplicationContext());
        setupActivityAppComponent(mApplication.getDaggerAppComponent());
        init(mApplication.getDaggerAppComponent());
    }

    public abstract int setLayout();

    public abstract void init(AppComponent appComponent);

    public abstract void setupActivityAppComponent(AppComponent appComponent);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
    }
}
