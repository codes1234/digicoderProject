package digi.coders.Qaione_Education;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.style.IconMarginSpan;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class EducatorloginActivity extends AppCompatActivity {

    WebView webViewEducator;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educatorlogin);

        webViewEducator=findViewById(R.id.webViewEducator);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        webViewEducator.setWebViewClient(new WebViewClient());
        webViewEducator.getSettings().setJavaScriptEnabled(true);
        webViewEducator.loadUrl("https://qaioneeducation.com/Home/EducatorLogin");
    }
}