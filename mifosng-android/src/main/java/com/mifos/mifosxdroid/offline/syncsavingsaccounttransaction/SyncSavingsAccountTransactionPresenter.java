package com.mifos.mifosxdroid.offline.syncsavingsaccounttransaction;

import com.google.gson.Gson;
import com.mifos.api.datamanager.DataManagerLoan;
import com.mifos.api.datamanager.DataManagerSavings;
import com.mifos.mifosxdroid.R;
import com.mifos.mifosxdroid.base.BasePresenter;
import com.mifos.objects.ErrorSyncServerMessage;
import com.mifos.objects.PaymentTypeOption;
import com.mifos.objects.accounts.savings.SavingsAccountTransactionRequest;
import com.mifos.objects.accounts.savings.SavingsAccountTransactionResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rajan Maurya on 19/08/16.
 */
public class SyncSavingsAccountTransactionPresenter extends
        BasePresenter<SyncSavingsAccountTransactionMvpView>{

    public final String LOG_TAG = getClass().getSimpleName();

    public final DataManagerSavings mDataManagerSavings;
    public final DataManagerLoan mDataManagerLoan;
    private CompositeSubscription mSubscriptions;

    private List<SavingsAccountTransactionRequest> mSavingsAccountTransactionRequests;

    private int mTransactionIndex = 0;

    @Inject
    public SyncSavingsAccountTransactionPresenter(DataManagerSavings dataManagerSavings,
                                                  DataManagerLoan dataManagerLoan) {
        mDataManagerSavings = dataManagerSavings;
        mDataManagerLoan = dataManagerLoan;
        mSubscriptions = new CompositeSubscription();
        mSavingsAccountTransactionRequests = new ArrayList<>();
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


    public void syncSavingsAccountTransactions() {
        if (mSavingsAccountTransactionRequests.size() != 0) {
            mTransactionIndex = 0;
            checkErrorAndSync();
        } else {
            getMvpView().showError(R.string.nothing_to_sync);
        }
    }


    public void checkErrorAndSync() {
        for (int i = 0; i < mSavingsAccountTransactionRequests.size(); ++i) {
            if (mSavingsAccountTransactionRequests.get(i).getErrorMessage() == null) {

                mTransactionIndex = i;

                String savingAccountType =
                        mSavingsAccountTransactionRequests.get(i).getSavingsAccountType();
                int savingAccountId =
                        mSavingsAccountTransactionRequests.get(i).getSavingAccountId();
                String transactionType = mSavingsAccountTransactionRequests
                        .get(i).getTransactionType();
                processTransaction(savingAccountType, savingAccountId, transactionType,
                        mSavingsAccountTransactionRequests.get(i));

                break;
            } else {
                getMvpView().showError(R.string.error_fix_before_sync);
            }
        }
    }


    public void showTransactionSyncSuccessfully() {
        deleteAndUpdateSavingsAccountTransaction(
                mSavingsAccountTransactionRequests.get(mTransactionIndex).getSavingAccountId());
    }

    public void showTransactionSyncFailed(ErrorSyncServerMessage errorMessage) {
        SavingsAccountTransactionRequest transaction = mSavingsAccountTransactionRequests.get
                (mTransactionIndex);
        transaction.setErrorMessage(errorMessage.getDefaultUserMessage());
        updateSavingsAccountTransaction(transaction);
    }

    public void showTransactionUpdatedSuccessfully(SavingsAccountTransactionRequest transaction) {
        mSavingsAccountTransactionRequests.set(mTransactionIndex, transaction);
        getMvpView().showSavingsAccountTransactions(mSavingsAccountTransactionRequests);

        mTransactionIndex = mTransactionIndex + 1;
        if (mSavingsAccountTransactionRequests.size() != mTransactionIndex) {
            syncSavingsAccountTransactions();
        }
    }

    public void showTransactionDeletedAndUpdated(
            List<SavingsAccountTransactionRequest> transactions) {

        mTransactionIndex = 0;
        mSavingsAccountTransactionRequests = transactions;
        getMvpView().showSavingsAccountTransactions(transactions);
        if (mSavingsAccountTransactionRequests.size() != 0) {
            syncSavingsAccountTransactions();
        } else {
            getMvpView().showEmptySavingsAccountTransactions(R.string.nothing_to_sync);
        }

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
                        getMvpView().showError(R.string.failed_to_load_savingaccounttransaction);
                    }

                    @Override
                    public void onNext(List<SavingsAccountTransactionRequest> transactionRequests) {
                        getMvpView().showProgressbar(false);
                        if (!transactionRequests.isEmpty()) {
                            getMvpView().showSavingsAccountTransactions(transactionRequests);
                            mSavingsAccountTransactionRequests = transactionRequests;
                        } else {
                            getMvpView().showEmptySavingsAccountTransactions(
                                    R.string.no_transaction_to_sync);
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
                        getMvpView().showError(R.string.failed_to_load_paymentoptions);
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
                        try {
                            if (e instanceof HttpException) {
                                String errorMessage = ((HttpException) e).response().errorBody()
                                        .string();
                                Gson gson = new Gson();
                                ErrorSyncServerMessage syncErrorMessage = gson.
                                        fromJson(errorMessage, ErrorSyncServerMessage.class);
                                getMvpView().showProgressbar(false);
                                showTransactionSyncFailed(syncErrorMessage);
                            }
                        } catch (Throwable throwable) {
                            RxJavaPlugins.getInstance().getErrorHandler().handleError(throwable);
                        }
                    }

                    @Override
                    public void onNext(SavingsAccountTransactionResponse
                                               savingsAccountTransactionResponse) {
                        getMvpView().showProgressbar(false);
                        showTransactionSyncSuccessfully();
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
                        getMvpView().showProgressbar(false);
                        getMvpView().showError(R.string.failed_to_update_list);
                    }

                    @Override
                    public void onNext(List<SavingsAccountTransactionRequest>
                                               savingsAccountTransactionRequests) {
                        getMvpView().showProgressbar(false);
                        showTransactionDeletedAndUpdated(savingsAccountTransactionRequests);
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
                        getMvpView().showProgressbar(false);
                        getMvpView().showError(R.string.failed_to_update_savingsaccount);
                    }

                    @Override
                    public void onNext(SavingsAccountTransactionRequest
                                               savingsAccountTransactionRequest) {
                        getMvpView().showProgressbar(false);
                        showTransactionUpdatedSuccessfully(savingsAccountTransactionRequest);
                    }
                })
        );
    }
}
