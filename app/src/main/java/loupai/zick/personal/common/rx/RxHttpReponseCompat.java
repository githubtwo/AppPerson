package loupai.zick.personal.common.rx;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import loupai.zick.personal.ui.entity.Basebean;


public class RxHttpReponseCompat {

    public static <T> ObservableTransformer<Basebean<T>,T> compatResult(){

        return new ObservableTransformer<Basebean<T>, T>() {
            @Override
            public ObservableSource<T> apply(final Observable<Basebean<T>> baseBeanObservable) {

                return baseBeanObservable.flatMap(new Function<Basebean<T>, ObservableSource<T>>() {


                    @Override
                    public ObservableSource<T> apply(@NonNull final Basebean<T> tBasebean) throws Exception {

                        //if(tBaseBean.success()){

                            return Observable.create(new ObservableOnSubscribe<T>() {
                                @Override
                                public void subscribe(ObservableEmitter<T> subscriber) throws Exception {

                                    try {
                                        subscriber.onNext(tBasebean.getData());
                                        subscriber.onComplete();
                                    }
                                    catch (Exception e){
                                        subscriber.onError(e);
                                    }
                                }
                            });
                        }
                        /*else {
                            return  Observable.error(new ApiException(tBaseBean.getStatus(),tBaseBean.getMessage()));
                        }
                    }*/
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
            }
        };
    }


    public static <T> ObservableTransformer<T,T> compatOrigin(){

        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(final Observable<T> baseBeanObservable) {

                return baseBeanObservable.flatMap(new Function<T, ObservableSource<T>>() {


                    @Override
                    public ObservableSource<T> apply(@NonNull final T tBaseBean) throws Exception {

                        //if(tBaseBean.success()){

                        return Observable.create(new ObservableOnSubscribe<T>() {
                            @Override
                            public void subscribe(ObservableEmitter<T> subscriber) throws Exception {

                                try {
                                    subscriber.onNext(tBaseBean);
                                    subscriber.onComplete();
                                }
                                catch (Exception e){
                                    subscriber.onError(e);
                                }
                            }
                        });
                    }
                        /*else {
                            return  Observable.error(new ApiException(tBaseBean.getStatus(),tBaseBean.getMessage()));
                        }
                    }*/
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
            }
        };
    }

}
