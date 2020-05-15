package com.example.bicap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import java.util.ArrayList;
import java.util.List;

public class QuestionarioAdapter extends RecyclerView.Adapter<QuestionarioAdapter.QuestionarioViewHolder> {

    private List<Questionario> questionarioList;
    private List<ParcelableBoolean> cardsVisibility;
    private Context context;
    private InformazioneRowAdapter.OnInformazioneRowListener onInformazioneRowListener;
    private InformazioneRowReciver informazioneRowReciver;
    private OnSubmitClickListener onSubmitClickListener;

    QuestionarioAdapter(List<Questionario> questionarioList, Context context,
                        OnSubmitClickListener onSubmitClickListener,
                        InformazioneRowReciver informazioneRowReciver,
                        List<ParcelableBoolean> cardsVisibility){
        this.questionarioList = questionarioList;
        this.context = context;
        this.onSubmitClickListener = onSubmitClickListener;
        this.informazioneRowReciver = informazioneRowReciver;
        this.cardsVisibility = cardsVisibility;
    }

    @NonNull
    @Override
    public QuestionarioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.questionario_card, viewGroup, false);
        QuestionarioViewHolder ivh = new QuestionarioViewHolder(v, onSubmitClickListener, informazioneRowReciver);
        return ivh;
    }


    @Override
    public void onBindViewHolder(@NonNull QuestionarioViewHolder holder, int position) {
        holder.titoloQuestionarioTextView.setText(questionarioList.get(position).getTitolo());
        holder.infoListRecyclerView.setNestedScrollingEnabled(false);
        if(cardsVisibility.size() > 0 && cardsVisibility.get(position).getKey()){
            holder.expandableView.setVisibility(View.VISIBLE);
            holder.expandButton.setBackgroundResource(R.drawable.ic_expand_less);
        }

        LinearLayoutManager llmInfoListLayoutManager = new LinearLayoutManager(context);
        llmInfoListLayoutManager.setOrientation(RecyclerView.VERTICAL);
        holder.infoListRecyclerView.setLayoutManager(llmInfoListLayoutManager);

        if(position == 0){
            holder.submitButton.setEnabled(true);
            holder.submitButton.setTextAppearance(context, R.style.EnableSubmit);
            //holder.submitButton.setTextColor(Color.parseColor("#A3214A")); //Brutto ma funzionante
        }

        if(position > 0){
            if(questionarioList.get(position - 1).isCompilato()){
                holder.submitButton.setEnabled(true);
            }
        }

        List<Informazione> informazioneList = questionarioList.get(position).getInformazioni();
        if(informazioneList != null) {
            InformazioneRowAdapter informazioneRowAdapter = new InformazioneRowAdapter(informazioneList, holder);
            holder.infoListRecyclerView.setAdapter(informazioneRowAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return questionarioList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    public static class QuestionarioViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, InformazioneRowAdapter.OnInformazioneRowListener{
        TextView titoloQuestionarioTextView;
        Button expandButton, submitButton;
        CardView cardView;
        ConstraintLayout expandableView;
        RecyclerView infoListRecyclerView;
        OnSubmitClickListener onSubmitClickListener;
        InformazioneRowReciver informazioneRowReciver;

        public QuestionarioViewHolder(View itemView, OnSubmitClickListener onSubmitClickListener,
                                      InformazioneRowReciver informazioneRowReciver){
            super(itemView);
            this.onSubmitClickListener = onSubmitClickListener;
            cardView = (CardView) itemView.findViewById(R.id.questionarioCardView);
            submitButton = (Button) itemView.findViewById(R.id.submitButton);
            expandableView = (ConstraintLayout) itemView.findViewById(R.id.expandableView);
            titoloQuestionarioTextView = (TextView) itemView.findViewById(R.id.titoloQuestionarioTextView);
            expandButton = (Button) itemView.findViewById(R.id.expandImageButton);
            infoListRecyclerView = (RecyclerView) itemView.findViewById(R.id.infoListRecyclerView);
            this.informazioneRowReciver = informazioneRowReciver;
            expandButton.setOnClickListener(this);
            submitButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.submitButton:
                    onSubmitClickListener.OnSubmitClick(getAdapterPosition());
                    break;
                case R.id.expandImageButton:
                    if (expandableView.getVisibility()==View.GONE){
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.VISIBLE);
                        expandButton.setBackgroundResource(R.drawable.ic_expand_less);

                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.GONE);
                        expandButton.setBackgroundResource(R.drawable.ic_expand_more);
                    }
                    break;
                default:
                    break;
            }

        }

        @Override
        public void OnInfoRowClick(int position) {
            //Richiama il metodo OnClickRecive passando la
            //posizione del questionario
            informazioneRowReciver.OnReciveClick(getAdapterPosition(), position);
        }
    }

    public interface OnSubmitClickListener{
        public void OnSubmitClick(int position);
    }

    public interface InformazioneRowReciver{
        public void OnReciveClick(int questionarioPosition, int infoPosition);
    }


}
