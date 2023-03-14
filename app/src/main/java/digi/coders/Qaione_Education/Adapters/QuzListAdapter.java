package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import digi.coders.Qaione_Education.Activities.PlayQuizActivity;
import digi.coders.Qaione_Education.Activities.QuizDetailsActivity;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.databinding.ItemMocktestBinding;
import digi.coders.Qaione_Education.models.Quiz;
import digi.coders.Qaione_Education.models.QuizListData;

public class QuzListAdapter extends RecyclerView.Adapter<QuzListAdapter.MyHolder> {

    private Context ctx;
    private int status;
    QuizListData[] quizListData;

    public QuzListAdapter(int status, QuizListData[] quizListData) {
        this.status = status;
        this.quizListData = quizListData;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mocktest,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MyHolder holder, int position) {

        QuizListData quizListData = this.quizListData[position];
        Quiz quiz= quizListData.getQuiz();

        holder.binding.quizName.setText(quiz.getName());
        holder.binding.quizDesc.setText(quiz.getDescription());
        holder.binding.noOfQues.setText(quiz.getNoOfQuestions()+" Qus | "+quiz.getPerQuestionNo()+" mark per");
        holder.binding.perQuestionMarks.setText("Per Question Marks- "+quiz.getPerQuestionNo());
        //holder.binding.openDate.setText(quizListData.getTiming().split(" ")[0]);
        holder.binding.startTime.setText(quiz.getTiming()+" mins");
        holder.binding.openTiming.setText("Start on- "+quizListData.getTiming().split(" ")[0]+" | "+quizListData.getTiming().split(" ")[1]);

        if(status==1)
        {
            holder.binding.startQuiz.setText("Start Quiz");
        }
        else if(status==2)
        {
            holder.binding.startQuiz.setText("Upcoming Quiz");
            holder.binding.rightAnsLayout.setVisibility(View.GONE);
            holder.binding.errorAnsLayout.setVisibility(View.GONE);
            holder.binding.score.setVisibility(View.GONE);

        }
        else {

            holder.binding.startQuiz.setText("View Detail");
//            holder.binding.startQuiz.setVisibility(View.GONE);
            holder.binding.openTiming.setVisibility(View.GONE);
            //holder.binding.openDateLayout.setVisibility(View.GONE);
            holder.binding.rightAnsLayout.setVisibility(View.GONE);
            holder.binding.errorAnsLayout.setVisibility(View.GONE);
            holder.binding.rightAns.setText(quizListData.getResultList().getRight());
            holder.binding.errorAns.setText(quizListData.getResultList().getWrong());
            holder.binding.score.setText("Your Score- "+quizListData.getResultList().getScore());
        }

        holder.binding.startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(status==1)
                {
                    Intent intent = new Intent(ctx, PlayQuizActivity.class);
                    intent.putExtra("id", quizListData.getId());
                    ctx.startActivity(intent);
                }
                else if(status==2)
                {
                    Toast.makeText(ctx, "Quiz Coming soon.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    QuizDetailsActivity.score=quizListData.getResultList();
                    QuizDetailsActivity.data=quizListData;
                    ctx.startActivity(new Intent(ctx, QuizDetailsActivity.class));

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return quizListData.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        ItemMocktestBinding binding;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            binding=ItemMocktestBinding.bind(itemView);
        }
    }
}

