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
import it.unimib.bicap.R;

import java.util.List;

import it.unimib.bicap.model.Informazione;

public class InformazioneAdapter extends RecyclerView.Adapter<InformazioneAdapter.InformazioneViewHolder> {

    private OnInfoCardListener mOnInfoCardListener;
    private List<Informazione> mInformazioneList;

    public InformazioneAdapter(List<Informazione> mInformazioneList, OnInfoCardListener mOnInfoCardListener){
        this.mInformazioneList = mInformazioneList;
        this.mOnInfoCardListener = mOnInfoCardListener;
    }

    @Override
    public int getItemCount(){
        return mInformazioneList.size();
    }

    @Override
    public InformazioneViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.square_info_card, viewGroup, false);
        return new InformazioneViewHolder(mView, mOnInfoCardListener);
    }

    public void onBindViewHolder(@NonNull InformazioneViewHolder holder, int position) {
        holder.mInformazioneTextView.setText(mInformazioneList.get(position).getNomeFile());
        Glide.with(holder.mInformazioneImageView.getContext())
                .load(mInformazioneList.get(position).getThumbnailUrl())
                .centerCrop()
                .placeholder(R.drawable.square_avatar_rounded)
                .signature(new ObjectKey(mInformazioneList.get(position).getultimaModifica()))
                .into(holder.mInformazioneImageView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class InformazioneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mInformazioneImageView;
        TextView mInformazioneTextView;
        OnInfoCardListener mOnInfoCardListener;

        public InformazioneViewHolder(View itemView, OnInfoCardListener mOnInfoCardListener){
            super(itemView);
            mInformazioneImageView = (ImageView) itemView.findViewById(R.id.informazioneImageView);
            mInformazioneTextView = (TextView) itemView.findViewById(R.id.informazioneTextView);
            this.mOnInfoCardListener = mOnInfoCardListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnInfoCardListener.onInfoCardClick(getAdapterPosition());
        }
    }

    public interface OnInfoCardListener{
        void onInfoCardClick(int position);
    }
}
