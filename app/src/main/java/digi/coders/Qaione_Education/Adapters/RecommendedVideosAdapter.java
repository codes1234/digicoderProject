package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.Qaione_Education.Activities.RecommendedVideoActivity;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.RecommendedVideos;

public class RecommendedVideosAdapter extends RecyclerView.Adapter<RecommendedVideosAdapter.MyHolder> {

    private List<RecommendedVideos> videoList;
    private Context ctx;
    public RecommendedVideosAdapter(List<RecommendedVideos> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_videos_layout,parent,false);
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
        RecommendedVideos recommendedVideos=videoList.get(position);
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.THUMBNIL+recommendedVideos.getVideo().getThumbnail()).placeholder(shimmerDrawable).into(holder.videoThumbnil);
        holder.videoTitle.setText(recommendedVideos.getVideo().getTitle());
        holder.duration.setText(recommendedVideos.getVideo().getDuration());
        holder.author.setText("By "+recommendedVideos.getAuthor().getName());

        holder.layoutAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(ctx,RecommendedVideoActivity.class);
                in.putExtra("id",recommendedVideos.getId());
                in.putExtra("type",recommendedVideos.getVideo().getType());
                if(recommendedVideos.getVideo().getType().equals("Internal")) {
                    in.putExtra("link", recommendedVideos.getVideo().getVideo());
                }
                else {
                    in.putExtra("link", recommendedVideos.getVideo().getLink());
                }
                Log.e("sdsdwew",recommendedVideos.getId());
                ctx.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder
    {

        private ImageView videoThumbnil;
        private TextView videoTitle, duration, author;
        private CardView layoutAll;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            videoThumbnil=itemView.findViewById(R.id.video_thubmnil);
            videoTitle=itemView.findViewById(R.id.video_title);
            layoutAll=itemView.findViewById(R.id.layout_all);
            duration=itemView.findViewById(R.id.duration);
            author=itemView.findViewById(R.id.author);

        }
    }
}
