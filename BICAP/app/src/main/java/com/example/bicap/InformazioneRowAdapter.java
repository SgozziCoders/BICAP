package com.example.bicap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InformazioneRowAdapter extends RecyclerView.Adapter<InformazioneRowAdapter.InformazioneRowViewHolder> {

    private List<Informazione> informazioneList;

    InformazioneRowAdapter(List<Informazione> informazioneList){
        this.informazioneList = informazioneList;
    }

    @NonNull
    @Override
    public InformazioneRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.info_row, viewGroup, false);
        InformazioneRowViewHolder irvh = new InformazioneRowViewHolder(v);
        return irvh;
    }

    @Override
    public void onBindViewHolder(@NonNull InformazioneRowViewHolder holder, int position) {
        holder.infoTextView.setText(informazioneList.get(position).getNomeFile());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return informazioneList.size();
    }


    public class InformazioneRowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView infoTextView;
        ImageView iconImageView;

        public InformazioneRowViewHolder(@NonNull View itemView) {
            super(itemView);
            infoTextView = (TextView) itemView.findViewById(R.id.infoTextView);
            iconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Probabile utilizzo di un custom click listener che gestisce il download e l'apertura del file
        }
    }
}
