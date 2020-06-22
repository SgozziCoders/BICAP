package it.unimib.bicap.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.bicap.model.IndaginiHeadList;
import it.unimib.bicap.repository.IndaginiRepository;
import it.unimib.bicap.wrapper.DataWrapper;

public class IndagineHeadListViewModel extends ViewModel {
    private static MutableLiveData<DataWrapper<IndaginiHeadList>> indaginiHeadListWrapper;

    public LiveData<DataWrapper<IndaginiHeadList>> loadIndaginiHeadList(String email){
        if(indaginiHeadListWrapper == null){
            indaginiHeadListWrapper = new MutableLiveData<>();
            IndaginiRepository.getInstance().getIndaginiHeadList(indaginiHeadListWrapper, email);
        }
        return indaginiHeadListWrapper;
    }

    public void RemoveById(int idIndagine){
        indaginiHeadListWrapper.getValue().getData().getHeads().remove(indaginiHeadListWrapper.
                getValue().getData().getIndagineHeadFromId(idIndagine));
    }

    /**
     * Utilizzato per pulire i dati statici, che altrimenti rimangono popolati anche se
     * l'applicazione viene chiusa (senza elimanrla dalle app recenti) per esempio in
     * seguito ad un errore di connessione.
     **/
    public void Clear(){
        indaginiHeadListWrapper = null;
    }
}
