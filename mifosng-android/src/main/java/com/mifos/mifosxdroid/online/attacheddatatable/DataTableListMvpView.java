package com.mifos.mifosxdroid.online.attacheddatatable;

import com.mifos.mifosxdroid.base.MvpView;

public interface DataTableListMvpView extends MvpView {

    void showMessage(int messageId, String successOrFail);
}
