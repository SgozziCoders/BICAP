package it.unimib.bicap.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.bicap.model.IndagineBody;
import it.unimib.bicap.repository.IndaginiRepository;
import it.unimib.bicap.wrapper.DataWrapper;

public class IndagineBodyViewModel extends ViewModel {
    private MutableLiveData<DataWrapper<IndagineBody>> indagineBody;

    public LiveData<DataWrapper<IndagineBody>> loadRemoteIndagineBody(int idIndagine){
        if(indagineBody == null){
            indagineBody = new MutableLiveData<>();
            IndaginiRepository.getInstance().getRemoteIndagineBody(indagineBody, idIndagine);
        }
        return indagineBody;
    }

    public LiveData<DataWrapper<IndagineBody>> loadLoacalIndagineBody(int idIndagine, String datadir){
        if(indagineBody == null){
            indagineBody = new MutableLiveData<>();
            IndaginiRepository.getInstance().getLocalIndagineBody(indagineBody, idIndagine, datadir);
        }
        return indagineBody;
    }

}
