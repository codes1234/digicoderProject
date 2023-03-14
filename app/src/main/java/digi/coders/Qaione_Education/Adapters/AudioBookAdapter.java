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

import java.util.List;

import digi.coders.Qaione_Education.Activities.AudioBookDetailsActivity;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Abook;

public class AudioBookAdapter extends RecyclerView.Adapter<AudioBookAdapter.ViewHolder> {
    private List<Abook> list;
    private Context ctx;
    public AudioBookAdapter(List<Abook> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AudioBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View   view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio, parent, false);
        return new AudioBookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioBookAdapter.ViewHolder holder, final int position) {
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        Abook abook=list.get(position);
        holder.name.setText(abook.getName());
        holder.description.setText(abook.getShortdesc());

        holder.layout_all.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ctx, AudioBookDetailsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("abookId",abook.getId());
                        ctx.startActivity(intent);

                    }
                }
        );
//        holder.ratingBar.setRating((float)ebook.getRating());
//        holder.rateValue.setText(ebook.getRating()+"");
//        holder.ratingValue.setText(ebook.getTotalrating()+"");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, description;
        LinearLayout layout_all;
        private ImageView image;
        private LinearLayout priceLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout_all=itemView.findViewById(R.id.layout_all);
            image=itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.name);
            description=itemView.findViewById(R.id.description);
            priceLayout=itemView.findViewById(R.id.price_layout);

        }
    }
}
