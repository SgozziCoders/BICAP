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
import it.unimib.bicap.wrapper.DataWrapper;
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

    public void getIndaginiHeadList(final MutableLiveData<DataWrapper<IndaginiHeadList>> indaginiHeadListWrapperMLD, String email){
        Call<IndaginiHeadList> call = indaginiService.getIndaginiHeadJson(email, Constants.API_AUTHORIZATION_TOKEN);
        call.enqueue(new Callback<IndaginiHeadList>() {
            @Override
            public void onResponse(Call<IndaginiHeadList> call, Response<IndaginiHeadList> response) {
                indaginiHeadListWrapperMLD.postValue(new DataWrapper<>(null, response.body()));
            }

            @Override
            public void onFailure(Call<IndaginiHeadList> call, Throwable t) {
                indaginiHeadListWrapperMLD.postValue(new DataWrapper<>(new Exception(t), null));
            }
        });
    }

    public void getRemoteIndagineBody(final MutableLiveData<DataWrapper<IndagineBody>> indagineBodyWrapperMLD, int indagineId){
        Call<IndagineBody> call = indaginiService.getIndagineBodyJson(Constants.INDAGINE_BODY_API_URL + indagineId, Constants.API_AUTHORIZATION_TOKEN);
        call.enqueue(new Callback<IndagineBody>() {
            @Override
            public void onResponse(Call<IndagineBody> call, Response<IndagineBody> response) {
                /**
                 * Prima di postare i valori bisogna controllare se la response.body() è null;
                 * in caso positivo siamo in stato di errore da parte del backend
                 **/
                indagineBodyWrapperMLD.postValue(new DataWrapper<>(null, response.body()));
            }

            @Override
            public void onFailure(Call<IndagineBody> call, Throwable t) {
                indagineBodyWrapperMLD.postValue(new DataWrapper<>(new Exception(t), null));
            }
        });
    }

    public void putIndagineTerminata(String email, int idIndagine, OnCallBackListener onCallBackListener) {
        Call<ResponseBody> call = indaginiService.putIndagineTerminata(email, idIndagine, new Post(true), Constants.API_AUTHORIZATION_TOKEN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                onCallBackListener.onPutFinished();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onCallBackListener.onPutFail(t.getMessage());
            }
        });
    }

    public void getLocalIndagineBody(final MutableLiveData<DataWrapper<IndagineBody>> indagineBodyWrapperMLD, int indagineId, String dataDir){
        try{
            File mIndagineBodyFile = new File(dataDir + Constants.INDAGINI_IN_CORSO_PATH + indagineId + ".json");
            IndagineBody mIndagineBodyLocal = new Gson().fromJson(new BufferedReader(new FileReader(mIndagineBodyFile.getAbsolutePath())), IndagineBody.class);
            indagineBodyWrapperMLD.postValue(new DataWrapper<>(null, mIndagineBodyLocal));
        }catch(Exception ex){
            indagineBodyWrapperMLD.postValue(new DataWrapper<>(ex, null));
        }
    }

    public interface OnCallBackListener {
        public void onPutFinished();
        public void onPutFail(String error);
    }

}
