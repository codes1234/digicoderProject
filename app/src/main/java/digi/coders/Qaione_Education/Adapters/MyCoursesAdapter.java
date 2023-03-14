package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.Qaione_Education.Activities.MyCourseDetailsActivity;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Item;
import digi.coders.Qaione_Education.models.MyItem;


public class MyCoursesAdapter extends RecyclerView.Adapter<MyCoursesAdapter.ViewHolder> {

    Context ctx;
    List<MyItem> itemList;
    int  status;

    public MyCoursesAdapter(Context ctx, List<MyItem> itemList, int status) {
        this.ctx = ctx;
        this.itemList = itemList;
        this.status = status;
    }

    @NonNull
    @Override
    public MyCoursesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(status==1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_courses_layout, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_courses_laout, parent, false);
        }
        return new MyCoursesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCoursesAdapter.ViewHolder holder, final int position) {

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
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.COURSES+item.getBanner()).placeholder(shimmerDrawable).into(holder.myCoursePic);
        holder.myCourseTitle.setText(item.getName());
        String[] d=myItem.getProgress().split("%");
        holder.progressBar.setProgress(Integer.parseInt(d[0]));
        holder.progressValue.setText(myItem.getProgress());
        holder.myCourseAuthor.setText("By "+item.getAuthor().getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, MyCourseDetailsActivity.class);
                intent.putExtra("courseid",item.getId());
                Log.e("sds",myItem.getId()+"");
                intent.putExtra("enrollid",myItem.getId());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView myCoursePic;
    private TextView myCourseTitle,myCourseAuthor,progressValue;
    private ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myCoursePic=itemView.findViewById(R.id.my_course_pic);
            myCourseTitle=itemView.findViewById(R.id.my_course_title);
            myCourseAuthor=itemView.findViewById(R.id.my_course_write_name);
            progressBar=itemView.findViewById(R.id.progressBar);
            progressValue=itemView.findViewById(R.id.progres_val);
        }
    }
}
