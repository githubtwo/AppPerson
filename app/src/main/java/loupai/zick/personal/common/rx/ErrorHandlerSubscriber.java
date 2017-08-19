package loupai.zick.personal.common.rx;

import android.content.Context;
import android.util.Log;


import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/7/6.
 */

public abstract class ErrorHandlerSubscriber<T> extends DefualtSubscriber<T> {

    private Context mContext;

    public ErrorHandlerSubscriber() {

    }

    @Override
    public void onError(Throwable e) {

            e.printStackTrace();
            Log.e("error",e.toString());
           // mRxErrorHandler.showErrorMessage(exception);

    }

    @Override
    public void onSubscribe(Disposable d) {
        //dialog.show();
    }

    @Override
    public void onComplete() {
       // dialog.dismiss();

    }


}
