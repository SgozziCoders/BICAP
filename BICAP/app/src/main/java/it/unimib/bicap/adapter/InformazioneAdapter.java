package it.unimib.bicap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.bicap.R;

import java.util.List;

import it.unimib.bicap.model.Informazione;

public class InformazioneAdapter extends RecyclerView.Adapter<InformazioneAdapter.InformazioneViewHolder> {

    private OnInfoCardListener onInfoCardListener;
    private List<Informazione> informazioneList;

    public InformazioneAdapter(List<Informazione> informazioneList, OnInfoCardListener onInfoCardListener){
        this.informazioneList = informazioneList;
        this.onInfoCardListener = onInfoCardListener;
    }

    @Override
    public int getItemCount(){
        return informazioneList.size();
    }

    @Override
    public InformazioneViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.square_info_card, viewGroup, false);
        InformazioneViewHolder ivh = new InformazioneViewHolder(v, onInfoCardListener);
        return ivh;
    }

    public void onBindViewHolder(@NonNull InformazioneViewHolder holder, int position) {
        holder.informazioneTextView.setText(informazioneList.get(position).getNomeFile());
        Glide.with(holder.informazioneImageView.getContext())
                .load(informazioneList.get(position).getThumbnailUrl())
                .centerCrop()
                .placeholder(R.drawable.square_avatar_rounded)
                .signature(new ObjectKey(informazioneList.get(position).getultimaModifica()))
                .into(holder.informazioneImageView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class InformazioneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView informazioneImageView;
        TextView informazioneTextView;
        OnInfoCardListener onInfoCardListener;

        public InformazioneViewHolder(View itemView, OnInfoCardListener onInfoCardListener){
            super(itemView);
            informazioneImageView = (ImageView) itemView.findViewById(R.id.informazioneImageView);
            informazioneTextView = (TextView) itemView.findViewById(R.id.informazioneTextView);
            this.onInfoCardListener = onInfoCardListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onInfoCardListener.onInfoCardClick(getAdapterPosition());
        }
    }

    public interface OnInfoCardListener{
        void onInfoCardClick(int position);
    }
}
