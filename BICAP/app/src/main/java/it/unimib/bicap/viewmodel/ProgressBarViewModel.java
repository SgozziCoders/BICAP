package it.unimib.bicap.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProgressBarViewModel extends ViewModel {
    MutableLiveData<Integer> progressValue;

    public MutableLiveData<Integer> getProgressValue(){
        if(progressValue == null){
            progressValue = new MutableLiveData<>();
        }
        return progressValue;
    }
}
