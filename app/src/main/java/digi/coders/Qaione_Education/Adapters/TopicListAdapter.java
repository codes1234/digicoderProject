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

import digi.coders.Qaione_Education.Activities.ReadPdfActivity;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Ebook;
import digi.coders.Qaione_Education.models.TopicListModel;


public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private Context ctx;
    private List<TopicListModel> list;
    Ebook ebook;
    int status;

    public TopicListAdapter(Context ctx, List<TopicListModel> list, Ebook ebook, int status) {
        this.ctx = ctx;
        this.list = list;
        this.ebook = ebook;
        this.status=status;
    }

    @NonNull
    @Override
    public TopicListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_list_layout, parent, false);
        return new TopicListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicListAdapter.ViewHolder holder, final int position) {
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
                    ReadPdfActivity.ebook=ebook;
                    Intent in=new Intent(ctx,ReadPdfActivity.class);
                    in.putExtra("status","2");
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
