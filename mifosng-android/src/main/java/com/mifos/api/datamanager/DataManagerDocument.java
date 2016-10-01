package com.mifos.api.datamanager;

import com.mifos.api.BaseApiManager;
import com.mifos.api.GenericResponse;
import com.mifos.objects.noncore.Document;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Rajan Maurya on 02/09/16.
 */
@Singleton
public class DataManagerDocument {

    public final BaseApiManager mBaseApiManager;

    @Inject
    public DataManagerDocument(BaseApiManager baseApiManager) {
        mBaseApiManager = baseApiManager;
    }


    public Observable<List<Document>> getDocumentsList(String type, int id) {
        return mBaseApiManager.getDocumentApi().getDocuments(type, id);
    }

    public Observable<GenericResponse> createDocument(
            String type, int id, String name, String desc, MultipartBody.Part file) {
        return mBaseApiManager.getDocumentApi().createDocument(type, id, name, desc, file);
    }

    public Observable<ResponseBody> downloadDocument(String entityType, int entityId,
                                                     int documentId) {
        return mBaseApiManager.getDocumentApi().downloadDocument(entityType, entityId, documentId);
    }

    public Observable<GenericResponse> removeDocument(String entityType, int entityId,
                                                      int documentId) {
        return mBaseApiManager.getDocumentApi().removeDocument(entityType, entityId, documentId);
    }

    public Observable<GenericResponse> updateDocument(String entityType, int entityId, int
            documentId, String name, String desc, MultipartBody.Part file) {
        return mBaseApiManager.getDocumentApi()
                .updateDocument(entityType, entityId, documentId, name, desc, file);
    }
}
