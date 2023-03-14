package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import digi.coders.Qaione_Education.Adapters.MyWishlistTabAdapter;
import digi.coders.Qaione_Education.R;

public class MyWishlistActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private TextView title;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wishlist);

        tabLayout=findViewById(R.id.tablayoute);
        title=findViewById(R.id.toolbar_texe);

        key=getIntent().getStringExtra("sendkey");
//        if(key.equals("1"))
//        {
//            title.setText("Wishlist");
//        }
//        if(key.equals("2"))
//        {
//            title.setText("My Ebooks");
//        }
        tabLayout.addTab(tabLayout.newTab().setText("Course"));
       /* tabLayout.addTab(tabLayout.newTab().setText("Audio Book"));*/
        tabLayout.addTab(tabLayout.newTab().setText("PDF Book"));
        ViewPager  viewPager=findViewById(R.id.viewpagr);
        MyWishlistTabAdapter myWishlistTabAdapter =new MyWishlistTabAdapter(this,getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(myWishlistTabAdapter);
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