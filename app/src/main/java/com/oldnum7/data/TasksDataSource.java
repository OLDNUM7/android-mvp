package com.oldnum7.data;


import java.util.List;

import io.reactivex.Observable;

/**
 * <pre>
 *       author : denglin
 *       time   : 2017/06/01/14:44
 *       desc   :Interface that represents a Repository for getting {@link UserEntity} related data.
 *       version: 1.0
 * </pre>
 */
public interface TasksDataSource {
    /**
     * Get an {@link Observable} which will emit a List of {@link UserEntity}.
     */
    Observable<List<UserEntity>> users(int since, int page);
}
