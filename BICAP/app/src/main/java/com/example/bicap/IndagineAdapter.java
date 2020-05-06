package com.example.bicap;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class IndagineAdapter extends RecyclerView.Adapter<IndagineAdapter.IndagineViewHolder>{

    private OnCardListener onCardListener;
    private List<IndagineHead> indagini;

    IndagineAdapter(List<IndagineHead> indagini, OnCardListener onCardListener){
        this.indagini = indagini;
        this.onCardListener = onCardListener;
    }

    @Override
    public int getItemCount() {
        return indagini.size();
    }

    @Override
    public IndagineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.indagine_card, viewGroup, false);
        IndagineViewHolder ivh = new IndagineViewHolder(v, onCardListener);
        return ivh;
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

    public static class IndagineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView titoloTextView;
        TextView erogatoreTextView;
        ImageView indagineImageView;
        OnCardListener onCardListener;

        public IndagineViewHolder(View itemView, OnCardListener onCardListener) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.model_card);
            titoloTextView = (TextView)itemView.findViewById(R.id.titoloTextView);
            erogatoreTextView = (TextView)itemView.findViewById(R.id.erogatoreTextView);
            indagineImageView = (ImageView)itemView.findViewById(R.id.indagineImageView);
            this.onCardListener = onCardListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCardListener.onCardClick(getAdapterPosition());
        }
    }

    public interface OnCardListener{
        void onCardClick(int position);
    }
}