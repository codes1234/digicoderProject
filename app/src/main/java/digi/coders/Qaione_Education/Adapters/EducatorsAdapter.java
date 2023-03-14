package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.Qaione_Education.Activities.EducatorActivity;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.EducatorModel;

public class EducatorsAdapter extends RecyclerView.Adapter<EducatorsAdapter.ViewHolder> {

    Context ctx;
//    String[] str;
    List<EducatorModel> list;
    int status;

    public EducatorsAdapter(Context ctx, List<EducatorModel> list, int status) {
        this.ctx = ctx;
        this.list = list;
        this.status = status;
    }

    @NonNull
    @Override
    public EducatorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.educator_layout, parent, false);

        return new EducatorsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducatorsAdapter.ViewHolder holder, final int position) {

        EducatorModel educatorModel=list.get(position);

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
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.TUTOR+educatorModel.getPhoto()).placeholder(shimmerDrawable).into(holder.image);
        holder.name.setText(educatorModel.getName());
        holder.description.setText(educatorModel.getDesignation()+", "+educatorModel.getAbout());

        holder.line.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(ctx, EducatorActivity.class);
                        intent.putExtra("id",educatorModel.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(intent);
                        //Toast.makeText(ctx, status+"", Toast.LENGTH_SHORT).show();

                    }
                }
        );


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, description;
        LinearLayout line;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            line=itemView.findViewById(R.id.line);
            description=itemView.findViewById(R.id.description);
            image=itemView.findViewById(R.id.image);

        }
    }
}
