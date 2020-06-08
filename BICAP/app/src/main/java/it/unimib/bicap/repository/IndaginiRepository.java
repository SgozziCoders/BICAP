package it.unimib.bicap.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import it.unimib.bicap.model.IndagineBody;
import it.unimib.bicap.model.IndaginiHeadList;
import it.unimib.bicap.model.Post;
import it.unimib.bicap.service.IndaginiService;
import it.unimib.bicap.utils.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IndaginiRepository {
    private static IndaginiRepository instance;
    private IndaginiService indaginiService;

    private IndaginiRepository() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BACKEND_URL + "/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        indaginiService = retrofit.create(IndaginiService.class);
    }

    public static synchronized IndaginiRepository getInstance(){
        if(instance == null){
            instance = new IndaginiRepository();
        }
        return instance;
    }

    public void getIndaginiHeadList(final MutableLiveData<IndaginiHeadList> indaginiHeadListMutableLiveData, String email){
        Call<IndaginiHeadList> call = indaginiService.getIndaginiHeadJson(email);
        call.enqueue(new Callback<IndaginiHeadList>() {
            @Override
            public void onResponse(Call<IndaginiHeadList> call, Response<IndaginiHeadList> response) {
                indaginiHeadListMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<IndaginiHeadList> call, Throwable t) {
                // Gestione fallimento della richiesta al server (Non comprende il 404 Not Found)
            }
        });
    }

    public void getRemoteIndagineBody(final MutableLiveData<IndagineBody> indagineBodyMutableLiveData, int indagineId){
        Call<IndagineBody> call = indaginiService.getIndagineBodyJson(Constants.INDAGINE_BODY_API_URL + indagineId);
        call.enqueue(new Callback<IndagineBody>() {
            @Override
            public void onResponse(Call<IndagineBody> call, Response<IndagineBody> response) {
                indagineBodyMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<IndagineBody> call, Throwable t) {
                // Gestione fallimento della richiesta al server (Non comprende il 404 Not Found)
            }
        });
    }

    public void putIndagineTerminata(String email, int idIndagine){
        Call<ResponseBody> call = indaginiService.putIndagineTerminata(email, idIndagine, new Post(true));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Gestione fallimento della richiesta al server (Non comprende il 404 Not Found)
            }
        });
    }

    public void getLocalIndagineBody(final MutableLiveData<IndagineBody> indagineBodyMutableLiveData, int indagineId, String dataDir){
        try{
            File mIndagineBodyFile = new File(dataDir + Constants.INDAGINI_IN_CORSO_PATH + indagineId + ".json");
            IndagineBody mIndagineBodyLocal = new Gson().fromJson(new BufferedReader(new FileReader(mIndagineBodyFile.getAbsolutePath())), IndagineBody.class);
            indagineBodyMutableLiveData.postValue(mIndagineBodyLocal);
        }catch(Exception ex){
            return;
        }
    }


}
