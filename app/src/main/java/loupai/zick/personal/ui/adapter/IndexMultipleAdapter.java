package loupai.zick.personal.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import loupai.zick.personal.R;
import loupai.zick.personal.ui.activity.RechargeActivity;

/**
 * Created by Zick on 2017/6/26.
 */

public class IndexMultipleAdapter extends RecyclerView.Adapter {

    private static final int TYPE_BLOCK = 0;
    private static final int TYPE_CONSUME = 1;
    private static final int TYPE_MONTH = 3;
    private static final int TYPE_RECHARGE = 2;

    private LayoutInflater mInflater;
    private Context mContext;
    private int login = 1;//未登录

    public IndexMultipleAdapter(Context context) {

        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BLOCK;
        } else if (position == 1) {
            return TYPE_CONSUME;
        } else if (position == 3) {
            return TYPE_MONTH;
        } else if (position == 2) {
            return TYPE_RECHARGE;
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BLOCK) {
            return new BlockViewHolder(mInflater.inflate(R.layout.template_type_block, parent, false));
        } else if (viewType == TYPE_CONSUME) {
            return new ConsumeViewHolder(mInflater.inflate(R.layout.template_type_consume, parent, false));
        } else if (viewType == TYPE_MONTH) {
            return new CheckViewHolder(mInflater.inflate(R.layout.template_type_check, parent, false));
        } else if (viewType == TYPE_RECHARGE) {
            return new RechargeHolder(mInflater.inflate(R.layout.template_type_recharge, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (position) {
            case 0:
                BlockViewHolder blockViewHolder = (BlockViewHolder) holder;
                blockViewHolder.mLayoutDirectwater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // mContext.startActivity(new Intent(mContext, DirectDrinkActivity.class));
                    }
                });
                blockViewHolder.mLayoutRecharge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    Intent intent = new Intent(mContext, RechargeActivity.class);
                    mContext.startActivity(intent);

                    }
                });
                blockViewHolder.mLayoutPackage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                blockViewHolder.mLayoutCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                if(login == 9 || login ==1){
                    blockViewHolder.mLayoutNoMore.setVisibility(View.VISIBLE);
                }else{
                    blockViewHolder.mLayoutNoMore.setVisibility(View.GONE);
                }

                break;

            case 1:
                ConsumeViewHolder consumeViewHolder = (ConsumeViewHolder) holder;
                consumeViewHolder.mLayoutComsumeInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });
                consumeViewHolder.mTxtMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


                break;

            case 3:
                CheckViewHolder checkViewHolder = (CheckViewHolder) holder;

                break;

            case 2:
                RechargeHolder rechargeHolder = (RechargeHolder) holder;

                break;
        }

    }

    @Override
    public int getItemCount() {
        if (login == 9 || login == 5) {
            return 1;
        } else {
            return 4;
        }
    }

    class BlockViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_directwater)
        LinearLayout mLayoutDirectwater;
        @BindView(R.id.layout_recharge)
        LinearLayout mLayoutRecharge;
        @BindView(R.id.layout_package)
        LinearLayout mLayoutPackage;
        @BindView(R.id.layout_check)
        LinearLayout mLayoutCheck;
        @BindView(R.id.layout_no_more)
        LinearLayout mLayoutNoMore;

        public BlockViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ConsumeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_comsume_info)
        LinearLayout mLayoutComsumeInfo;
        @BindView(R.id.txt_more)
        TextView mTxtMore;

        @BindView(R.id.xf_time)
        TextView mXfTime;
        @BindView(R.id.xf_card)
        TextView mXfCard;
        @BindView(R.id.xf_money)
        TextView mXfMoney;
        @BindView(R.id.xf_address)
        TextView mXfAddress;

        public ConsumeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    class CheckViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_month_check)
        LinearLayout mLayoutMonthCheck;

        @BindView(R.id.month_cz_money)
        TextView mMonthCzMoney;
        @BindView(R.id.month_xf_money)
        TextView mMonthXfMoney;
        @BindView(R.id.month_cz)
        TextView mMonthCz;
        @BindView(R.id.month_xf)
        TextView mMonthXf;

        public CheckViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    class RechargeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_comsume_info)
        LinearLayout mLayoutComsumeInfo;
        @BindView(R.id.txt_more)
        TextView mTxtMore;

        @BindView(R.id.cz_time)
        TextView mCzTime;
        @BindView(R.id.cz_card)
        TextView mCzCard;
        @BindView(R.id.cz_money)
        TextView mCzMoney;
        @BindView(R.id.cz_address)
        TextView mCzAddress;

        public RechargeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /*
   * 将时间戳转换为时间
   */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
}
