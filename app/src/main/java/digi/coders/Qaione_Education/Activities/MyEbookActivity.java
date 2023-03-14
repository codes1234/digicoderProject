package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import digi.coders.Qaione_Education.Adapters.MyEbookTabAdapter;
import digi.coders.Qaione_Education.R;

public class MyEbookActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private TextView title;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ebook);

        tabLayout=findViewById(R.id.tablayoute);
        title=findViewById(R.id.toolbar_texe);

        key=getIntent().getStringExtra("sendkey");

        /*tabLayout.addTab(tabLayout.newTab().setText("Audio Book"));*/
        tabLayout.addTab(tabLayout.newTab().setText("PDF Book"));
        ViewPager viewPager=findViewById(R.id.viewpagr);
        MyEbookTabAdapter myEbookTabAdapter =new MyEbookTabAdapter(this,getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(myEbookTabAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void close(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}