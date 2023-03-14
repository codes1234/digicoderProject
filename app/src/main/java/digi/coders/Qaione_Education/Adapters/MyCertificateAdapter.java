package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Item;
import digi.coders.Qaione_Education.models.MyCertificate;

public class MyCertificateAdapter extends RecyclerView.Adapter<MyCertificateAdapter.MyHolder> {

    private Context ctx;
    private List<MyCertificate> list;

    public MyCertificateAdapter(List<MyCertificate> list) {
        this.list = list;
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
        MyCertificate myCertificate=list.get(position);
        Item item=myCertificate.getItem();
        Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.COURSES + item.getBanner()).placeholder(shimmerDrawable).into(holder.itemBanner);
        holder.itemTitle.setText(item.getName());

        holder.ratingBar.setRating((float)myCertificate.getRating());
        holder.ratingValue.setText(myCertificate.getRating()+" ( "+myCertificate.getTotalrating()+")");
        String[] e=myCertificate.getProgress().split("%");


        holder.progressBar.setProgress(Integer.parseInt(e[0]));
        holder.progressValue.setText(myCertificate.getProgress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.codersadda.com/Home/Certificate/"+myCertificate.getId();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                ctx.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        private RatingBar ratingBar;
        private ProgressBar progressBar;
        private ImageView itemBanner;
        private TextView itemTitle,ratingValue,progressValue;
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
        }
    }

}
