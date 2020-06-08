package it.unimib.bicap.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.bicap.model.IndaginiHeadList;
import it.unimib.bicap.repository.IndaginiRepository;

public class IndagineHeadListViewModel extends ViewModel {
    private static MutableLiveData<IndaginiHeadList> indaginiHeadList;

    public LiveData<IndaginiHeadList> loadIndaginiHeadList(String email){
        if(indaginiHeadList == null){
            indaginiHeadList = new MutableLiveData<>();
            IndaginiRepository.getInstance().getIndaginiHeadList(indaginiHeadList, email);
        }
        return indaginiHeadList;
    }

    public void RemoveById(int idIndagine){
        indaginiHeadList.getValue().getHeads().remove(indaginiHeadList.getValue().
                getIndagineHeadFromId(idIndagine));
    }
}
