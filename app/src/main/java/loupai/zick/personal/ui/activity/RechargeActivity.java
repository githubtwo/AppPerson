package loupai.zick.personal.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import loupai.zick.alipay.AuthResult;
import loupai.zick.alipay.PayResult;
import loupai.zick.personal.R;
import loupai.zick.personal.common.rx.ErrorHandlerSubscriber;
import loupai.zick.personal.common.rx.RxHttpReponseCompat;
import loupai.zick.personal.di.component.AppComponent;
import loupai.zick.personal.ui.entity.Alipaybean;
import loupai.zick.personal.ui.entity.Basebean;

/**
 * Created by Zick on 2017/6/26.
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.img_card)
    ImageView mImgCard;
    @BindView(R.id.btn_recharge)
    Button mBtnRecharge;
    @BindView(R.id.layout_finish)
    LinearLayout mLayoutFinish;
    @BindView(R.id.btn_10)
    CheckBox mBtn10;
    @BindView(R.id.btn_20)
    CheckBox mBtn20;
    @BindView(R.id.btn_30)
    CheckBox mBtn30;
    @BindView(R.id.btn_50)
    CheckBox mBtn50;
    @BindView(R.id.btn_100)
    CheckBox mBtn100;
    @BindView(R.id.btn_200)
    CheckBox mBtn200;
    @BindView(R.id.edit_input)
    EditText mEditInput;
    @BindView(R.id.edit_card)
    EditText mEditCard;
    @BindView(R.id.recycler_card)
    RecyclerView mRecyclerCard;
    @BindView(R.id.layout_recycler)
    LinearLayout mLayoutRecycler;
    @BindView(R.id.layout_remark)
    LinearLayout mLayoutRemark;
    @BindView(R.id.txt_clear)
    TextView mTxtClear;
    @BindView(R.id.remark)
    TextView mRemark;
    public List<String>  mList = new ArrayList<>();

    private int Request_Code = 110;
    private int Request_Code_Recharge = 111;
    public static Alipaybean mAlipaybean;
    public AppComponent mAppComponent;
    private Map<String, String> mMap = new HashMap<>();
    public static String card_no;

    private List<CheckBox> mBoxList = new ArrayList<>();


    @Override
    public int setLayout() {
        return R.layout.activity_recharge;
    }

    @Override
    public void init(AppComponent appComponent) {
        mAppComponent = appComponent;


        mImgCard.setOnClickListener(this);
        mBtnRecharge.setOnClickListener(this);
        mLayoutFinish.setOnClickListener(this);
        mBtn10.setOnClickListener(this);
        mBtn20.setOnClickListener(this);
        mBtn30.setOnClickListener(this);
        mBtn50.setOnClickListener(this);
        mBtn100.setOnClickListener(this);
        mBtn200.setOnClickListener(this);
        mEditCard.setOnClickListener(this);
        mRecyclerCard.setOnClickListener(this);
        mLayoutRemark.setOnClickListener(this);
        mTxtClear.setOnClickListener(this);

        mBoxList.add(mBtn10);
        mBoxList.add(mBtn20);
        mBoxList.add(mBtn30);
        mBoxList.add(mBtn50);
        mBoxList.add(mBtn100);
        mBoxList.add(mBtn200);
        mEditCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//获得焦点

                } else {//失去焦点
                    mLayoutRemark.setVisibility(View.VISIBLE);
                    mLayoutRecycler.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void setupActivityAppComponent(AppComponent appComponent) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_card:


                break;
            case R.id.btn_recharge:

                if(mEditInput.getText().toString() != null){
                    mAppComponent.provideApiService().postRecharge(mEditInput.getText().toString())
                            .compose(RxHttpReponseCompat.<Basebean<String>>compatOrigin())
                            .subscribe(new ErrorHandlerSubscriber<Basebean<String>>() {
                                @Override
                                public void onNext(Basebean<String> stringBasebean) {

                                    if(stringBasebean.getData()!=null){
                                        getApply(stringBasebean.getData());
                                    }
                                }
                            });
                }

                break;

            case R.id.edit_card:


                break;

            case R.id.layout_finish:
                finish();
                break;

            case R.id.btn_10:
                mEditInput.setText("10");
                CheckBoxOnlyClick(mBtn10);
                break;

            case R.id.btn_20:
                mEditInput.setText("20");
                CheckBoxOnlyClick(mBtn20);
                break;

            case R.id.btn_30:
                mEditInput.setText("30");
                CheckBoxOnlyClick(mBtn30);
                break;

            case R.id.btn_50:
                mEditInput.setText("50");
                CheckBoxOnlyClick(mBtn50);
                break;

            case R.id.btn_100:
                mEditInput.setText("100");
                CheckBoxOnlyClick(mBtn100);
                break;

            case R.id.btn_200:
                mEditInput.setText("200");
                CheckBoxOnlyClick(mBtn200);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request_Code) {
            if (resultCode == 120) {
                finish();
            }
        }
        if (requestCode == Request_Code_Recharge) {
            if (resultCode == 121) {

                Bundle b = data.getExtras(); //data为B中回传的Intent
                String card = b.getString("card");//str即为回传的值

                mEditCard.setText(card);
                mRemark.setText(b.getString("remark"));
            }
        }
    }

    public void CheckBoxOnlyClick(CheckBox cb){
        for(CheckBox checkBox:mBoxList){
            checkBox.setChecked(false);
        }
        cb.setChecked(true);
    }
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        mList.add(mEditCard.getText().toString());
                 /*       LocalData.putPhone(getApplicationContext(),"Phone_data",mList);
                        Intent intent = new Intent(RechargeActivity.this, RechargeInfoActivity.class);
                        startActivityForResult(intent, Request_Code);*/


                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                      /*  Intent intent = new Intent(RechargeActivity.this, RechargeInfoActivity.class);
                        startActivityForResult(intent, Request_Code);*/
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(RechargeActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(RechargeActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    private void getApply(final String orderInfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Log.i("msp", result.get("result").toString());
                mAlipaybean = new Gson().fromJson(result.get("result").toString(), Alipaybean.class);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
