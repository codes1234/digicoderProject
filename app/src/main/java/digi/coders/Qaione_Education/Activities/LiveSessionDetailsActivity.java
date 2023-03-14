package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.LiveSession;

public class LiveSessionDetailsActivity extends AppCompatActivity {

    private Button back;
    private ImageView thumbnil,copyUserId,copyPassoword,copyLink;
    public static LiveSession liveSession;
    private TextView tag,title,time,desc,authorName,userId,pass,link;
    private CircleImageView authorImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_session_joined);
        initView();
        setData();
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        copyUserId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipData clip = ClipData.newPlainText("simple text",userId.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(LiveSessionDetailsActivity.this, "User id copied successfully", Toast.LENGTH_SHORT).show();

            }
        });
        copyPassoword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText("simple text",pass.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(LiveSessionDetailsActivity.this, "Password copied successfully", Toast.LENGTH_SHORT).show();
            }
        });
        copyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText("simple text",link.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(LiveSessionDetailsActivity.this, "Link copied successfully", Toast.LENGTH_SHORT).show();
            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChromeCustomeTab(link.getText()+"");
            }
        });
    }
    public void openChromeCustomeTab(String uri){
        CustomTabsIntent.Builder builder=new CustomTabsIntent.Builder();
        CustomTabsIntent intent=builder.build();
        intent.launchUrl( this, Uri.parse( uri ) );
    }

    private void initView() {
        back=findViewById(R.id.back);
        thumbnil=findViewById(R.id.live_thumbnil);
        tag=findViewById(R.id.live_tags);
        title=findViewById(R.id.live_title);
        desc=findViewById(R.id.live_desc);
        time=findViewById(R.id.live_time);
        authorImage=findViewById(R.id.live_author_image);
        authorName=findViewById(R.id.live_author_name);
        userId=findViewById(R.id.use);
        pass=findViewById(R.id.password);
        link=findViewById(R.id.link);
        copyUserId=findViewById(R.id.copy_user_id);
        copyPassoword=findViewById(R.id.copy_password);
        copyLink=findViewById(R.id.copy_link);
    }

    private void setData() {
        tag.setText(liveSession.getTags());
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.LIVE_VIDEO+liveSession.getThumbnail()).placeholder(shimmerDrawable).into(thumbnil);
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.TUTOR+liveSession.getAuthor().getPhoto()).placeholder(shimmerDrawable).into(authorImage);
        time.setText("Started at "+liveSession.getTiming()+" , "+liveSession.getDuration()+" mins");
        title.setText(liveSession.getTitle());
        authorName.setText(liveSession.getAuthor().getName());
        userId.setText(liveSession.getUserid());
        pass.setText(liveSession.getPassword());
        link.setText(liveSession.getLink());

    }
}