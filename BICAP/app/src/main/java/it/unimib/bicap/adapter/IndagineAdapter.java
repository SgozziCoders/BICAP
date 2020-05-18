package it.unimib.bicap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.bicap.R;

import it.unimib.bicap.model.IndaginiHeadList;


public class IndagineAdapter extends RecyclerView.Adapter<IndagineAdapter.IndagineViewHolder>{

    private OnCardListener onCardListener;
    private IndaginiHeadList indaginiHeadList;

    public IndagineAdapter(IndaginiHeadList indaginiHeadList, OnCardListener onCardListener){
        this.indaginiHeadList = indaginiHeadList;
        this.onCardListener = onCardListener;
    }

    @Override
    public int getItemCount() {
        return indaginiHeadList.getHeads().size();
    }

    @Override
    public IndagineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.indagine_card, viewGroup, false);
        IndagineViewHolder ivh = new IndagineViewHolder(v, onCardListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull IndagineViewHolder holder, int position) {
        holder.titoloTextView.setText(indaginiHeadList.getHeads().get(position).getTitoloIndagine());
        holder.erogatoreTextView.setText(indaginiHeadList.getHeads().get(position).getErogatore());

        //https://stackoverflow.com/questions/32136973/how-to-get-a-context-in-a-recycler-view-adapter
        Glide.with(holder.indagineImageView.getContext())
                .load(indaginiHeadList.getHeads().get(position).getImgUrl())
                .centerCrop()
                .placeholder(R.drawable.square_avatar_rounded)
                .signature(new ObjectKey(indaginiHeadList.getHeads().get(position).getultimaModifica()))
                .into(holder.indagineImageView);
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