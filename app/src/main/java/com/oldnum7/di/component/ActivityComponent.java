package com.oldnum7.di.component;

import com.oldnum7.di.annotation.ActivityScoped;
import com.oldnum7.di.module.ActivityModule;
import com.oldnum7.ui.login.LoginFragment;
import com.oldnum7.ui.main.MainActivity;
import com.oldnum7.ui.user.LoginActivity;

import dagger.Component;

/**
 * <pre>
 *       author : denglin
 *       time   : 2017/06/16/14:36
 *       desc   :
 *       version: 1.0
 * </pre>
 */

@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(LoginActivity activity);

    void inject(LoginFragment fragment);

//    void inject(Tab1Fragment fragment);
//
//    void inject(Tab2Fragment fragment);
//
//    void inject(Tab3Fragment fragment);

}
