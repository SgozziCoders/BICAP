package it.unimib.bicap.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.unimib.bicap.adapter.QuestionarioAdapter;

import static java.util.Arrays.asList;

public class CardsViewModel extends ViewModel implements QuestionarioAdapter.VisibilityListener {
    List<Boolean> visibilityList;

    public List<Boolean> getVisibilityList(int size) {
        if(visibilityList != null)
            return visibilityList;
        else{
            visibilityList = new ArrayList<Boolean>();
            for(int i=0; i<size; i++)
                visibilityList.add(false);
            return visibilityList;
        }
    }

    private void setVisibilityValue(int position, Boolean val){
        visibilityList.set(position, val);
    }

    /**
     * In ascolto dei click delle card dei questionari; ogni volta che riceve l'evento click
     * aggiorna la lista di Booleani della visibilità
     */
    @Override
    public void OnExpandClick(int position, Boolean visibility) {
        setVisibilityValue(position, visibility);
    }
}
