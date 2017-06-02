package com.oldnum7.business;

import android.support.annotation.NonNull;

import com.oldnum7.base.App;
import com.oldnum7.data.TasksRepository;
import com.oldnum7.data.UserEntity;
import com.oldnum7.data.local.TasksLocalDataSource;
import com.oldnum7.data.remote.TasksRemoteDataSource;
import com.oldnum7.mvp.BaseMvpPresenter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * author : denglin
 * time   : 2017/05/31/13:54
 * desc   :
 * version: 1.0
 */
public class MainPresenter extends BaseMvpPresenter<IMainContract.View> implements IMainContract.Presenter {

    private final String TAG = getClass().getSimpleName();

    @NonNull
    private final TasksRepository mTasksRepository = TasksRepository.getInstance(TasksRemoteDataSource.getInstance(), TasksLocalDataSource.getInstance(App.getmContext()));

    private CompositeDisposable mSubscriptions = new CompositeDisposable();

    private boolean mFirstLoad = true;

    public MainPresenter() {
    }

    @Override
    public void getUsers(int since, int per_page) {

    }

    @Override
    public void subscribe() {
        loadData(false);
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    public void loadData(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadData(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }


    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link }
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadData(final boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            getView().setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mTasksRepository.refreshTasks();
        }


        mSubscriptions.clear();

        Observable<List<UserEntity>> tasks = mTasksRepository
                .getUsers(10, 10);
        Observable<UserEntity> userObservable = tasks
                .flatMap(new Function<List<UserEntity>, ObservableSource<UserEntity>>() {
                    @Override
                    public ObservableSource<UserEntity> apply(List<UserEntity> userEntities) throws Exception {
                        return Observable.fromIterable(userEntities);
                    }
                });

        SingleObserver<List<UserEntity>> observer = userObservable
                .filter(new Predicate<UserEntity>() {
                    @Override
                    public boolean test(UserEntity userEntity) throws Exception {
                        return true;
                    }
                })
//                .doFinally()
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().setLoadingIndicator(false);
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<List<UserEntity>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull List<UserEntity> userEntities) {
                        getView().getUsers(userEntities);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });
//        mSubscriptions.add(observer.);
    }

    private void processTasks(@NonNull List<UserEntity> tasks) {
        if (tasks.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
//            processEmptyTasks();
        } else {
            // Show the list of tasks
//            mTasksView.showTasks(tasks);
            // Set the filter label's text.
//            showFilterLabel();
        }
    }
//    @Override
//    public void getUsers(int since, int per_page) {

//        Call<List<UserEntity>> call = mUserService.getUsers(since, per_page);
//        call.enqueue(new Callback<List<UserEntity>>() {
//            @Override
//            public void onResponse(Call<List<UserEntity>> call, Response<List<UserEntity>> response) {
//                Log.e(TAG, "onResponse: " + response.body().toString());
//                getView().getUsers(response.body());
//            }

//            @Override
//            public void onFailure(Call<List<UserEntity>> call, Throwable t) {
//                Log.e(TAG, "onResponse: " + t.toString());
//                getView().showNetWorkError();
//            }
//        });

//    }
}
