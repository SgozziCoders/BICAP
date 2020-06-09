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
import it.unimib.bicap.R;
import it.unimib.bicap.model.IndaginiHeadList;
import it.unimib.bicap.utils.Constants;


public class IndagineAdapter extends RecyclerView.Adapter<IndagineAdapter.IndagineViewHolder>{

    private OnCardListener mOnCardListener;
    private IndaginiHeadList mindaginiHeadList;

    public IndagineAdapter(IndaginiHeadList indaginiHeadList, OnCardListener onCardListener){
        this.mindaginiHeadList = indaginiHeadList;
        this.mOnCardListener = onCardListener;
    }

    @Override
    public int getItemCount() {
        return mindaginiHeadList.getHeads().size();
    }

    @Override
    public IndagineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.indagine_card, viewGroup, false);
        return new IndagineViewHolder(mView, mOnCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull IndagineViewHolder holder, int position) {
        holder.mTitoloTextView.setText(mindaginiHeadList.getHeads().get(position).getTitoloIndagine());
        holder.mErogatoreTextView.setText(mindaginiHeadList.getHeads().get(position).getErogatore());
        String imgUrl = Constants.BACKEND_URL + mindaginiHeadList.getHeads().get(position).getImgUrl();

        Glide.with(holder.mIndagineImageView.getContext())
                .load(mindaginiHeadList.getHeads().get(position).getImgUrl())
                .centerCrop()
                .placeholder(R.drawable.square_avatar_rounded)
                .signature(new ObjectKey(mindaginiHeadList.getHeads().get(position).getUltimaModifica()))
                .into(holder.mIndagineImageView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class IndagineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView mCardView;
        TextView mTitoloTextView;
        TextView mErogatoreTextView;
        ImageView mIndagineImageView;
        OnCardListener mOnCardListener;

        public IndagineViewHolder(View itemView, OnCardListener mOnCardListener) {
            super(itemView);
            mCardView = (CardView)itemView.findViewById(R.id.model_card);
            mTitoloTextView = (TextView)itemView.findViewById(R.id.titoloTextView);
            mErogatoreTextView = (TextView)itemView.findViewById(R.id.erogatoreTextView);
            mIndagineImageView = (ImageView)itemView.findViewById(R.id.indagineImageView);
            this.mOnCardListener = mOnCardListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnCardListener.onCardClick(getAdapterPosition());
        }
    }

    public interface OnCardListener{
        void onCardClick(int position);
    }
}