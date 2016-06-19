package com.mifos.mifosxdroid;

import com.google.gson.reflect.TypeToken;
import com.mifos.objects.SearchedEntity;
import com.mifos.objects.client.Client;
import com.mifos.objects.client.Page;
import com.mifos.objects.group.Center;
import com.mifos.objects.group.CenterWithAssociations;

import java.util.List;

/**
 * Created by Rajan Maurya on 18/6/16.
 */
public class FakeRemoteDataSource {

    private static TestDataFactory mTestDataFactory = new TestDataFactory();


    public static Page<Client> getClientList() {
        return mTestDataFactory.getObjectTypePojo(Page.class, FakeJsonName.CLIENTS_JSON);
    }

    public static List<SearchedEntity> getSearchedEntity() {
        return mTestDataFactory.getListTypePojo(new TypeToken<List<SearchedEntity>>() {
                                                },
                FakeJsonName.SEARCHED_ENTITY_JSON);
    }

    public static Page<Center> getCenters() {
        return mTestDataFactory.getListTypePojo(new TypeToken<Page<Center>>(){},
                FakeJsonName.CENTERS_JSON);
    }

    public static CenterWithAssociations getCentersGroupAndMeeting() {
        return mTestDataFactory.getObjectTypePojo(CenterWithAssociations.class,
                FakeJsonName.CENTER_WITH_ASSOCIATIONS_JSON);
    }
}
