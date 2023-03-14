package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.databinding.ActivityQuizScoreBinding;
import digi.coders.Qaione_Education.models.QuizLData;

public class QuizScoreActivity extends AppCompatActivity {

    ActivityQuizScoreBinding binding;
    public static QuizLData quizLData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_score);

        binding= ActivityQuizScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.score.setText(quizLData.getScore());
        binding.rightAns.setText("Correct ("+quizLData.getRight()+")");
        binding.wrongAns.setText("Wrong ("+quizLData.getWrong()+")");
        binding.bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuizScoreActivity.this,HomeActivity.class));
                finish();
            }
        });
        binding.share.setOnClickListener(new View.OnClickListener() {
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


    }
}