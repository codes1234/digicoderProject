package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

import java.util.List;

import digi.coders.Qaione_Education.Activities.AudioFullViewActivity;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Abookdetails;
import digi.coders.Qaione_Education.models.TopicListModel;


public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {

    private Context ctx;
    private List<TopicListModel> list;
    Abookdetails abookdetails;
    int status;

    public PlayListAdapter(Context ctx, List<TopicListModel> list, Abookdetails abookdetails, int status) {
        this.ctx = ctx;
        this.list = list;
        this.abookdetails = abookdetails;
        this.status=status;
    }

    @NonNull
    @Override
    public PlayListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_list_layout, parent, false);
        return new PlayListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListAdapter.ViewHolder holder, final int position) {
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        TopicListModel topicListModel=list.get(position);
        holder.videoTitle.setText(topicListModel.getName());
        holder.videoDuration.setText(topicListModel.getTopic_no());
        holder.videoDesc.setText(topicListModel.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status == 1) {
                    Intent in = new Intent(ctx, AudioFullViewActivity.class);
//                    PlayVideoActivity.videoDetails = videoDetails;
//                    PlayVideoActivity.coursesw=courses;
                    in.putExtra("name", topicListModel.getName());
                    in.putExtra("desc", topicListModel.getDescription());
                    in.putExtra("type", topicListModel.getType());
                    if (topicListModel.getType().equals("Internal")) {
                        in.putExtra("link", topicListModel.getTopic());
                    } else {
                        in.putExtra("link", topicListModel.getTopic());
                    }
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(in);

                }
                else
                {

                }
            }

        });



    }


    @Override
    public int getItemCount() {
        return list.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView videoTitle,videoDesc,videoDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoTitle=itemView.findViewById(R.id.video_intro);
            videoDesc=itemView.findViewById(R.id.video_desc);
            videoDuration=itemView.findViewById(R.id.duration);



        }
    }
}
