package it.unimib.bicap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import it.unimib.bicap.R;

import java.util.List;

import it.unimib.bicap.model.Informazione;
import it.unimib.bicap.model.Questionario;

public class QuestionarioAdapter extends RecyclerView.Adapter<QuestionarioAdapter.QuestionarioViewHolder> {

    private List<Questionario> mQuestionarioList;
    private List<Boolean> mCardsVisibility;
    private Context mContext;
    private InformazioneRowReciver mInformazioneRowReciver;
    private OnSubmitClickListener mOnSubmitClickListener;
    private VisibilityListener mVisibilityListener;

    public QuestionarioAdapter(List<Questionario> mQuestionarioList, Context mContext,
                               OnSubmitClickListener mOnSubmitClickListener,
                               InformazioneRowReciver mInformazioneRowReciver,
                               List<Boolean> mCardsVisibility,
                               VisibilityListener mVisibilityListener){
        this.mQuestionarioList = mQuestionarioList;
        this.mContext = mContext;
        this.mOnSubmitClickListener = mOnSubmitClickListener;
        this.mInformazioneRowReciver = mInformazioneRowReciver;
        this.mCardsVisibility = mCardsVisibility;
        this.mVisibilityListener = mVisibilityListener;
    }

    @NonNull
    @Override
    public QuestionarioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.questionario_card, viewGroup, false);
        return new QuestionarioViewHolder(mView, mOnSubmitClickListener,
                mInformazioneRowReciver, mVisibilityListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionarioViewHolder holder, int position) {
        holder.mTitoloQuestionarioTextView.setText(mQuestionarioList.get(position).getTitolo());
        holder.mInfoListRecyclerView.setNestedScrollingEnabled(false);
        if(mCardsVisibility.size() > 0 && mCardsVisibility.get(position)){
            holder.mExpandableView.setVisibility(View.VISIBLE);
            holder.mExpandButton.setBackgroundResource(R.drawable.ic_expand_less);
        }

        LinearLayoutManager mInfoListLinearLayoutManager = new LinearLayoutManager(mContext);
        mInfoListLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        holder.mInfoListRecyclerView.setLayoutManager(mInfoListLinearLayoutManager);

        // Formattazione del primo bottone submit
        if(position == 0){
            if(mQuestionarioList.get(0).isCompilato()){
                holder.mSubmitButton.setVisibility(View.GONE);
                holder.mCompilatoTextView.setVisibility(View.VISIBLE);
                holder.mCompilatoImageView.setVisibility(View.VISIBLE);
            }else{
                holder.mSubmitButton.setEnabled(true);
                holder.mSubmitButton.setTextAppearance(mContext, R.style.EnableSubmit);
            }
        }

        // Formattazione di tutti i bottoni submit
        if(position > 0){
            if(mQuestionarioList.get(position).isCompilato()){
                holder.mSubmitButton.setVisibility(View.GONE);
                holder.mCompilatoTextView.setVisibility(View.VISIBLE);
                holder.mCompilatoImageView.setVisibility(View.VISIBLE);
            }else{
                if(mQuestionarioList.get(position - 1).isCompilato()){
                    holder.mSubmitButton.setEnabled(true);
                    holder.mSubmitButton.setTextAppearance(mContext, R.style.EnableSubmit);
                }
            }
        }
        // Caricamento della recyclerView delle informazioni inerenti al questionario
        List<Informazione> mInformazioneList = mQuestionarioList.get(position).getInformazioni();
        if(mInformazioneList != null) {
            // holder sarà il click Listener dell'informazione
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
        TextView mTitoloQuestionarioTextView, mCompilatoTextView;
        Button mExpandButton, mSubmitButton;
        CardView mCardView;
        ConstraintLayout mExpandableView;
        RecyclerView mInfoListRecyclerView;
        OnSubmitClickListener mOnSubmitClickListener;
        InformazioneRowReciver mInformazioneRowReciver;
        ImageView mCompilatoImageView;
        VisibilityListener mVisibilityListener;

        public QuestionarioViewHolder(View itemView, OnSubmitClickListener mOnSubmitClickListener,
                                      InformazioneRowReciver mInformazioneRowReciver,
                                      VisibilityListener mVisibilityListener){
            super(itemView);
            this.mOnSubmitClickListener = mOnSubmitClickListener;
            mCardView = (CardView) itemView.findViewById(R.id.questionarioCardView);
            mSubmitButton = (Button) itemView.findViewById(R.id.submitButton);
            mExpandableView = (ConstraintLayout) itemView.findViewById(R.id.expandableView);
            mTitoloQuestionarioTextView = (TextView) itemView.findViewById(R.id.titoloQuestionarioTextView);
            mExpandButton = (Button) itemView.findViewById(R.id.expandImageButton);
            mInfoListRecyclerView = (RecyclerView) itemView.findViewById(R.id.infoListRecyclerView);
            mCompilatoImageView = (ImageView) itemView.findViewById(R.id.compilatoImageView);
            mCompilatoTextView = (TextView) itemView.findViewById(R.id.compilatoTextView);
            this.mInformazioneRowReciver = mInformazioneRowReciver;
            this.mVisibilityListener = mVisibilityListener;
            mExpandButton.setOnClickListener(this);
            mSubmitButton.setOnClickListener(this);
        }

        /**
         * Un solo listener per il bottone dell'espansione della card e per il bottone submit;
         * filtraggio attraverso lo switch.
        */
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
                        mVisibilityListener.OnExpandClick(getAdapterPosition(), true);
                    } else {
                        mExpandableView.setVisibility(View.GONE);
                        mExpandButton.setBackgroundResource(R.drawable.ic_expand_more);
                        mVisibilityListener.OnExpandClick(getAdapterPosition(), false);
                    }
                    break;
                default:
                    break;
            }
        }

        /** Richiama il metodo OnClickRecive passando la posizione del questionario */
        @Override
        public void OnInfoRowClick(int position) {
            /**
             * getAdapterPosition() per la posizione del questionario, position per la posizione
             * dell'informazione
             */
            mInformazioneRowReciver.OnReciveClick(getAdapterPosition(), position);
        }
    }

    public interface OnSubmitClickListener{
        void OnSubmitClick(int position);
    }

    /** Riceve sia la posizione del questionario che dell'informazione */
    public interface InformazioneRowReciver{
        void OnReciveClick(int questionarioPosition, int infoPosition);
    }

    /**
     * Ci si occupa del mantenimento dello stato della lista di visibilità delle cards;
     * nel nostro caso sarà il vewmodel
     */
    public interface VisibilityListener{
        void OnExpandClick(int position, Boolean visibility);
    }
}
