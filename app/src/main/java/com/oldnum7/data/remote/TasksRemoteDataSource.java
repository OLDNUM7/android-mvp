package com.oldnum7.data.remote;

import android.support.annotation.NonNull;

import com.oldnum7.business.ApiService;
import com.oldnum7.data.TasksDataSource;
import com.oldnum7.http.HttpFactory;
import com.oldnum7.http.Transformer.HttpTransformer;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * <pre>
 *       author : denglin
 *       time   : 2017/06/01/14:55
 *       desc   : Implementation of the data source that adds a latency simulating network.
 *       version: 1.0
 * </pre>
 */
@Singleton
public class TasksRemoteDataSource implements TasksDataSource {
    private HttpFactory mHttpManager;
    private ApiService mApiService;

    // Prevent direct instantiation.
    @Inject
    TasksRemoteDataSource() {
//        mHttpManager = new HttpFactory.Builder().build();
    }

    @Override
    public Observable<List<T>> getUsers(int since, int per_page) {
        mApiService = mHttpManager.createService(ApiService.class);
        return mApiService.getUsers(since, per_page);
    }

    @Override
    public Observable<List<T>> getUsers() {
        mApiService = mHttpManager.createService(ApiService.class);

        return mApiService.getUsers().compose(HttpTransformer.expTransformer());
    }

    @Override
    public void saveTask(@NonNull T userEntity) {

    }
}
