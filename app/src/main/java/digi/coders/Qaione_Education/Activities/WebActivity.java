package digi.coders.Qaione_Education.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import digi.coders.Qaione_Education.Helper.InternetCheck;
import digi.coders.Qaione_Education.R;

public class WebActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    String link="";
    TextView tittlen;
    ImageView back;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        link=getIntent().getStringExtra("link");

        webView = (WebView)findViewById(R.id.privacy_policy_webview);
        progressBar = findViewById(R.id.progressBar);
        tittlen = findViewById(R.id.tittlen);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tittlen.setText(getIntent().getStringExtra("tittle"));

        if(new InternetCheck().isNetworkAvailable(getApplicationContext())) {
            webView.setWebViewClient(new MyBrowser());
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.getSettings().setAllowContentAccess(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

            webView.getSettings().setAppCachePath(getApplicationContext().getApplicationContext().getCacheDir().getAbsolutePath());
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            webView.getSettings().setDatabaseEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String request) {
                    view.loadUrl(request);
                    progressBar.setVisibility(View.GONE);
                    return super.shouldOverrideUrlLoading(view, request);
                }
            });
            webView.loadUrl(link);
        }else{
            Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
        }
    }



    private class MyBrowser extends WebViewClient {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

//            boolean overrideUrlLoading = false;

            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
                return true;
            } else if (url.startsWith("mailto:")) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                startActivity(intent);
                return true;
            }/*else if (url != null && url.startsWith("whatsapp://")) {

                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                overrideUrlLoading = true;

            }*/
            else if (url.startsWith("whatsapp:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            } else {
                if (!new InternetCheck().isNetworkAvailable(getApplicationContext())) {

                    Toast toast = Toast.makeText(WebActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else {
                    view.loadUrl(url);
                }

                if (!progressBar.isAnimating()) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                // Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();
//                view.loadUrl(url);
            }
//            overrideUrlLoading;
            return true;
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressBar.isAnimating()) {
                progressBar.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onBackPressed() {

        if(webView.canGoBack()){
            progressBar.setVisibility(View.VISIBLE);
            webView.goBack();
        }
        else
        {
            finish();
        }
    }

}
