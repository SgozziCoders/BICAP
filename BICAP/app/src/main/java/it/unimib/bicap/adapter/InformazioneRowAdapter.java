package it.unimib.bicap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicap.R;

import java.util.List;

import it.unimib.bicap.model.Informazione;

public class InformazioneRowAdapter extends RecyclerView.Adapter<InformazioneRowAdapter.InformazioneRowViewHolder> {

    private List<Informazione> informazioneList;
    private OnInformazioneRowListener onInformazioneRowListener;

    public InformazioneRowAdapter(List<Informazione> informazioneList, OnInformazioneRowListener onInformazioneRowListener){
        this.informazioneList = informazioneList;
        this.onInformazioneRowListener = onInformazioneRowListener;
    }

    @NonNull
    @Override
    public InformazioneRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.info_row, viewGroup, false);
        InformazioneRowViewHolder irvh = new InformazioneRowViewHolder(v, onInformazioneRowListener);
        return irvh;
    }

    @Override
    public void onBindViewHolder(@NonNull InformazioneRowViewHolder holder, int position) {
        holder.infoTextView.setText(informazioneList.get(position).getNomeFile());
        setIconImage(informazioneList.get(position).getTipoFile(), holder.iconImageView);
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
        return informazioneList.size();
    }


    public class InformazioneRowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView infoTextView;
        ImageView iconImageView;
        OnInformazioneRowListener onInformazioneRowListener;

        public InformazioneRowViewHolder(@NonNull View itemView, OnInformazioneRowListener onInformazioneRowListener) {
            super(itemView);
            infoTextView = (TextView) itemView.findViewById(R.id.infoTextView);
            iconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
            this.onInformazioneRowListener = onInformazioneRowListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onInformazioneRowListener.OnInfoRowClick(getAdapterPosition());
        }
    }

    public interface OnInformazioneRowListener{
        public void OnInfoRowClick(int position);
    }

}
