package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import digi.coders.Qaione_Education.R;


public class MyViewPagerAdapter extends PagerAdapter {

    private int[] array_image= new int[] {
            R.drawable.slide1,
            R.drawable.slide2,
            R.drawable.slide3,
            R.drawable.slide4};


    String[] str,title;
    int[] date;

    Context ctx;

    public MyViewPagerAdapter(String[] str,String[] title,int[] date, Context ctx) {
        this.str = str;
        this.title = title;
        this.date = date;
        this.ctx = ctx;
    }

    private LayoutInflater layoutInflater;
    private LinearLayout parent;
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.view_pager_layout, container, false);
        ImageView img=view.findViewById(R.id.card_logo);
        TextView title1=view.findViewById(R.id.title);
        parent=view.findViewById(R.id.lyt_parent);
        TextView date1=view.findViewById(R.id.date);

        date1.setText(title[position]);
        title1.setText(str[position]);
        Picasso.get()
                .load(array_image[position])
                .into(img);
        if(position==0)
        {
            parent.setBackgroundColor(ctx.getResources().getColor(R.color.slide1));
        }
         if(position==1)
        {
            parent.setBackgroundColor(ctx.getResources().getColor(R.color.slide2));

        }
        container.addView(view);
        return view;
    }


    @Override
    public int getCount() {
        return array_image.length;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
