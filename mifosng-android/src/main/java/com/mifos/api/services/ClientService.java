/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package com.mifos.api.services;

import com.mifos.objects.client.Client;
import com.mifos.objects.client.Page;
import com.mifos.api.model.APIEndPoint;
import com.mifos.api.model.ClientPayload;
import com.mifos.objects.templates.clients.ClientsTemplate;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;
import rx.Observable;

/**
 * @author fomenkoo
 */
public interface ClientService {

    //This is a default call and Loads client from 0 to 200
    @GET(APIEndPoint.CLIENTS)
    Observable<Page<Client>> listAllClients();

    @GET(APIEndPoint.CLIENTS)
    Observable<Page<Client>> listAllClients(@Query("offset") int offset, @Query("limit") int limit);

    @GET(APIEndPoint.CLIENTS + "/{clientId}")
    Observable<Client> getClient(@Path("clientId") int clientId);

    @Multipart
    @POST(APIEndPoint.CLIENTS + "/{clientId}/images")
    Observable<Response> uploadClientImage(@Path("clientId") int clientId,
                           @Part("file") TypedFile file);

    @DELETE(APIEndPoint.CLIENTS + "/{clientId}/images")
    Observable<Response> deleteClientImage(@Path("clientId") int clientId);

    //TODO: Implement when API Fixed
    @GET("/clients/{clientId}/images")
    void getClientImage(@Path("clientId") int clientId, Callback<TypedString> callback);

    @POST(APIEndPoint.CLIENTS)
    Observable<Client> createClient(@Body ClientPayload clientPayload);

    @GET(APIEndPoint.CLIENTS + "/template")
    Observable<ClientsTemplate> getClientTemplate();
}
