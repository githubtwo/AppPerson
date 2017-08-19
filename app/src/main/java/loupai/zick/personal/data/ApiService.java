package loupai.zick.personal.data;

import java.util.Map;

import io.reactivex.Observable;
import loupai.zick.personal.ui.entity.Basebean;
import loupai.zick.personal.ui.entity.User;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2017/7/3.
 */

public interface ApiService {
//    public static final String BASE_URL = "http://172.16.0.204:8088/";
    public static final String BASE_URL = "http://47.93.243.239:8080/mmall/";

    @GET("user/login")
    Observable<Basebean<User>> login(@QueryMap Map<String,String> map);//登录

    @GET("user/logout")
    Observable<Basebean<String>> logout();//退出登录

    @GET("user/get_user_info")
    Observable<Basebean<User>> getUserInfo();//获取用户信息

    @GET("user/register")
    Observable<Basebean<String>> register(@QueryMap Map<String,String> map);//用户注册


    @POST("picture/updateImg")
    Observable<Basebean<String>> postUpdatePicture(@Query("fileName") String string);  //修改图片

    // /app/recharge
    @POST("order/pay")
    Observable<Basebean<String>> postRecharge(@Query("number") String number);  //



}
