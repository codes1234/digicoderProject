package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.Qaione_Education.Activities.MyCourseDetailsActivity;
import digi.coders.Qaione_Education.Activities.MyEbookDetailsActivity;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Item;
import digi.coders.Qaione_Education.models.MyCertificate;
import digi.coders.Qaione_Education.models.MyItem;

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.MyHolder> {
    private List<MyItem> itemList;
    private Context ctx;
    private List<MyCertificate> list;
    public MyItemAdapter(List<MyItem> itemList) {
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_item_list_layout,parent,false);
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
        MyItem myItem=itemList.get(position);

        Item item=myItem.getItem();

        if(item!=null) {
            if (myItem.getItemtype().equals("Ebook")) {
                Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.EBOOK + item.getBanner()).placeholder(shimmerDrawable).into(holder.itemBanner);
                holder.progreeLay.setVisibility(View.GONE);

            }
            if (myItem.getItemtype().equals("Course")) {
                Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.COURSES + item.getBanner()).placeholder(shimmerDrawable).into(holder.itemBanner);

            }

            holder.itemTitle.setText(item.getName());
            holder.ratingBar.setRating((float)myItem.getRating());
            holder.rateValue.setText(myItem.getRating()+"");
            holder.ratingValue.setText(myItem.getTotalrating()+"");

            String[] e=myItem.getProgress().split("%");


            holder.progressBar.setProgress(Integer.parseInt(e[0]));
            holder.progressValue.setText(myItem.getProgress());

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myItem.getItemtype().equals("Course")) {
                    Intent intent = new Intent(ctx, MyCourseDetailsActivity.class);
                    intent.putExtra("courseid", item.getId() + "");
                    ctx.startActivity(intent);
                }
                if (myItem.getItemtype().equals("Ebook"))
                {
                    Intent intent = new Intent(ctx, MyEbookDetailsActivity.class);
                    Log.e("ewe",item.getId()+"");
                    intent.putExtra("ebookid", item.getId()+"");
                    ctx.startActivity(intent);

                }
            }
        });
        //holder.delete.setVisibility(View.GONE);
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder
    {
        private RatingBar ratingBar;
        private ProgressBar progressBar;
        private ImageView itemBanner;
        private TextView itemTitle,ratingValue,progressValue,rateValue;

        private LinearLayout progreeLay;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            itemBanner=itemView.findViewById(R.id.item_flc_banner);
            itemTitle=itemView.findViewById(R.id.item_flc_title);
            progreeLay=itemView.findViewById(R.id.progress_layout);
            ratingBar=itemView.findViewById(R.id.rating);
            ratingValue=itemView.findViewById(R.id.rating_value);
            progressBar=itemView.findViewById(R.id.progress);
            progressValue=itemView.findViewById(R.id.progress_value);
            rateValue=itemView.findViewById(R.id.rate_value);
        }
    }
}
