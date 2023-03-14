package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import digi.coders.Qaione_Education.Activities.CategoryCoursesActivity;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.CategoriesModel;


public class CategoryCourseAdapter extends RecyclerView.Adapter<CategoryCourseAdapter.ViewHolder> {

    //ArrayList<LoanModel> arrayList;
    Context ctx;
    List<CategoriesModel> list;
    int status;

    public CategoryCourseAdapter(Context ctx, List<CategoriesModel> list, int status) {
        this.ctx = ctx;
        this.list = list;
        this.status = status;
    }

    @NonNull
    @Override
    public CategoryCourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_course_layout, parent, false);
        return new CategoryCourseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryCourseAdapter.ViewHolder holder, final int position) {
        CategoriesModel categoriesModel=list.get(position);

        holder.card.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(ctx, CategoryCoursesActivity.class);
                        intent.putExtra("id",categoriesModel.getId()+"");
                        intent.putExtra("title",categoriesModel.getTitle());
                        intent.putExtra("status",status);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(intent);
                        //Toast.makeText(ctx, status+"", Toast.LENGTH_SHORT).show();

                    }
                }
        );
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        // This is the placeholder for the imageView
      /*  ShimmerDrawable shimmerDrawable = new ShimmerDrawable().setShimmer(shimmer){

        }*/
        Picasso.get().load(Constraints.BASE_IMAGE_URL+ Constraints.CATEGORY+categoriesModel.getIcon()).placeholder(shimmerDrawable).into(holder.icon);
        try {
            holder.cardColor.setCardBackgroundColor(Color.parseColor(categoriesModel.getColor()));
        }catch (Exception e){

        }
        holder.category_name.setText(categoriesModel.getTitle());
        if (status==1){
            holder.courseCount.setText(categoriesModel.getCourses()+" Courses");
        }else {
            holder.courseCount.setText(categoriesModel.getEbooks()+" Ebook");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView category_name, courseCount;
        CardView card;
        CardView cardColor;
        private ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            category_name=itemView.findViewById(R.id.category_name);
            courseCount=itemView.findViewById(R.id.courseCount);
            card=itemView.findViewById(R.id.card);
            cardColor=itemView.findViewById(R.id.card_color);
            icon=itemView.findViewById(R.id.category_icon);

        }
    }
}
