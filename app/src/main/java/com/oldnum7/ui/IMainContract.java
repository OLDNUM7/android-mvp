package com.oldnum7.ui;


import com.oldnum7.androidlib.mvp.persenter.BasePresenter;
import com.oldnum7.androidlib.mvp.view.MvpView;

/**
 * <pre>
 *       author : denglin
 *       time   : 2017/05/31/14:11
 *       desc   :
 *       version: 1.0
 * </pre>
 */
public interface IMainContract {

    interface View extends MvpView {
        void setLoadingIndicator(boolean active);

//        void getUsers(List<T> users);

        void showLoading();

        void showError();

        void showNetWorkError();
    }

    abstract class Presenter extends BasePresenter<View> {

        abstract void loadData(boolean forceUpdate);

        abstract void getUsers(int since, int per_page);

        abstract void getUsers();
    }
}
