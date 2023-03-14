package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
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
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Courses;

public class FullCoursesListAdapter extends RecyclerView.Adapter<FullCoursesListAdapter.MyHolder>{
    private List<Courses> list;
    private Context ctx;

    public FullCoursesListAdapter(List<Courses> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.full_courses_list_design,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        Courses courses=list.get(position);
        Picasso.get().load(Constraints.BASE_IMAGE_URL+ Constraints.COURSES+courses.getBanner()).placeholder(shimmerDrawable).into(holder.coursesBanner);
        holder.coursesTitle.setText(courses.getName());
        if(courses.getType().equals("Paid"))
        {
            holder.show_price.setText(Html.fromHtml("&#8377")+courses.getOfferprice());
            holder.free.setVisibility(View.GONE);
            if(courses.getDiscountpercent().equals("0"))
            {
                holder.courseDiscount.setVisibility(View.GONE);
                holder.cut_price.setVisibility(View.GONE);
            }
            else
            {
                holder.courseDiscount.setText(courses.getDiscountpercent());
                holder.cut_price.setText(Html.fromHtml("<strike>&#8377 "+courses.getPrice()+"</strike>"));
            }

        }
        else
        {
            holder.free.setVisibility(View.VISIBLE);
            holder.priceLayout.setVisibility(View.GONE);


        }

    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(ctx, CourseDetailsActivity.class);
            intent.putExtra("courseid",courses.getId()+"");
            ctx.startActivity(intent);

        }
    });
        holder.ratingBar.setRating((float)courses.getRating());
        holder.rateValue.setText(courses.getRating()+"");
        holder.ratingValue.setText(courses.getTotalrating()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        private ImageView coursesBanner;
        private TextView coursesTitle,cut_price,show_price,courseDiscount,free,rateValue;
        private RatingBar ratingBar;
        private TextView ratingValue;
        private LinearLayout priceLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            coursesBanner=itemView.findViewById(R.id.flc_banner);
            coursesTitle=itemView.findViewById(R.id.flc_title);
            cut_price=itemView.findViewById(R.id.flc_cut_price);
            show_price=itemView.findViewById(R.id.flc_show_price);
            courseDiscount=itemView.findViewById(R.id.discountw);
            ratingBar=itemView.findViewById(R.id.rating);
            free=itemView.findViewById(R.id.free);
            priceLayout=itemView.findViewById(R.id.pri_layout);
            ratingValue=itemView.findViewById(R.id.rating_value);
            rateValue=itemView.findViewById(R.id.rate_value);
        }

    }
}
