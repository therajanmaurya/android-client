package com.mifos.mifosxdroid.offline.syncsavingsaccounttransaction;

import com.mifos.api.datamanager.DataManagerSavings;
import com.mifos.mifosxdroid.base.BasePresenter;
import com.mifos.objects.accounts.savings.SavingsAccountTransactionRequest;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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


    public void loadDatabaseSavingsAccountTransactions() {
        checkViewAttached();
        getMvpView().showProgressbar(true);
        mSubscriptions.add(mDataManagerSavings.getAllSavingsAccountTransactions()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<SavingsAccountTransactionRequest>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<SavingsAccountTransactionRequest>
                                               savingsAccountTransactionRequests) {
                        getMvpView().showProgressbar(false);

                    }
                })
        );
    }
}
