package com.example.bicap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.IndagineViewHolder>{
    public static class IndagineViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView titoloTextView;
        TextView erogatoreTextView;
        ImageView indagineImageView;

        IndagineViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.model_card);
            titoloTextView = (TextView)itemView.findViewById(R.id.titoloTextView);
            erogatoreTextView = (TextView)itemView.findViewById(R.id.erogatoreTextView);
            indagineImageView = (ImageView)itemView.findViewById(R.id.indagineImageView);
        }
    }

    List<IndagineHead> indagini;
    RVAdapter(List<IndagineHead> indagini){
        this.indagini = indagini;
    }

    @Override
    public int getItemCount() {
        return indagini.size();
    }

    @Override
    public IndagineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.indagine_card, viewGroup, false);
        IndagineViewHolder pvh = new IndagineViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull IndagineViewHolder holder, int position) {
        holder.titoloTextView.setText(indagini.get(position).getTitoloIndagine());
        holder.erogatoreTextView.setText(indagini.get(position).getErogatore());
        //IndagineViewHolder.indagineImageView.setImageResource(indagini.get(i).getImgUrl());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}