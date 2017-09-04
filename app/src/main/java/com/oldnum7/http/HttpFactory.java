package com.oldnum7.http;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.oldnum7.BuildConfig;
import com.oldnum7.Constants;
import com.oldnum7.base.App;
import com.oldnum7.http.cache.CacheMode;
import com.oldnum7.http.model.HttpHeaders;
import com.oldnum7.http.model.HttpParams;
import com.oldnum7.http.utils.HttpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * <pre>
 *       author : denglin
 *       time   : 2017/06/02/17:28
 *       desc   : Http 请求封装
 *       version: 1.0
 * </pre>
 */

public class HttpFactory {
    private static final String TAG = "HttpFactory";
    public static final long DEFAULT_MILLISECONDS = 60000;      //默认的超时时间

    private static HttpFactory sInstance;

    private Application mContext;            //全局上下文
    private Handler mDelivery;              //用于在主线程执行的调度器
    private OkHttpClient okHttpClient;      //ok请求的客户端
    private HttpParams mCommonParams;       //全局公共请求参数
    private HttpHeaders mCommonHeaders;     //全局公共请求头
    private int mRetryCount;                //全局超时重试次数
    private CacheMode mCacheMode;           //全局缓存模式
    private long mCacheTime;                //全局缓存过期时间,默认永不过期

    //------------------------------------------------
    private static final long CACHE_SIZE = 1024 * 1024 * 10;

    // base url for Http request
    private String mBaseUrl;

    private long mReadTimeout = 10000;

    private long mWriteTimeout = 10000;

    private long mConnectTimeout = 10000;

    // 是否失败重连
    private boolean mRetryOnConnectionFailure = true;

    // ssl 管理
    private SSLSocketFactory mSslSocketFactory;

    private TrustManager[] trustAllCerts;

    private X509TrustManager mX509TrustManager;

    private HostnameVerifier hostnameVerifier;

    private List<Interceptor> interceptors = new ArrayList<>();

    private List<Interceptor> networkInterceptors = new ArrayList<>();

    private OkHttpClient mOkHttpClient;

    private Retrofit mRetrofit;

    //----------------------------------------------------------------------------------------------------

    public static HttpFactory getInstance() {
        if (sInstance == null) {
            synchronized (HttpFactory.class) {
                if (sInstance == null) {
                    sInstance = new HttpFactory();
                }
            }
        }
        return sInstance;
    }

    HttpFactory() {
        mDelivery = new Handler(Looper.getMainLooper());
        mRetryCount = 3;
//        mCacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        mCacheMode = CacheMode.NO_CACHE;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        builder.addInterceptor(loggingInterceptor);

        builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        okHttpClient = builder.build();

    }

    public HttpFactory init(Application app) {
        this.mContext = app;
        return this;
    }

    /**
     * 获取全局上下文
     */
    public Context getContext() {
        HttpUtils.checkNotNull(mContext, "please call HttpFactory.getInstance().init() first in application!");
        return mContext;
    }



    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        HttpUtils.checkNotNull(okHttpClient, "please call OkGo.getInstance().setOkHttpClient() first in application!");
        return okHttpClient;
    }

    /** 必须设置 */
    public HttpFactory setOkHttpClient(OkHttpClient okHttpClient) {
        HttpUtils.checkNotNull(okHttpClient, "okHttpClient == null");
        this.okHttpClient = okHttpClient;
        return this;
    }

    /** 获取全局的cookie实例 */
//    public CookieJarImpl getCookieJar() {
//        return (CookieJarImpl) okHttpClient.cookieJar();
//    }

    /** 超时重试次数 */
    public HttpFactory setRetryCount(int retryCount) {
        if (retryCount < 0) throw new IllegalArgumentException("retryCount must > 0");
        mRetryCount = retryCount;
        return this;
    }

    /** 超时重试次数 */
    public int getRetryCount() {
        return mRetryCount;
    }

    /** 全局的缓存模式 */
    public HttpFactory setCacheMode(CacheMode cacheMode) {
        mCacheMode = cacheMode;
        return this;
    }

    /** 获取全局的缓存模式 */
    public CacheMode getCacheMode() {
        return mCacheMode;
    }

    /** 全局的缓存过期时间 */
//    public HttpFactory setCacheTime(long cacheTime) {
//        if (cacheTime <= -1) cacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
//        mCacheTime = cacheTime;
//        return this;
//    }

    /** 获取全局的缓存过期时间 */
    public long getCacheTime() {
        return mCacheTime;
    }

    /** 获取全局公共请求参数 */
    public HttpParams getCommonParams() {
        return mCommonParams;
    }

    /** 添加全局公共请求参数 */
    public HttpFactory addCommonParams(HttpParams commonParams) {
        if (mCommonParams == null) mCommonParams = new HttpParams();
        mCommonParams.put(commonParams);
        return this;
    }

    /** 获取全局公共请求头 */
    public HttpHeaders getCommonHeaders() {
        return mCommonHeaders;
    }

    /** 添加全局公共请求参数 */
    public HttpFactory addCommonHeaders(HttpHeaders commonHeaders) {
        if (mCommonHeaders == null) mCommonHeaders = new HttpHeaders();
        mCommonHeaders.put(commonHeaders);
        return this;
    }

    /** 根据Tag取消请求 */
    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /** 根据Tag取消请求 */
    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /** 取消所有请求请求 */
    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /** 取消所有请求请求 */
    public static void cancelAll(OkHttpClient client) {
        if (client == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }


    //------------------------------------------------------------------------------------------------------
    // set base url
    private void setBaseUrl() {
        mBaseUrl = Constants.HTTP_BASE_URL;
    }


    private void createHttpClient() {
        File cacheFile = new File(App.getmContext().getCacheDir(), "app_cache");
        Cache cache = new Cache(cacheFile, CACHE_SIZE);

        // create OkHttpClient instance
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // network interceptor
//        builder.addNetworkInterceptor(new StethoInterceptor());
        //设置读、写、连接超时
        builder.readTimeout(mReadTimeout, TimeUnit.MILLISECONDS);
        builder.connectTimeout(mConnectTimeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(mWriteTimeout, TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(mRetryOnConnectionFailure);
        builder.cache(cache);

        // add application interceptor
        if (interceptors.size() > 0) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        // add Network interceptor
        if (networkInterceptors.size() > 0) {
            for (Interceptor interceptor : networkInterceptors) {
                builder.addNetworkInterceptor(interceptor);
            }
        }

        // confirge ssl
//        builder.hostnameVerifier(hostnameVerifier);
//        builder.sslSocketFactory(mSslSocketFactory, mX509TrustManager);

        mOkHttpClient = builder.build();
    }

    private void createRetrofit() {
        // create Retrofit.Builder instance
        mRetrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();
    }

    public <T> T createService(Class<T> clazz) {

        return mRetrofit.create(clazz);
    }
}
