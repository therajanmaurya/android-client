package com.mifos.mifosxdroid.offline.syncsavingsaccounttransaction;

import com.mifos.api.datamanager.DataManagerLoan;
import com.mifos.api.datamanager.DataManagerSavings;
import com.mifos.mifosxdroid.base.BasePresenter;
import com.mifos.objects.PaymentTypeOption;
import com.mifos.objects.accounts.savings.SavingsAccountTransactionRequest;
import com.mifos.objects.accounts.savings.SavingsAccountTransactionResponse;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rajan Maurya on 19/08/16.
 */
public class SyncSavingsAccountTransactionPresenter extends
        BasePresenter<SyncSavingsAccountTransactionMvpView>{


    public final DataManagerSavings mDataManagerSavings;
    public final DataManagerLoan mDataManagerLoan;
    private CompositeSubscription mSubscriptions;

    @Inject
    public SyncSavingsAccountTransactionPresenter(DataManagerSavings dataManagerSavings,
                                                  DataManagerLoan dataManagerLoan) {
        mDataManagerSavings = dataManagerSavings;
        mDataManagerLoan = dataManagerLoan;
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
                    public void onNext(List<SavingsAccountTransactionRequest> transactionRequests) {
                        getMvpView().showProgressbar(false);
                        if (!transactionRequests.isEmpty()) {
                            getMvpView().showSavingsAccountTransactions(transactionRequests);
                        } else {
                            getMvpView().showEmptySavingsAccountTransactions();
                        }
                    }
                })
        );
    }


    public void loadPaymentTypeOption() {
        checkViewAttached();
        getMvpView().showProgressbar(true);
        mSubscriptions.add(mDataManagerLoan.getPaymentTypeOption()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<PaymentTypeOption>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<PaymentTypeOption> paymentTypeOptions) {
                        getMvpView().showProgressbar(false);
                        getMvpView().showPaymentTypeOptions(paymentTypeOptions);
                    }
                }));
    }


    public void processTransaction(String type, int accountId, String transactionType,
                                   SavingsAccountTransactionRequest request) {
        checkViewAttached();
        getMvpView().showProgressbar(true);
        mSubscriptions.add(mDataManagerSavings
                .processTransaction(type, accountId, transactionType, request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SavingsAccountTransactionResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                        //getMvpView().showError(R.string.transaction_failed);
                    }

                    @Override
                    public void onNext(SavingsAccountTransactionResponse
                                               savingsAccountTransactionResponse) {
                        getMvpView().showProgressbar(false);
                    }
                }));
    }


    public void deleteAndUpdateSavingsAccountTransaction(int savingsAccountId) {
        checkViewAttached();
        getMvpView().showProgressbar(true);
        mSubscriptions.add(mDataManagerSavings.deleteAndUpdateTransactions(savingsAccountId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<SavingsAccountTransactionRequest>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<SavingsAccountTransactionRequest>
                                               savingsAccountTransactionRequests) {

                    }
                })
        );
    }

    public void updateSavingsAccountTransaction(SavingsAccountTransactionRequest request) {
        checkViewAttached();
        getMvpView().showProgressbar(true);
        mSubscriptions.add(mDataManagerSavings.updateLoanRepaymentTransaction(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SavingsAccountTransactionRequest>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SavingsAccountTransactionRequest
                                               savingsAccountTransactionRequest) {

                    }
                })
        );
    }
}
