package it.unimib.bicap.service;

import org.json.JSONObject;

import it.unimib.bicap.model.IndagineBody;
import it.unimib.bicap.model.IndaginiHeadList;
import it.unimib.bicap.model.Post;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface IndaginiService {

    @GET("indagineHeadList/")
    Call<IndaginiHeadList> getIndaginiHeadJson(@Query("email") String email,
                                               @Header("Authorization") String token);

    @GET()
    Call<IndagineBody> getIndagineBodyJson(@Url String url,
                                           @Header("Authorization") String token);

    @PUT("distribuzione/")
    Call<ResponseBody> putIndagineTerminata(@Query("email") String email,
                                            @Query("idIndagine") int idIndagine,
                                            @Body Post post,
                                            @Header("Authorization") String token);
}
