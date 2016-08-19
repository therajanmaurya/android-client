package com.mifos.mifosxdroid.offline.syncsavingsaccounttransaction;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mifos.mifosxdroid.R;
import com.mifos.mifosxdroid.core.MifosBaseActivity;
import com.mifos.mifosxdroid.core.MifosBaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rajan Maurya on 19/08/16.
 */
public class SyncSavingsAccountTransactionFragment extends MifosBaseFragment implements
        SyncSavingsAccountTransactionMvpView {


    public final String LOG_TAG = getClass().getSimpleName();

    @BindView(R.id.rv_sync_payload)
    RecyclerView rv_loan_repayment;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.noPayloadText)
    TextView mNoPayloadText;

    @BindView(R.id.noPayloadIcon)
    ImageView mNoPayloadIcon;

    @BindView(R.id.ll_error)
    LinearLayout ll_error;

    @Inject
    SyncSavingsAccountTransactionPresenter mSyncSavingsAccountTransactionPresenter;

    private View rootView;


    public static SyncSavingsAccountTransactionFragment newInstance() {
        Bundle arguments = new Bundle();
        SyncSavingsAccountTransactionFragment sync = new SyncSavingsAccountTransactionFragment();
        sync.setArguments(arguments);
        return sync;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MifosBaseActivity) getActivity()).getActivityComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_syncpayload, container, false);

        ButterKnife.bind(this, rootView);
        mSyncSavingsAccountTransactionPresenter.attachView(this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_loan_repayment.setLayoutManager(mLayoutManager);
        rv_loan_repayment.setHasFixedSize(true);
        //rv_loan_repayment.setAdapter(mSyncLoanRepaymentAdapter);

        return rootView;
    }

    @Override
    public void showProgressbar(boolean b) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSyncSavingsAccountTransactionPresenter.detachView();
    }

    @Override
    public void showError() {

    }
}
