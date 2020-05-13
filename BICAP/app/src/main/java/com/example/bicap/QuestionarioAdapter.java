package com.example.bicap;

import android.content.Context;
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

public class QuestionarioAdapter extends RecyclerView.Adapter<QuestionarioAdapter.QuestionarioViewHolder>{

    //Il custom listener non serve più, l'evento di click viene gestito dal ViewHolder
    private List<Questionario> questionarioList;
    private Context context;
    private InformazioneRowAdapter.OnInformazioneRowListener onInformazioneRowListener;

    QuestionarioAdapter(List<Questionario> questionarioList, Context context, InformazioneRowAdapter.OnInformazioneRowListener onInformazioneRowListener){
        this.questionarioList = questionarioList;
        this.context = context;
        this.onInformazioneRowListener = onInformazioneRowListener;
    }

    @NonNull
    @Override
    public QuestionarioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.questionario_card, viewGroup, false);
        QuestionarioViewHolder ivh = new QuestionarioViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionarioViewHolder holder, int position) {
        holder.titoloQuestionarioTextView.setText(questionarioList.get(position).getTitolo());
        holder.infoListRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager llmInfoListLayoutManager = new LinearLayoutManager(context);
        llmInfoListLayoutManager.setOrientation(RecyclerView.VERTICAL);
        holder.infoListRecyclerView.setLayoutManager(llmInfoListLayoutManager);

        List<Informazione> informazioneList = questionarioList.get(position).getInformazioni();
        if(informazioneList != null) {
        InformazioneRowAdapter informazioneRowAdapter = new InformazioneRowAdapter(informazioneList, onInformazioneRowListener);
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

    public static class QuestionarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titoloQuestionarioTextView;
        Button expandButton;
        CardView cardView;
        ConstraintLayout expandableView;
        RecyclerView infoListRecyclerView;

        public QuestionarioViewHolder(View itemView){
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.questionarioCardView);
            expandableView = (ConstraintLayout) itemView.findViewById(R.id.expandableView);
            titoloQuestionarioTextView = (TextView) itemView.findViewById(R.id.titoloQuestionarioTextView);
            expandButton = (Button) itemView.findViewById(R.id.expandImageButton);
            infoListRecyclerView = (RecyclerView) itemView.findViewById(R.id.infoListRecyclerView);
            expandButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (expandableView.getVisibility()==View.GONE){
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableView.setVisibility(View.VISIBLE);
                expandButton.setBackgroundResource(R.drawable.ic_expand_less);

            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableView.setVisibility(View.GONE);
                expandButton.setBackgroundResource(R.drawable.ic_expand_more);

            }
        }
    }

    public interface OnQuestionarioCardListener{
        void onQuestionarioCardClick(int position);
    }

    //Provvisiorio
/*    private List<Informazione> getInformazioniList() {
        List<Informazione> lista = new ArrayList<>();

        lista.add(new Informazione("Informazione.pdf", "url_file", "application/pdf", "https://mangadex.org/images/avatars/default2.jpg"));
        lista.add(new Informazione("Informazione.mp3", "url_file", "audio/mp3", "https://mangadex.org/images/avatars/default2.jpg"));
        lista.add(new Informazione("Informazione.txt", "url_file", "text/plain", "https://mangadex.org/images/avatars/default2.jpg"));
        lista.add(new Informazione("Informazione.avi", "url_file", "video/mp3", "https://mangadex.org/images/avatars/default2.jpg"));
        lista.add(new Informazione("Informazione.pdf", "url_file", "application/pdf", "https://mangadex.org/images/avatars/default2.jpg"));
        lista.add(new Informazione("Informazione.text", "url_file", "text/plain", "https://mangadex.org/images/avatars/default2.jpg"));

        return lista;
    }*/
}
