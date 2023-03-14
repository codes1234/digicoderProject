package digi.coders.Qaione_Education.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;

import com.skydoves.elasticviews.ElasticLayout;

import digi.coders.Qaione_Education.Activities.WebActivity;
import digi.coders.Qaione_Education.R;

public class SettingFragment extends Fragment {

    ElasticLayout faq,terms,privacy,help,twiter,blog,facebook,insta,youtube,share,rateUs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_settings, container, false);

        youtube= view.findViewById(R.id.youtube);
        insta= view.findViewById(R.id.insta);
        facebook= view.findViewById(R.id.facebook);
        twiter= view.findViewById(R.id.twiter);
        privacy= view.findViewById(R.id.privacy);
        rateUs= view.findViewById(R.id.rateus);
        help= view.findViewById(R.id.help);
        terms= view.findViewById(R.id.terms);
        faq= view.findViewById(R.id.faq);
        share= view.findViewById(R.id.share);
        blog= view.findViewById(R.id.blog);


        rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=digi.coders.Qaione_Education"));
                startActivity(intent);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT,"Join Qaione Education Platform "+
                        "\n"+"Download app now:"+"\n"+
                        "https://play.google.com/store/apps/details?id=digi.coders.Qaione_Education");
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(intent,"Share via"));

            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"));
                startActivity(intent);
                //openChromeCustomeTab("https://www.youtube.com/c/CodersAdda");
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
                startActivity(intent);
//                openChromeCustomeTab("https://www.facebook.com/CodersAddaDCT/");
            }
        });

        twiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/codersaddadct/"));

                startActivity(intent);*/
                openChromeCustomeTab("https://twitter.com/");
            }
        });

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/codersaddadct/"));
                startActivity(intent);*/
                openChromeCustomeTab("https://www.instagram.com//");
            }
        });


        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), WebActivity.class);
                intent.putExtra("link","http://qaione.digitalcoders.in/Home/index");
                intent.putExtra("tittle","Blog");
                startActivity(intent);
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), WebActivity.class);
                intent.putExtra("link","http://qaione.digitalcoders.in/Home/privacypolicy");
                intent.putExtra("tittle","Privacy Policy");
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), WebActivity.class);
                intent.putExtra("link","http://qaione.digitalcoders.in/Home/contact");
                intent.putExtra("tittle","Help & Support");
                startActivity(intent);
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), WebActivity.class);
                intent.putExtra("link","http://qaione.digitalcoders.in/Home/termscondition");
                intent.putExtra("tittle","Terms & Conditions");
                startActivity(intent);
            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), WebActivity.class);
                intent.putExtra("link","http://qaione.digitalcoders.in/");
                intent.putExtra("tittle","FAQs");
                startActivity(intent);
            }
        });

        return view;
    }

    public void openChromeCustomeTab(String uri){
        CustomTabsIntent.Builder builder=new CustomTabsIntent.Builder();
        CustomTabsIntent intent=builder.build();
        intent.launchUrl( getActivity(), Uri.parse( uri ) );
    }

}
