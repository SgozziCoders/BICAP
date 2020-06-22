package it.unimib.bicap.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import it.unimib.bicap.IndagineActivity;
import it.unimib.bicap.adapter.IndagineAdapter;
import it.unimib.bicap.databinding.DisponibiliFragmentBinding;
import it.unimib.bicap.model.IndagineHead;
import it.unimib.bicap.model.IndaginiHeadList;
import it.unimib.bicap.utils.Constants;

public class FragmentDisponibili extends Fragment implements IndagineAdapter.OnCardListener{
    private IndaginiHeadList indaginiHeadList;
    private DisponibiliFragmentBinding binding;

    public FragmentDisponibili(){

    }

    public static FragmentDisponibili newInstance(){
        return new FragmentDisponibili();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DisponibiliFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null){
            indaginiHeadList = getArguments().getParcelable(Constants.INDAGINI_HEAD_LIST_ARG);
        }

        binding.disponibiliRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.disponibiliRecyclerView.setLayoutManager(mLinearLayoutManager);

        IndagineAdapter mAdapter = new IndagineAdapter(indaginiHeadList, this);
        binding.disponibiliRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onCardClick(int position) {
        IndagineHead mIndagineHead = indaginiHeadList.getHeads().get(position);
        Intent mIntent = new Intent(getContext(), IndagineActivity.class);
        mIntent.putExtra(Constants.INDAGINE_HEAD_ARG, mIndagineHead);
        startActivity(mIntent);
    }

}
