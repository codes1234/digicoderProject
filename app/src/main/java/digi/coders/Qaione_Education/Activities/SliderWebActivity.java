package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import digi.coders.Qaione_Education.R;

public class SliderWebActivity extends AppCompatActivity {

    private WebView webview;
    private ProgressBar progressBar;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_web);
        initView();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled( true );
        webview.loadUrl("http://qaione.digitalcoders.in/");
        webview.setWebChromeClient( new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress( newProgress );
                if (newProgress==100){
                    progressBar.setVisibility( View.GONE );
                }
                super.onProgressChanged( view, newProgress );
            }
        });

    }


    private void initView() {
        webview=findViewById(R.id.webview);
        progressBar=findViewById(R.id.progressBa);
        back=findViewById(R.id.back1);
    }
}