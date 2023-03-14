package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import digi.coders.Qaione_Education.Adapters.QuestionAnalysisAdapter;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.databinding.ActivityQuizDetailsBinding;
import digi.coders.Qaione_Education.databinding.QuestionDesignBinding;
import digi.coders.Qaione_Education.models.AnsBook;
import digi.coders.Qaione_Education.models.QuestionData;
import digi.coders.Qaione_Education.models.QuizListData;
import digi.coders.Qaione_Education.models.Score;

public class QuizDetailsActivity extends AppCompatActivity {

    public static Score score;
    public static QuizListData data;
    AnsBook[] ansBook=null;
    RecyclerView quizList;

    ActivityQuizDetailsBinding binding;
    private QuestionDesignBinding queBinding;

    TextView Score, totalScore, rank, totalRank, right_ans, wrongAns, share, reattempt;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityQuizDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Score=findViewById(R.id.score);
        totalScore=findViewById(R.id.totalScore);
        rank=findViewById(R.id.rank);
        totalRank=findViewById(R.id.totalRank);
        right_ans=findViewById(R.id.right_ans);
        wrongAns=findViewById(R.id.wrongAns);
        share=findViewById(R.id.share);
        reattempt=findViewById(R.id.reattempt);
        back=findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Score.setText(score.getScore());

        totalScore.setText("Out of "+Integer.parseInt(score.getScore())*Integer.parseInt(data.getQuiz().getNoOfQuestions()));
        rank.setText(score.getRank());
        totalRank.setText("Out of "+score.getOut_of_rank());
        right_ans.setText("Correct ("+score.getRight()+")");
        wrongAns.setText("Wrong ("+score.getWrong()+")");

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "Join Qaione Education Platform "+
                        "\n"+"Download app now:"+"\n"+
                        "https://play.google.com/store/apps/details?id=digi.coders.Qaione_Education");
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(intent,"Share via"));

            }
        });

        reattempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayQuizActivity.class);
                intent.putExtra("id", score.getId());
                startActivity(intent);
            }
        });

        ansBook=score.getAnsBook();
        if(ansBook.length==0)
        {

        } else
        {
            loadQuizesWithAns(ansBook);
        }

    }

    private void loadQuizesWithAns(AnsBook[] ansBook) {
        binding.quizListRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        QuestionAnalysisAdapter adapter=new QuestionAnalysisAdapter(ansBook,1);
        binding.quizListRecycler.setAdapter(adapter);
        adapter.getSelectedOption(new QuestionAnalysisAdapter.GetOption() {
            @Override
            public void click(QuestionDesignBinding binding, int position) {
                queBinding=binding;

                AnsBook ansBook1=ansBook[position];
                QuestionData questionData=ansBook1.getQuestionData();

            }
        });

    }
}