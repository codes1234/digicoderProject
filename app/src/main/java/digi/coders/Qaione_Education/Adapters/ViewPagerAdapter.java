package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.Qaione_Education.Activities.CategoryCoursesActivity;
import digi.coders.Qaione_Education.Activities.CourseDetailsActivity;
import digi.coders.Qaione_Education.Activities.EbookDetailActivity;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Slider;

public class ViewPagerAdapter extends PagerAdapter {

    private List<Slider> sliderList;
    Context ctx;

    public ViewPagerAdapter(List<Slider> sliderList, Context ctx) {
        this.sliderList = sliderList;
        this.ctx = ctx;
    }

    private LayoutInflater layoutInflater;
    @Override
    public int getCount() {
        return sliderList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Slider slider=sliderList.get(position);
        View view = layoutInflater.inflate(R.layout.items_view_pager, container, false);
        ImageView img=view.findViewById(R.id.card_logo);

        TextView description=view.findViewById(R.id.desc);
        TextView watchLive=view.findViewById(R.id.watchLive);
        description.setText(slider.getTitle());
        watchLive.setText(slider.getButton());
        /*Log.e("s",photos.get(position));*/
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.SLIDER+slider.getImage()).placeholder(R.drawable.logo).into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* ctx.startActivity(new Intent(ctx, SliderWebActivity.class));*/
                if(slider.getParameter().equals("External"))
                {
                    openChromeCustomeTab( slider.getLink() );
                }
                else if(slider.getParameter().equals("Category"))
                {
                    Intent in=new Intent(ctx, CategoryCoursesActivity.class);
                    in.putExtra("id",slider.getLink());
                    in.putExtra("title",slider.getTitle());
                    Log.i("ghjk", slider.getTitle());
                    ctx.startActivity(in);
                }
                else if(slider.getParameter().equals("Course"))
                {
                    Intent in = new Intent(ctx, CourseDetailsActivity.class);
                    in.putExtra("courseid", slider.getLink());
                    ctx.startActivity(in);
                }
                else if(slider.getParameter().equals("Ebook"))
                {
                    Intent in=new Intent(ctx, EbookDetailActivity.class);
                    in.putExtra("ebookid",slider.getLink());
                    ctx.startActivity(in);
                }
                else if(slider.getParameter().equals("LiveSession"))
                {
//                    Intent intent = new Intent(ctx, LiveSessionDetailsActivity.class);
//                    //LiveSessionDetailsActivity.liveSession = liveSession1;
//                    ctx.startActivity(intent);
                }
                else if(slider.getParameter().equals("None"))
                {


                }


            }
        });


        /*Picasso.get()
                .load(str[position])
                .placeholder(R.drawable.pic1)
                .into(img);*/
        container.addView(view);
        return view;
    }
    public void openChromeCustomeTab(String uri){
        CustomTabsIntent.Builder builder=new CustomTabsIntent.Builder();
        CustomTabsIntent intent=builder.build();
        intent.launchUrl( ctx, Uri.parse( uri ) );
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


}
