package it.unimib.bicap.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import it.unimib.bicap.R;
import it.unimib.bicap.utils.Constants;

public class EmptyListFragment extends Fragment {
    private TextView mMessageTextView;

    public EmptyListFragment(){

    }

    public static EmptyListFragment newInstance(){
        return new EmptyListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.message_fragment, container, false);
       mMessageTextView = (TextView) v.findViewById(R.id.messageTextView);
       return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null){
            mMessageTextView.setText(getArguments().getString(Constants.EMPTY_FRAGMENT_MESSAGE));
        }
    }
}
