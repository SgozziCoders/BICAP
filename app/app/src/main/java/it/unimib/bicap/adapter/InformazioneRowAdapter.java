package it.unimib.bicap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.unimib.bicap.R;

import java.util.List;

import it.unimib.bicap.model.Informazione;

public class InformazioneRowAdapter extends RecyclerView.Adapter<InformazioneRowAdapter.InformazioneRowViewHolder> {

    private List<Informazione> mInformazioneList;
    private OnInformazioneRowListener mOnInformazioneRowListener;

    public InformazioneRowAdapter(List<Informazione> mInformazioneList, OnInformazioneRowListener mOnInformazioneRowListener){
        this.mInformazioneList = mInformazioneList;
        this.mOnInformazioneRowListener = mOnInformazioneRowListener;
    }

    @NonNull
    @Override
    public InformazioneRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.info_row, viewGroup, false);
        return new InformazioneRowViewHolder(mView, mOnInformazioneRowListener);
    }

    @Override
    public void onBindViewHolder(@NonNull InformazioneRowViewHolder holder, int position) {
        holder.mInfoTextView.setText(mInformazioneList.get(position).getNomeFile());
        setIconImage(mInformazioneList.get(position).getTipoFile(), holder.mIconImageView);
    }

    private void setIconImage(String tipoFile, ImageView iconImageView) {
        String temp = tipoFile.split("/")[0];
        switch(temp){
            case "application":
                iconImageView.setImageResource(R.drawable.pdf_icon2);
                break;
            case "video":
                iconImageView.setImageResource(R.drawable.video_icon);
                break;
            case "audio":
                iconImageView.setImageResource(R.drawable.audio_icon);
                break;
            case "text":
                iconImageView.setImageResource(R.drawable.text_icon);
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mInformazioneList.size();
    }


    public class InformazioneRowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mInfoTextView;
        ImageView mIconImageView;
        OnInformazioneRowListener mOnInformazioneRowListener;

        public InformazioneRowViewHolder(@NonNull View itemView, OnInformazioneRowListener mOnInformazioneRowListener) {
            super(itemView);
            mInfoTextView = (TextView) itemView.findViewById(R.id.infoTextView);
            mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
            this.mOnInformazioneRowListener = mOnInformazioneRowListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnInformazioneRowListener.OnInfoRowClick(getAdapterPosition());
        }
    }

    public interface OnInformazioneRowListener{
        public void OnInfoRowClick(int position);
    }

}
