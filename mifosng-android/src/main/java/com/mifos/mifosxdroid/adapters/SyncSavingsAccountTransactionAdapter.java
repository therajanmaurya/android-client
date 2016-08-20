package com.mifos.mifosxdroid.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mifos.mifosxdroid.R;
import com.mifos.objects.accounts.savings.SavingsAccountTransactionRequest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rajan Maurya on 19/08/16.
 */
public class SyncSavingsAccountTransactionAdapter extends
        RecyclerView.Adapter<SyncSavingsAccountTransactionAdapter.ViewHolder> {

    List<SavingsAccountTransactionRequest> mSavingsAccountTransactionRequests;

    @Inject
    public SyncSavingsAccountTransactionAdapter() {
        mSavingsAccountTransactionRequests = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sync_savings_account_transaction, parent, false);
        return new ViewHolder(view);
    }


    public void setSavingsAccountTransactions(
            List<SavingsAccountTransactionRequest> savingsAccountTransactions) {
        mSavingsAccountTransactionRequests = savingsAccountTransactions;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SavingsAccountTransactionRequest transaction =
                mSavingsAccountTransactionRequests.get(position);

        holder.tv_savings_account_id.setText(transaction.getSavingAccountId());
        holder.tv_transaction_type.setText(transaction.getTransactionType());
        holder.tv_transaction_amount.setText(transaction.getTransactionAmount());
        holder.tv_transaction_date.setText(transaction.getTransactionDate());

    }

    @Override
    public int getItemCount() {
        return mSavingsAccountTransactionRequests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_db_savings_account_id)
        TextView tv_savings_account_id;

        @BindView(R.id.tv_db_payment_type)
        TextView tv_payment_type;

        @BindView(R.id.tv_db_transaction_type)
        TextView tv_transaction_type;

        @BindView(R.id.tv_db_transaction_amount)
        TextView tv_transaction_amount;

        @BindView(R.id.tv_db_transaction_date)
        TextView tv_transaction_date;

        @BindView(R.id.tv_error_message)
        TextView tv_error_message;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
