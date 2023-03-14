package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Review;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {

    Review[] list;
    Context ctx;

    public RatingAdapter(Review[] list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_layout_image, parent, false);

        return new RatingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.ViewHolder holder, final int position) {
        Review review=list[position];
        Log.e("re",review.getDate()+"");
        //holder.txt_name.setText(review.getName());
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        holder.ratingtxt.setText(review.getReview());
        if(!review.getName().isEmpty())
        {
            holder.name.setText(review.getName());
        }
        else
        {
            holder.name.setText("New User");

        }

        holder.date.setText(review.getDate()+","+review.getTime());
        double d=Double.parseDouble(review.getRating());
        holder.ratingBar.setRating((float)d);
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.PROFILE_PHOTO+review.getProfilePhoto()).placeholder(R.drawable.user).into(holder.imageView);
        Log.e("sdasd",review.getProfilePhoto()+"");


    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rating,ratingtxt;
        CircleImageView imageView;
        TextView name,date,txt_name;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingtxt=itemView.findViewById(R.id.ratign_txt);
            ratingBar=itemView.findViewById(R.id.ratingbar);
            name=itemView.findViewById(R.id.name);
            date=itemView.findViewById(R.id.date);
            imageView=itemView.findViewById(R.id.review_image);

        }
    }
}
