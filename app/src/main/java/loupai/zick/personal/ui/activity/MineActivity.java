package loupai.zick.personal.ui.activity;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import loupai.zick.personal.AppApplication;
import loupai.zick.personal.R;
import loupai.zick.personal.common.rx.ErrorHandlerSubscriber;
import loupai.zick.personal.common.rx.RxHttpReponseCompat;
import loupai.zick.personal.di.component.AppComponent;
import loupai.zick.personal.ui.entity.Basebean;
import loupai.zick.personal.ui.entity.PictureResult;
import loupai.zick.personal.ui.entity.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/27.
 */

public class MineActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.layout_head)
    LinearLayout mLayoutHead;
    @BindView(R.id.layout_mine_name)
    LinearLayout mLayoutMineName;
    @BindView(R.id.layout_mine_package)
    LinearLayout mLayoutMinePackage;
    @BindView(R.id.layout_mine_check)
    LinearLayout mLayoutMineCheck;
    @BindView(R.id.layout_finish)
    LinearLayout mLayoutFinish;
    private static String fileName;
    @BindView(R.id.txt_name)
    TextView mTxtName;
    private PictureResult mPersonImg;
    AppApplication mApplication;
    @BindView(R.id.img_head)
    CircleImageView mImgHead;
    private File mFile;
    private AppComponent mAppComponent;


    @Override
    public int setLayout() {
        return R.layout.activity_mine;
    }

    @Override
    public void init(AppComponent appComponent) {

        mAppComponent = appComponent;

        mLayoutHead.setOnClickListener(this);
        mLayoutMineName.setOnClickListener(this);
        mLayoutMinePackage.setOnClickListener(this);
        mLayoutMineCheck.setOnClickListener(this);
        mLayoutFinish.setOnClickListener(this);

        initUser(appComponent);

    }

    public void initUser(AppComponent appComponent){
        appComponent.provideApiService().getUserInfo()
                .compose(RxHttpReponseCompat.<Basebean<User>>compatOrigin())
                .subscribe(new ErrorHandlerSubscriber<Basebean<User>>() {
                    @Override
                    public void onNext(Basebean<User> userBasebean) {
                        if (userBasebean.getStatus() == 0) {
                            mTxtName.setText(userBasebean.getData().getUsername());
                            if(userBasebean.getData().getImg() != null){
                                Glide
                                        .with(getApplicationContext())
                                        .load("http://47.93.243.239:8080/mmall/picture/image?img=" + userBasebean.getData().getImg())
                                        .into(mImgHead);
                            }
                        }
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        initUser(mAppComponent);
    }

    @Override
    public void setupActivityAppComponent(AppComponent appComponent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_finish:
                finish();
                break;

            case R.id.layout_head:
                selectPic(v);
                break;

        }
    }

    private final String iconPath = Environment.getExternalStorageDirectory() + "/Image";//图片的存储目录

    /**
     * 打开系统相册，并选择图片
     */
    public void selectPic(View view) {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    /**
     * 选择拍照的图片
     */
    public void takePhoto(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 1);
    }

    /**
     * 给拍的照片命名
     */
    public String createPhotoName() {
        //以系统的当前时间给图片命名
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        fileName = format.format(date) + ".jpg";
        return fileName;
    }

    /**
     * 把拍的照片保存到SD卡
     */
    public void saveToSDCard(Bitmap bitmap) {
        //先要判断SD卡是否存在并且挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(iconPath);
            mFile = file;
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(createPhotoName());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);//把图片数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Toast.makeText(this, "SD卡不存在", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取选择的图片
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            return;//当data为空的时候，不做任何处理
        }
        Bitmap bitmap = null;
        if (requestCode == 0) {
            //获取从相册界面返回的缩略图
            bitmap = data.getParcelableExtra("data");
            if (bitmap == null) {//如果返回的图片不够大，就不会执行缩略图的代码，因此需要判断是否为null,如果是小图，直接显示原图即可
                try {
                    //通过URI得到输入流
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    //通过输入流得到bitmap对象
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    final Bitmap finalBitmap = bitmap;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                saveBitmapFile(finalBitmap);
                            } catch (NetworkErrorException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 1) {
            bitmap = (Bitmap) data.getExtras().get("data");

            saveToSDCard(bitmap);
        }
        //将选择的图片设置到控件上
        //mImgHead.setImageBitmap(bitmap);
        //mImgHead.setImageURI(Uri.parse(Contants.API.base_img + "image/upload_pumphouse/meian_21.jpg"));
    }

    private final OkHttpClient client = new OkHttpClient();

    //Bitmap对象保存位图片文件
    public String saveBitmapFile(Bitmap bitmap) throws NetworkErrorException {

        File filepath = compressImage(bitmap);
        File file = new File(filepath.toString());
        Log.e("file", file.toString());
        final String url = "http://47.93.243.239:8080/mmall/picture/uploadPicture";
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("imagetype", "image/png")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response;

        try {
            response = client.newCall(request).execute();
            String jsonString = response.body().string();
            Log.e("personInfo", " upload jsonString =" + jsonString);
            mPersonImg = new Gson().fromJson(jsonString, PictureResult.class);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide
                            .with(getApplicationContext())
                            .load("http://47.93.243.239:8080/mmall/picture/image?img=" + mPersonImg.getData())
                            .into(mImgHead);
                    Log.e("img",mPersonImg.getData());
                    mAppComponent.provideApiService().postUpdatePicture(mPersonImg.getData())
                            .compose(RxHttpReponseCompat.<Basebean<String>>compatOrigin())
                            .subscribe(new ErrorHandlerSubscriber<Basebean<String>>() {
                                @Override
                                public void onNext(Basebean<String> stringBasebean) {
                                    Log.e("update",stringBasebean.getData().toString());
                                }
                            });
                }
            });
            if (!response.isSuccessful()) {
                Toast.makeText(mApplication, "上传失败", Toast.LENGTH_SHORT).show();
                throw new NetworkErrorException("upload error code " + response);
            }

        } catch (IOException e) {
            Log.e("person", "upload IOException ", e);
        }
        return null;

    }

    /**
     * 压缩图片（质量压缩）
     *
     * @param bitmap
     */
    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File file = new File(Environment.getExternalStorageDirectory(), filename + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                Log.e("e", e.getMessage());
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            Log.e("e", e.getMessage());
            e.printStackTrace();
        }
        recycleBitmap(bitmap);
        return file;
    }

    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
