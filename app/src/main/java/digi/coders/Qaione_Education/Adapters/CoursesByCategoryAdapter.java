package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.Qaione_Education.Activities.CourseDetailsActivity;
import digi.coders.Qaione_Education.Activities.EbookDetailActivity;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Courses;
import digi.coders.Qaione_Education.models.Ebook;

public class  CoursesByCategoryAdapter  extends RecyclerView.Adapter<CoursesByCategoryAdapter.MyHolder> {
    private List<Courses> list;
    private Context ctx;
    private int status;
    private List<Ebook> ebookList;

    public CoursesByCategoryAdapter(List<Courses> list, Context ctx, int status, List<Ebook> ebookList) {
        this.list = list;
        this.ctx = ctx;
        this.status = status;
        this.ebookList = ebookList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.full_courses_list_design,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        //Log.e("sadsa",list.size()+""+ebookList.size()+" "+list.get(position).getAuthor());

        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        if (status == 1 || status == 3) {
            Courses courses = list.get(position);

            Picasso.get().load(Constraints.BASE_IMAGE_URL+ Constraints.COURSES+courses.getBanner()).placeholder(shimmerDrawable).into(holder.coursesBanner);
            holder.coursesTitle.setText(courses.getName());
            if(courses.getType().equals("Paid"))
            {
                holder.priceLayout.setVisibility(View.VISIBLE);
                holder.cut_price.setText(Html.fromHtml("<strike>&#8377 "+courses.getPrice()+"</strike>"));
                holder.show_price.setText(Html.fromHtml("&#8377")+courses.getOfferprice());
                holder.freeTxt.setVisibility(View.GONE);

            }
            else
            {
                holder.priceLayout.setVisibility(View.GONE);
                holder.freeTxt.setVisibility(View.VISIBLE);

            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(ctx, CourseDetailsActivity.class);
                    in.putExtra("courseid",courses.getId());
                    ctx.startActivity(in);
                }
            });
            holder.discountW.setText(courses.getDiscountpercent());
            holder.ratingBar.setRating((float) courses.getRating());
            holder.rateValue.setText(courses.getRating()+"");
            holder.ratingValue.setText(courses.getTotalrating()+"");
            holder.description.setText(courses.getShortdesc());

        }
        else {
            Ebook ebook = ebookList.get(position);
            Log.e("sds","this is ebook");
            Picasso.get().load(Constraints.BASE_IMAGE_URL+ Constraints.EBOOK+ebook.getBanner()).placeholder(shimmerDrawable).into(holder.coursesBanner);
            holder.coursesTitle.setText(ebook.getName());
            if(ebook.getType().equals("Paid"))
            {
                holder.priceLayout.setVisibility(View.VISIBLE);
                holder.cut_price.setText(Html.fromHtml("<strike>&#8377 "+ebook.getPrice()+"</strike>"));
                holder.show_price.setText(Html.fromHtml("&#8377")+ebook.getOfferprice());
                holder.freeTxt.setVisibility(View.GONE);

            }
            else
            {
                holder.priceLayout.setVisibility(View.GONE);
                holder.freeTxt.setVisibility(View.VISIBLE);

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(ctx, EbookDetailActivity.class);
                    in.putExtra("ebookid",ebook.getId());
                    ctx.startActivity(in);
                }
            });

            holder.ratingBar.setRating((float) ebook.getRating());
            holder.rateValue.setText(ebook.getRating()+"");
            holder.ratingValue.setText(ebook.getTotalrating()+"");
            holder.discountW.setText(ebook.getDiscountpercent());
            holder.description.setText(ebook.getShortdesc());
        }


    }

    @Override
    public int getItemCount() {
        if(status==1 || status==3)
        {
            Log.i("asddsd","if"+list.size());
            return list.size();
        }
        else
        {

            Log.i("asddsd","else"+ebookList.size());
            return ebookList.size();

        }

    }

    public class MyHolder extends RecyclerView.ViewHolder{

        private ImageView coursesBanner;
        private TextView coursesTitle,description,cut_price,show_price,discountW,rateValue,ratingValue,freeTxt;
        private RatingBar ratingBar;
        private LinearLayout priceLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            coursesBanner=itemView.findViewById(R.id.flc_banner);
            coursesTitle=itemView.findViewById(R.id.flc_title);
            description=itemView.findViewById(R.id.description);
            cut_price=itemView.findViewById(R.id.flc_cut_price);
            show_price=itemView.findViewById(R.id.flc_show_price);
            discountW=itemView.findViewById(R.id.discountw);
            rateValue=itemView.findViewById(R.id.rate_value);
            ratingBar=itemView.findViewById(R.id.rating);
            ratingValue=itemView.findViewById(R.id.rating_value);
            priceLayout=itemView.findViewById(R.id.price_layout);
            freeTxt=itemView.findViewById(R.id.free);

        }
    }
}
