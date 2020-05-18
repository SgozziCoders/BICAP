package it.unimib.bicap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.example.bicap.R;

import java.util.List;

import it.unimib.bicap.utils.ParcelableBoolean;
import it.unimib.bicap.model.Informazione;
import it.unimib.bicap.model.Questionario;

public class QuestionarioAdapter extends RecyclerView.Adapter<QuestionarioAdapter.QuestionarioViewHolder> {

    private List<Questionario> mQuestionarioList;
    private List<ParcelableBoolean> mCardsVisibility;
    private Context mContext;
    private InformazioneRowReciver mInformazioneRowReciver;
    private OnSubmitClickListener mOnSubmitClickListener;

    public QuestionarioAdapter(List<Questionario> mQuestionarioList, Context mContext,
                               OnSubmitClickListener mOnSubmitClickListener,
                               InformazioneRowReciver mInformazioneRowReciver,
                               List<ParcelableBoolean> mCardsVisibility){
        this.mQuestionarioList = mQuestionarioList;
        this.mContext = mContext;
        this.mOnSubmitClickListener = mOnSubmitClickListener;
        this.mInformazioneRowReciver = mInformazioneRowReciver;
        this.mCardsVisibility = mCardsVisibility;
    }

    @NonNull
    @Override
    public QuestionarioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.questionario_card, viewGroup, false);
        return new QuestionarioViewHolder(mView, mOnSubmitClickListener, mInformazioneRowReciver);
    }


    @Override
    public void onBindViewHolder(@NonNull QuestionarioViewHolder holder, int position) {
        holder.mTitoloQuestionarioTextView.setText(mQuestionarioList.get(position).getTitolo());
        holder.mInfoListRecyclerView.setNestedScrollingEnabled(false);
        if(mCardsVisibility.size() > 0 && mCardsVisibility.get(position).getKey()){
            holder.mExpandableView.setVisibility(View.VISIBLE);
            holder.mExpandButton.setBackgroundResource(R.drawable.ic_expand_less);
        }

        LinearLayoutManager mInfoListLinearLayoutManager = new LinearLayoutManager(mContext);
        mInfoListLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        holder.mInfoListRecyclerView.setLayoutManager(mInfoListLinearLayoutManager);

        if(position == 0){
            holder.mSubmitButton.setEnabled(true);
            holder.mSubmitButton.setTextAppearance(mContext, R.style.EnableSubmit);
        }

        if(position > 0){
            if(mQuestionarioList.get(position - 1).isCompilato()){
                holder.mSubmitButton.setEnabled(true);
                holder.mSubmitButton.setTextAppearance(mContext, R.style.EnableSubmit);
            }
        }

        List<Informazione> mInformazioneList = mQuestionarioList.get(position).getInformazioni();
        if(mInformazioneList != null) {
            InformazioneRowAdapter informazioneRowAdapter = new InformazioneRowAdapter(mInformazioneList, holder);
            holder.mInfoListRecyclerView.setAdapter(informazioneRowAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return mQuestionarioList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    public static class QuestionarioViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, InformazioneRowAdapter.OnInformazioneRowListener{
        TextView mTitoloQuestionarioTextView;
        Button mExpandButton, mSubmitButton;
        CardView mCardView;
        ConstraintLayout mExpandableView;
        RecyclerView mInfoListRecyclerView;
        OnSubmitClickListener mOnSubmitClickListener;
        InformazioneRowReciver mInformazioneRowReciver;

        public QuestionarioViewHolder(View itemView, OnSubmitClickListener mOnSubmitClickListener,
                                      InformazioneRowReciver mInformazioneRowReciver){
            super(itemView);
            this.mOnSubmitClickListener = mOnSubmitClickListener;
            mCardView = (CardView) itemView.findViewById(R.id.questionarioCardView);
            mSubmitButton = (Button) itemView.findViewById(R.id.submitButton);
            mExpandableView = (ConstraintLayout) itemView.findViewById(R.id.expandableView);
            mTitoloQuestionarioTextView = (TextView) itemView.findViewById(R.id.titoloQuestionarioTextView);
            mExpandButton = (Button) itemView.findViewById(R.id.expandImageButton);
            mInfoListRecyclerView = (RecyclerView) itemView.findViewById(R.id.infoListRecyclerView);
            this.mInformazioneRowReciver = mInformazioneRowReciver;
            mExpandButton.setOnClickListener(this);
            mSubmitButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int mId = v.getId();
            switch (mId){
                case R.id.submitButton:
                    mOnSubmitClickListener.OnSubmitClick(getAdapterPosition());
                    break;
                case R.id.expandImageButton:
                    if (mExpandableView.getVisibility()==View.GONE){
                        TransitionManager.beginDelayedTransition(mCardView, new AutoTransition());
                        mExpandableView.setVisibility(View.VISIBLE);
                        mExpandButton.setBackgroundResource(R.drawable.ic_expand_less);

                    } else {
                        TransitionManager.beginDelayedTransition(mCardView, new AutoTransition());
                        mExpandableView.setVisibility(View.GONE);
                        mExpandButton.setBackgroundResource(R.drawable.ic_expand_more);
                    }
                    break;
                default:
                    break;
            }
        }

        /** Richiama il metodo OnClickRecive passando la posizione del questionario */
        @Override
        public void OnInfoRowClick(int position) {
            mInformazioneRowReciver.OnReciveClick(getAdapterPosition(), position);
        }
    }

    public interface OnSubmitClickListener{
        public void OnSubmitClick(int position);
    }

    public interface InformazioneRowReciver{
        public void OnReciveClick(int questionarioPosition, int infoPosition);
    }
}
