package it.unimib.bicap.service;

import it.unimib.bicap.model.IndagineBody;
import it.unimib.bicap.model.IndaginiHeadList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface IndaginiService {
    @GET("indagineHeadList/")
    Call<IndaginiHeadList> getIndaginiHeadJson(@Query("email") String email);

    // api/
    @GET()
    Call<IndagineBody> getIndagineBodyJson(@Url String url);
}
