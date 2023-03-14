package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;

public class ReferActivity extends AppCompatActivity {

    private SingleTask singleTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);

        singleTask = (SingleTask) getApplication();
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);

        TextView ref_code=findViewById(R.id.ref_code);
        Button refer_now=findViewById(R.id.refer_now);

        ref_code.setText(user.getRefcode());

        refer_now.setOnClickListener(
                v -> {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, "Your Referral Code is "+ref_code.getText().toString()+". Download App now https://play.google.com/store/apps/details?id=digi.coders.Qaione_Education");
                    startActivity(Intent.createChooser(share, "Share using"));
                }
        );
    }

    public void back(View view) {
       startActivity(new Intent(this,HomeActivity.class));
       finish();
    }
}