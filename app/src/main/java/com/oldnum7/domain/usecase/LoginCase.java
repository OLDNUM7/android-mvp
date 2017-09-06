package com.oldnum7.domain.usecase;

import com.oldnum7.data.TasksRepository;
import com.oldnum7.data.entity.LoginEntity;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * <pre>
 *       author : denglin
 *       time   : 2017/09/06/13:51
 *       desc   : 登录的用例...
 *       version: 1.0
 * </pre>
 */
public class LoginCase extends UseCase<LoginEntity, LoginCase.Params> {

    @Inject
    TasksRepository mTasksRepository;

    @Inject
    public LoginCase(TasksRepository tasksRepository) {
        this.mTasksRepository = tasksRepository;
    }

    @Override
    Observable<LoginEntity> buildUseCaseObservable(Params params) {

        return mTasksRepository.login(params.userName, params.pwd);
    }

    public static final class Params {

        private String userName;
        private String pwd;

        public Params(String userName, String pwd) {
            this.userName = userName;
            this.pwd = pwd;
        }

        public static LoginCase.Params params(String userName, String pwd) {
            return new Params(userName, pwd);
        }
    }
}
