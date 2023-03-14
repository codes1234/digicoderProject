package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import digi.coders.Qaione_Education.models.SearchItem;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.MyHolder>{


    private List<SearchItem> list;
    private Context ctx;
    public SearchItemAdapter(List<SearchItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.full_courses_list_design,parent,false);
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
        SearchItem searchItem=list.get(position);
        holder.title.setText(searchItem.getName()+"( "+Html.fromHtml("<b>"+searchItem.getItemtype()+"</b>")+" )");
        holder.ratingBar.setRating((float)searchItem.getRating());
        holder.cutPrice.setText(Html.fromHtml("<strike>&#8377 "+searchItem.getPrice()+"</strike>"));
        holder.showPrice.setText(Html.fromHtml("&#8377")+searchItem.getOfferprice());
        holder.rateValue.setText(searchItem.getRating()+"");
        holder.ratingValue.setText(searchItem.getTotalrating()+"");
        holder.discount.setText(searchItem.getDiscountpercent());
        String itemType=searchItem.getItemtype();
        if(itemType.equals("Course"))
        {
            Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.COURSES+searchItem.getLogo()).placeholder(shimmerDrawable).into(holder.banner);


        }
        if(itemType.equals("Ebook"))
        {
            Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.EBOOK+searchItem.getLogo()).placeholder(shimmerDrawable).into(holder.banner);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(itemType.equals("Course")) {
                    Intent intent = new Intent(ctx, CourseDetailsActivity.class);
                    intent.putExtra("courseid", searchItem.getItemid() + "");
                    ctx.startActivity(intent);

                }
                if(itemType.equals("Ebook"))
                {
                    Intent intent = new Intent(ctx, EbookDetailActivity.class);
                    intent.putExtra("ebookid", searchItem.getItemid() + "");
                    ctx.startActivity(intent);

                }

            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private ImageView banner;
        private TextView title,discount,showPrice,cutPrice,ratingValue,rateValue;
        private RatingBar ratingBar;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            banner=itemView.findViewById(R.id.flc_banner);
            title=itemView.findViewById(R.id.flc_title);
            showPrice=itemView.findViewById(R.id.flc_show_price);
            cutPrice=itemView.findViewById(R.id.flc_cut_price);
            ratingBar=itemView.findViewById(R.id.rating);
            discount=itemView.findViewById(R.id.discountw);
            ratingValue=itemView.findViewById(R.id.rating_value);
            rateValue=itemView.findViewById(R.id.rate_value);
        }
    }
}
