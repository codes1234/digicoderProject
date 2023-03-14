package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import digi.coders.Qaione_Education.Adapters.QuestionAdapter;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.databinding.ActivityCompletedQuizHistoryBinding;
import digi.coders.Qaione_Education.databinding.QuestionDesignBinding;
import digi.coders.Qaione_Education.models.AnsBook;
import digi.coders.Qaione_Education.models.QuestionData;
import digi.coders.Qaione_Education.models.QuizListData;
import digi.coders.Qaione_Education.models.Score;

public class CompletedQuizHistoryActivity extends AppCompatActivity {

    ActivityCompletedQuizHistoryBinding binding;
    public static Score score;
    //public  static ExamListData data1;
    public static QuizListData data;
    private QuestionDesignBinding queBinding;
    AnsBook[] ansBook=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_quiz_history);
        binding=ActivityCompletedQuizHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.tittle.setText(data.getQuiz().getName());

//        if(data1==null) {
//            binding.tittle.setText(data.getQuiz().getName());
//
//        }
//        else
//        {
//            binding.tittle.setText(data1.getExam().getName());
//
//        }
        ansBook=score.getAnsBook();
        if(ansBook.length==0)
        {

            binding.progressBar.setVisibility(View.GONE);
            binding.notxt.setVisibility(View.VISIBLE);

        }
        else
        {
            loadQuizesWithAns(ansBook);
        }

    }

    private void loadQuizesWithAns(AnsBook[] ansBook) {
        binding.progressBar.setVisibility(View.GONE);
        binding.quizList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        QuestionAdapter adapter=new QuestionAdapter(ansBook,1);
        binding.quizList.setAdapter(adapter);
        adapter.getSelectedOption(new QuestionAdapter.GetOption() {
            @Override
            public void click(QuestionDesignBinding binding, int position) {
                queBinding=binding;

                AnsBook ansBook1=ansBook[position];
                QuestionData questionData=ansBook1.getQuestionData();

            }
        });

    }
}