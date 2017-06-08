package com.oldnum7.business;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.oldnum7.R;
import com.oldnum7.adapter.UserAdapter;
import com.oldnum7.data.entity.UserEntity;
import com.oldnum7.mvp.BaseActivity;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity<IMainContract.View, IMainContract.Presenter> implements IMainContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_list)
    RecyclerView mRvList;
    @BindView(R.id.sr_refresh)
    SwipeRefreshLayout mSrRefresh;

    private MainPresenter mMainPresenter;
    private UserAdapter mUserAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainPresenter.setStatusLayoutManager(getStatusLayoutManager());
    }

    @NonNull
    @Override
    public IMainContract.Presenter createPresenter() {
        mMainPresenter = new MainPresenter();
        return mMainPresenter;
    }

    @Override
    protected void initViews() {

    }


    @Override
    protected void loadData() {
        showLoading();
//        mMainPresenter.getUsers(mSince, 10);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMainPresenter.subscribe();
            }
        }, 2000);

    }

    @Override
    public void setLoadingIndicator(final boolean active) {

        mSrRefresh.post(() -> mSrRefresh.setRefreshing(active));
    }

    @Override
    public void getUsers(List<UserEntity> users) {
        getStatusLayoutManager().showContent();

        mUserAdapter.setNewData(users);

        if (mSrRefresh.isRefreshing()) {//刷新
            mUserAdapter.setEnableLoadMore(true);
            mSrRefresh.setRefreshing(false);
            mUserAdapter.setNewData(users);

        }
    }

    @Override
    public void showLoading() {
        getStatusLayoutManager().showLoading();
    }

    @Override
    public void showError() {
        getStatusLayoutManager().showError();

    }

    @Override
    protected void initEvent() {
        initAdapter();
        mSrRefresh.setOnRefreshListener(this);
    }

    @Override
    public void showNetWorkError() {
        getStatusLayoutManager().showNetWorkError();
    }

    private void initAdapter() {
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.hasFixedSize();
        mUserAdapter = new UserAdapter(R.layout.item_user, null);
//        mUserAdapter.setOnLoadMoreListener(this, mRvList);
        mRvList.setAdapter(mUserAdapter);
    }

    @Override
    public void onRefresh() {
        mMainPresenter.loadData(true);
    }

    @Override
    public void setPresenter(Object presenter) {

    }
}
