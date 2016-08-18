package com.mifos.api.local.databasehelper;

import com.mifos.objects.PaymentTypeOption;
import com.mifos.objects.accounts.savings.SavingsAccountWithAssociations;
import com.mifos.objects.accounts.savings.SavingsAccountWithAssociations_Table;
import com.mifos.objects.accounts.savings.SavingsTransactionDate;
import com.mifos.objects.accounts.savings.Transaction;
import com.mifos.objects.accounts.savings.Transaction_Table;
import com.mifos.objects.templates.savings.SavingsAccountTransactionTemplate;
import com.mifos.objects.templates.savings.SavingsAccountTransactionTemplate_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Created by Rajan Maurya on 17/08/16.
 */
@Singleton
public class DatabaseHelperSavings {


    @Inject
    public DatabaseHelperSavings() {
    }


    public Observable<SavingsAccountWithAssociations> saveSavingsAccount(
            final SavingsAccountWithAssociations savingsAccountWithAssociations) {
        return Observable.defer(new Func0<Observable<SavingsAccountWithAssociations>>() {
            @Override
            public Observable<SavingsAccountWithAssociations> call() {

                List<Transaction> transactions = savingsAccountWithAssociations.getTransactions();

                if (transactions.size() != 0) {
                    Observable.from(transactions)
                            .flatMap(new Func1<Transaction, Observable<Transaction>>() {
                                @Override
                                public Observable<Transaction> call(Transaction transaction) {
                                    return Observable.just(transaction);
                                }
                            })
                            .subscribe(new Action1<Transaction>() {
                                @Override
                                public void call(Transaction transaction) {
                                    SavingsTransactionDate savingsTransactionDate =
                                            new SavingsTransactionDate(transaction.getId(),
                                                    transaction.getDate().get(0),
                                                    transaction.getDate().get(1),
                                                    transaction.getDate().get(2));
                                    transaction.setSavingsAccountId
                                            (savingsAccountWithAssociations.getId());
                                    transaction.setSavingsTransactionDate(savingsTransactionDate);
                                    transaction.save();
                                }
                            });
                }
                savingsAccountWithAssociations.getSummary().setSavingsId
                        (savingsAccountWithAssociations.getId());
                savingsAccountWithAssociations.save();

                return Observable.just(savingsAccountWithAssociations);
            }
        });
    }


    public Observable<SavingsAccountWithAssociations> readSavingsAccount(
            final int savingsAccountId) {
        return Observable.defer(new Func0<Observable<SavingsAccountWithAssociations>>() {
            @Override
            public Observable<SavingsAccountWithAssociations> call() {

                SavingsAccountWithAssociations savingsAccountWithAssociations = SQLite.select()
                        .from(SavingsAccountWithAssociations.class)
                        .where(SavingsAccountWithAssociations_Table.id.eq(savingsAccountId))
                        .querySingle();


                List<Transaction> transactions = SQLite.select()
                        .from(Transaction.class)
                        .where(Transaction_Table.savingsAccountId.eq(savingsAccountId))
                        .queryList();

                Observable.from(transactions)
                        .flatMap(new Func1<Transaction, Observable<Transaction>>() {
                            @Override
                            public Observable<Transaction> call(Transaction transaction) {
                                return Observable.just(transaction);
                            }
                        })
                        .subscribe(new Action1<Transaction>() {
                            @Override
                            public void call(Transaction transaction) {
                                transaction.setDate(Arrays.asList(
                                        transaction.getSavingsTransactionDate().getYear(),
                                        transaction.getSavingsTransactionDate().getMonth(),
                                        transaction.getSavingsTransactionDate().getDay()));
                            }
                        });

                if (savingsAccountWithAssociations != null) {
                    savingsAccountWithAssociations.setTransactions(transactions);
                }

                return Observable.just(savingsAccountWithAssociations);
            }
        });
    }

    public Observable<SavingsAccountTransactionTemplate> saveSavingsAccountTransactionTemplate
            (final SavingsAccountTransactionTemplate savingsAccountTransactionTemplate) {
        return Observable.defer(new Func0<Observable<SavingsAccountTransactionTemplate>>() {
            @Override
            public Observable<SavingsAccountTransactionTemplate> call() {

                Observable.from(savingsAccountTransactionTemplate.getPaymentTypeOptions())
                        .subscribe(new Action1<PaymentTypeOption>() {
                            @Override
                            public void call(PaymentTypeOption paymentTypeOption) {
                                paymentTypeOption.save();
                            }
                        });

                savingsAccountTransactionTemplate.save();

                return Observable.just(savingsAccountTransactionTemplate);
            }
        });
    }

    public Observable<SavingsAccountTransactionTemplate> readSavingsAccountTransactionTemplate
            (final int savingsAccountId) {
        return Observable.defer(new Func0<Observable<SavingsAccountTransactionTemplate>>() {
            @Override
            public Observable<SavingsAccountTransactionTemplate> call() {

                SavingsAccountTransactionTemplate savingsAccountTransactionTemplate =
                        SQLite.select()
                                .from(SavingsAccountTransactionTemplate.class)
                                .where(SavingsAccountTransactionTemplate_Table
                                        .accountId.eq(savingsAccountId))
                                .querySingle();

                List<PaymentTypeOption> paymentTypeOptions = SQLite.select()
                        .from(PaymentTypeOption.class)
                        .queryList();

                if (savingsAccountTransactionTemplate != null) {
                    savingsAccountTransactionTemplate.setPaymentTypeOptions(paymentTypeOptions);
                }

                return Observable.just(savingsAccountTransactionTemplate);
            }
        });
    }
}
