package com.mifos.mifosxdroid.online.clientidentifiers;

import com.mifos.api.GenericResponse;
import com.mifos.api.datamanager.DataManagerClient;
import com.mifos.mifosxdroid.base.BasePresenter;
import com.mifos.objects.noncore.Identifier;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rajan Maurya on 06/06/16.
 */
public class ClientIdentifiersPresenter extends BasePresenter<ClientIdentifiersMvpView> {

    private final DataManagerClient mdataManagerClient;
    private CompositeSubscription mSubscriptions;

    @Inject
    public ClientIdentifiersPresenter(DataManagerClient dataManagerClient) {
        mdataManagerClient = dataManagerClient;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(ClientIdentifiersMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        mSubscriptions.clear();
    }

    public void loadIdentifiers(int clientId) {
        checkViewAttached();
        getMvpView().showProgressbar(true);
        mSubscriptions.add(mdataManagerClient.getClientIdentifiers(clientId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Identifier>>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().showProgressbar(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                        getMvpView().showFetchingError("Error to Load Identifiers");
                    }

                    @Override
                    public void onNext(List<Identifier> identifiers) {
                        getMvpView().showProgressbar(false);
                        if (!identifiers.isEmpty()) {
                            getMvpView().showClientIdentifiers(identifiers);
                        } else {
                            getMvpView().showEmptyClientIdentifier();
                        }
                    }
                }));
    }

    public void deleteIdentifier(final int clientId, int identifierId) {
        checkViewAttached();
        getMvpView().showProgressbar(true);
        mSubscriptions.add(mdataManagerClient.deleteClientIdentifier(clientId, identifierId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<GenericResponse>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().showProgressbar(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                        getMvpView().showFetchingError("Failed to delete Identifier");
                    }

                    @Override
                    public void onNext(GenericResponse genericResponse) {
                        getMvpView().identifierDeletedSuccessfully("Successfully deleted");
                        getMvpView().showProgressbar(false);
                    }
                }));
    }

}