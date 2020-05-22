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
import androidx.recyclerview.widget.RecyclerView;

import it.unimib.bicap.IndagineActivity;
import it.unimib.bicap.adapter.IndagineAdapter;
import it.unimib.bicap.model.IndagineHead;
import it.unimib.bicap.model.IndaginiHeadList;
import it.unimib.bicap.R;

public class FragmentInCorso extends Fragment implements IndagineAdapter.OnCardListener{
    View v;
    IndaginiHeadList indaginiHeadList;

    public FragmentInCorso(IndaginiHeadList indaginiHeadList){
        super();
        this.indaginiHeadList = indaginiHeadList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        v = inflater.inflate(R.layout.in_corso_fragment, container, false);
        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.inCorsoRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        IndagineAdapter mAdapter = new IndagineAdapter(indaginiHeadList, this);
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }


    @Override
    public void onCardClick(int position) {
        IndagineHead mIndagineHead = indaginiHeadList.getHeads().get(position);
        Intent mIntent = new Intent(getContext(), IndagineActivity.class);
        mIntent.putExtra("Indagine", mIndagineHead);
        startActivity(mIntent);
    }


}
