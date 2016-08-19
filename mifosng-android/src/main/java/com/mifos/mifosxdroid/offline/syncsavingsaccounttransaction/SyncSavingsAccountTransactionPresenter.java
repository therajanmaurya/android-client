package com.mifos.mifosxdroid.offline.syncsavingsaccounttransaction;

import com.mifos.api.datamanager.DataManagerSavings;
import com.mifos.mifosxdroid.base.BasePresenter;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rajan Maurya on 19/08/16.
 */
@Singleton
public class SyncSavingsAccountTransactionPresenter extends
        BasePresenter<SyncSavingsAccountTransactionMvpView>{


    public final DataManagerSavings mDataManagerSavings;
    private CompositeSubscription mSubscriptions;

    @Inject
    public SyncSavingsAccountTransactionPresenter(DataManagerSavings dataManagerSavings) {
        mDataManagerSavings = dataManagerSavings;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(SyncSavingsAccountTransactionMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        mSubscriptions.unsubscribe();
    }



}
