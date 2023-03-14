package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.Qaione_Education.Activities.PlayVideoActivity;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Courses;
import digi.coders.Qaione_Education.models.VideoDetails;


public class CourseDetailsAdapter extends RecyclerView.Adapter<CourseDetailsAdapter.ViewHolder> {

    private Context ctx;
    private List<VideoDetails> list;
    Courses courses;
    int status;

    public CourseDetailsAdapter(Context ctx, List<VideoDetails> list, Courses courses,int status) {
        this.ctx = ctx;
        this.list = list;
        this.courses = courses;
        this.status=status;
    }

    @NonNull
    @Override
    public CourseDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (status==2){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_details_layout, parent, false);
        }else if (status==1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_details_layout, parent, false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_details_layout2, parent, false);
        }
        return new CourseDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseDetailsAdapter.ViewHolder holder, final int position) {
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        VideoDetails videoDetails=list.get(position);
        holder.videoTitle.setText(videoDetails.getTitle());
        holder.videoDuration.setText(videoDetails.getDuration());
        holder.videoDesc.setText(videoDetails.getDescription());
        Log.e("sds",videoDetails.getDuration());
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.THUMBNIL+videoDetails.getThumbnail()).placeholder(shimmerDrawable).into(holder.videoThumbnil);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status == 2 || status == 0) {
                    Intent in = new Intent(ctx, PlayVideoActivity.class);
                    PlayVideoActivity.videoDetails = videoDetails;
                    PlayVideoActivity.coursesw=courses;
                    in.putExtra("videoid", videoDetails.getId());
                    in.putExtra("courseid", courses.getId());
                    in.putExtra("type", videoDetails.getType());
                    Log.i("playvideoauthor", courses.getAuthorDetails().getName());
                    if (videoDetails.getType().equals("Internal")) {
                        in.putExtra("link", videoDetails.getVideo());
                    } else {
                        in.putExtra("link", videoDetails.getLink());

                    }
                    //ctx.startActivity(in);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(in);

                }
                else
                {
                    Toast.makeText(ctx, "Please Enroll this course", Toast.LENGTH_SHORT).show();
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
        private ImageView videoThumbnil;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoTitle=itemView.findViewById(R.id.video_intro);
            videoDesc=itemView.findViewById(R.id.video_desc);
            videoThumbnil=itemView.findViewById(R.id.thumbnail);
            videoDuration=itemView.findViewById(R.id.duration);

        }
    }
}
