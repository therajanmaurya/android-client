package com.mifos.api.datamanager;

import com.mifos.api.BaseApiManager;
import com.mifos.api.local.databasehelper.DatabaseHelperSurveys;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Rajan Maurya on 22/08/16.
 */
@Singleton
public class DataManagerSurveys {

    public final BaseApiManager mBaseApiManager;
    public final DatabaseHelperSurveys mDatabaseHelperSurveys;

    @Inject
    public DataManagerSurveys(BaseApiManager baseApiManager,
                              DatabaseHelperSurveys databaseHelperSurveys) {
        mBaseApiManager = baseApiManager;
        mDatabaseHelperSurveys = databaseHelperSurveys;
    }


}
