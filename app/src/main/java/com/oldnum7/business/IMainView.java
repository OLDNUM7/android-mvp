package com.oldnum7.business;

import com.oldnum7.data.entity.UserEntity;
import com.oldnum7.mvp.MvpView;

import java.util.List;

/**
 * <pre>
 *       author : denglin
 *       time   : 2017/05/31/14:20
 *       desc   :
 *       version: 1.0
 * </pre>
 */
public interface IMainView extends MvpView {

    void getUsers(List<UserEntity> users);

    void showLoading();

    void showError();

    void showNetWorkError();

}
