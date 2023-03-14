package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Ebook;

public class ReadPdfActivity extends AppCompatActivity {

    private WebView pdfWebview;
    private TextView ebookTitle;
    private String title,sample,status;
    private Button back;
    public  static Ebook ebook;
    private ProgressBar pdfProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_pdf);
        initView();
        status=getIntent().getStringExtra("status");

        if(status.equals("1"))
        {
            sample=getIntent().getStringExtra("sample");
            title=getIntent().getStringExtra("ebookname");

            ebookTitle.setText(title);
            pdfUrl = Constraints.BASE_IMAGE_URL + Constraints.EBOOK + sample;
            Log.e("readpdf",pdfUrl+"");

            //pdfUrl="http://www.ddegjust.ac.in/studymaterial/mca-3/ms-11.pdf";
            //URL u = null;

            pdfWebview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdfUrl);
            //pdfWebview.loadUrl("file:///android_asset/pdfviewer/index.html?file=" + path);
/*

            pdfWebview.setWebViewClient(new WebViewClient() {
                //once the page is loaded get the html element by class or id and through javascript hide it.

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    pdfProgress.setVisibility(View.GONE);
                    pdfWebview.setVisibility(View.VISIBLE);
                    pdfWebview.loadUrl("javascript:(function() { " +
                            "document.getElementsByClassName('ndfHFb-c4YZDc-GSQQnc-LgbsSe ndfHFb-c4YZDc-to915-LgbsSe VIpgJd-TzA9Ye-eEGnhe ndfHFb-c4YZDc-LgbsSe')[0].style.visibility='hidden'; })()");

                }
            });
*/
            pdfWebview.setWebViewClient(new WebViewClient());

            pdfWebview.getSettings().setSupportZoom(true);
            pdfWebview.getSettings().setBuiltInZoomControls(true);
            pdfWebview.getSettings().setJavaScriptEnabled(true);
            pdfWebview.setHorizontalScrollBarEnabled(true);
            pdfWebview.clearCache(true);
        }
        else
        {
            setPdfData();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pdfProgress.setVisibility(View.GONE);
            pdfWebview.setVisibility(View.VISIBLE);
            pdfWebview.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('ndfHFb-c4YZDc-GSQQnc-LgbsSe ndfHFb-c4YZDc-to915-LgbsSe VIpgJd-TzA9Ye-eEGnhe ndfHFb-c4YZDc-LgbsSe')[0].style.visibility='hidden'; })()");
        }
    }
    String pdfUrl;
    private void setPdfData() {
        ebookTitle.setText(ebook.getName());
        pdfUrl = Constraints.BASE_IMAGE_URL + Constraints.EBOOK + ebook.getEbook();

        //pdfUrl="http://www.ddegjust.ac.in/studymaterial/mca-3/ms-11.pdf";
        //URL u = null;

        pdfWebview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdfUrl);
        //pdfWebview.loadUrl("file:///android_asset/pdfviewer/index.html?file=" + path);
        pdfWebview.setWebViewClient(new WebViewClient() {
            //once the page is loaded get the html element by class or id and through javascript hide it.

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pdfProgress.setVisibility(View.GONE);
                pdfWebview.setVisibility(View.VISIBLE);
                pdfWebview.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('ndfHFb-c4YZDc-GSQQnc-LgbsSe ndfHFb-c4YZDc-to915-LgbsSe VIpgJd-TzA9Ye-eEGnhe ndfHFb-c4YZDc-LgbsSe')[0].style.visibility='hidden'; })()");

            }
        });
        pdfWebview.getSettings().setSupportZoom(true);
        pdfWebview.getSettings().setBuiltInZoomControls(true);
        pdfWebview.getSettings().setJavaScriptEnabled(true);

        pdfWebview.setHorizontalScrollBarEnabled(true);
        pdfWebview.clearCache(true);
    }

    private void initView() {
        pdfWebview=findViewById(R.id.pdfWebview);
        ebookTitle=findViewById(R.id.toolbar_tex);
        back=findViewById(R.id.back_but);
        pdfProgress=findViewById(R.id.pdf_progress);

    }
}