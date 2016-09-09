package com.mifos.mifosxdroid.online.groupslist;

import com.mifos.api.datamanager.DataManagerGroups;
import com.mifos.mifosxdroid.R;
import com.mifos.mifosxdroid.base.BasePresenter;
import com.mifos.objects.client.Page;
import com.mifos.objects.group.Group;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rajan Maurya on 7/6/16.
 */
public class GroupsListPresenter extends BasePresenter<GroupsListMvpView> {


    private final DataManagerGroups mDataManagerGroups;
    private CompositeSubscription mSubscriptions;

    private int limit = 100;
    private Boolean loadmore = false;

    @Inject
    public GroupsListPresenter(DataManagerGroups dataManagerGroups) {
        mDataManagerGroups = dataManagerGroups;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(GroupsListMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        mSubscriptions.unsubscribe();
    }

    public void loadGroups(Boolean loadmore, int offset) {
        this.loadmore = loadmore;
        loadGroups(true, offset, limit);
    }

    /**
     * Showing Groups List in View, If loadmore is true call showLoadMoreGroups(...) and else
     * call showGroupsList(...).
     */
    public void showClientList(List<Group> clients) {
        if (loadmore) {
            getMvpView().showLoadMoreGroups(clients);
        } else {
            getMvpView().showGroups(clients);
        }
    }


    public void loadGroups(boolean paged, int offset, int limit) {
        checkViewAttached();
        getMvpView().showProgressbar(true);
        mSubscriptions.add(mDataManagerGroups.getGroups(paged, offset, limit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Page<Group>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                        if (loadmore) {
                            getMvpView().showMessage(R.string.failed_to_fetch_groups);
                        } else {
                            getMvpView().showFetchingError();
                        }

                    }

                    @Override
                    public void onNext(Page<Group> groupPage) {
                        getMvpView().showProgressbar(false);
                        showClientList(groupPage.getPageItems());
                    }
                }));
    }
}
