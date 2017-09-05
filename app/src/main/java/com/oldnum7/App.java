package com.oldnum7;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.oldnum7.di.component.AppComponent;
import com.oldnum7.di.component.DaggerAppComponent;
import com.oldnum7.di.module.ApplicationModule;
import com.oldnum7.http.HttpFactory;
import com.oldnum7.http.model.HttpHeaders;
import com.oldnum7.http.model.HttpParams;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * <pre>
 *       author : denglin
 *       time   : 2017/06/01/10:17
 *       desc   :
 *       version: 1.0
 * </pre>
 */
public class App extends Application {
    private static Context mContext;

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this;
        Utils.init(this);

        this.mAppComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        //----------------------------------------------------------------------------------------//

        initHttpManager();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public static Context getmContext() {
        return mContext;
    }

    private void initHttpManager() {

        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");
        //----------------------------------------------------------------------------------------//

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
//        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
//        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));

        //超时时间设置，默认60秒
        builder.readTimeout(HttpFactory.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(HttpFactory.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(HttpFactory.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        HttpFactory.getInstance().init(this)                     //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数
    }
}