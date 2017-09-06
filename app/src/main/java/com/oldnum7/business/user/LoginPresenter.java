package com.oldnum7.business.user;

import android.app.Activity;

import com.oldnum7.data.entity.LoginEntity;
import com.oldnum7.domain.usecase.LoginCase;
import com.oldnum7.http.callback.DialogHttpObserver;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

/**
 * <pre>
 *       author : denglin
 *       time   : 2017/09/06/13:49
 *       desc   :
 *       version: 1.0
 * </pre>
 */
public class LoginPresenter extends ILoginContract.Presenter {

    private LoginCase mLoginCase;

    @Inject
    LoginPresenter(LoginCase loginCase) {
        this.mLoginCase = loginCase;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


    @Override
    void login(String userName, String pwd) {

        ILoginContract.View view = getView();

        mLoginCase.execute(new DialogHttpObserver<LoginEntity>((Activity) view) {
            @Override
            public void onNext(@NonNull LoginEntity loginEntity) {

            }
        },new LoginCase.Params(userName,pwd));
    }
}
