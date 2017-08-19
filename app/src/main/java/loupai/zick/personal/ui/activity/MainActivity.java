package loupai.zick.personal.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import loupai.zick.personal.R;
import loupai.zick.personal.common.rx.ErrorHandlerSubscriber;
import loupai.zick.personal.common.rx.RxHttpReponseCompat;
import loupai.zick.personal.di.component.AppComponent;
import loupai.zick.personal.ui.adapter.IndexMultipleAdapter;
import loupai.zick.personal.ui.entity.Basebean;
import loupai.zick.personal.ui.entity.User;

public class MainActivity extends BaseActivity {

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.collapsing_toolbar_layout)
    AppBarLayout mCollapsingToolbarLayout;
    @BindView(R.id.layout_user)
    LinearLayout mLayoutUser;
    @BindView(R.id.txt_login)
    TextView mTxtLogin;
    @BindView(R.id.txt_settle)
    TextView mTxtSettle;
    @BindView(R.id.img_head)
    CircleImageView mImgHead;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    private IndexMultipleAdapter mAdapter;
    private AppComponent mAppComponent;

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void init(AppComponent appComponent) {
        mAppComponent = appComponent;

        refreshView(appComponent);
        mLayoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        mImgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MineActivity.class));
            }
        });


        mAdapter = new IndexMultipleAdapter(MainActivity.this);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mApplication));
    }

    @Override
    public void setupActivityAppComponent(AppComponent appComponent) {


    }

    //刷新数据
    private void refreshView(final AppComponent appComponent) {

        initUser(appComponent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //initUser(appComponent);
                Log.e("refresh", "refreshing");
                initUser(appComponent);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        //设置样式刷新显示的位置
        mSwipeRefreshLayout.setProgressViewOffset(true, -20, 100);
        // mSwipeRefreshLayout.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2, R.color.swiperefresh_color3, R.color.swiperefresh_color4);

        mCollapsingToolbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset >= 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        });

        mTxtSettle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettleActivity.class));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("hell","hhello");
        initUser(mAppComponent);
    }

    private void initUser(AppComponent appComponent) {
        appComponent.provideApiService().getUserInfo()
                .compose(RxHttpReponseCompat.<Basebean<User>>compatOrigin())
                .subscribe(new ErrorHandlerSubscriber<Basebean<User>>() {
                    @Override
                    public void onNext(Basebean<User> userBasebean) {
                        if (userBasebean.getStatus() == 0) {
                            Log.e("user",userBasebean.getData().toString());
                            mTxtLogin.setText(userBasebean.getData().getUsername());
                            mLayoutUser.setEnabled(false);
                            if(userBasebean.getData().getImg() != null){
                                Glide
                                        .with(getApplicationContext())
                                        .load("http://47.93.243.239:8080/mmall/picture/image?img=" + userBasebean.getData().getImg())
                                        .into(mImgHead);
                            }
                        }else {
                            mTxtLogin.setText("登录/注册");
                            mLayoutUser.setEnabled(true);
                            mImgHead.setImageResource(R.drawable.icon_default1x);
                        }
                    }
                });

    }
}
