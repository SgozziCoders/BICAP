package com.example.bicap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import java.util.List;

public class QuestionarioAdapter extends RecyclerView.Adapter<QuestionarioAdapter.QuestionarioViewHolder>{

    private OnQuestionarioCardListener onQuestionarioCardListener;
    private List<Questionario> questionarioList;

    QuestionarioAdapter(List<Questionario> questionarioList, OnQuestionarioCardListener onQuestionarioCardListener){
        this.questionarioList = questionarioList;
        this.onQuestionarioCardListener = onQuestionarioCardListener;
    }

    @NonNull
    @Override
    public QuestionarioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.questionario_card, viewGroup, false);
        QuestionarioViewHolder ivh = new QuestionarioViewHolder(v, onQuestionarioCardListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionarioViewHolder holder, int position) {
        holder.titoloQuestionarioTextView.setText(questionarioList.get(position).getTitolo());
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
        OnQuestionarioCardListener onQuestionarioCardListener;

        public QuestionarioViewHolder(View itemView, OnQuestionarioCardListener onQuestionarioCardListener){
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.questionarioCardView);
            expandableView = (ConstraintLayout) itemView.findViewById(R.id.expandableView);
            titoloQuestionarioTextView = (TextView) itemView.findViewById(R.id.titoloQuestionarioTextView);
            expandButton = (Button) itemView.findViewById(R.id.expandImageButton);
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
}
