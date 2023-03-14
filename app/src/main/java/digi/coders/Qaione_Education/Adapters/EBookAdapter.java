package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.Qaione_Education.Activities.EbookDetailActivity;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Ebook;


public class EBookAdapter extends RecyclerView.Adapter<EBookAdapter.ViewHolder> {
    private List<Ebook> list;
    private Context ctx;
    int status;
    public EBookAdapter(List<Ebook> list, int status) {
        this.list = list;
        this.status = status;
    }

    @NonNull
    @Override
    public EBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view;
        if (status == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ebook_layout, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ebook_layout2, parent, false);
        }
           return new EBookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EBookAdapter.ViewHolder holder, final int position) {
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        Ebook ebook=list.get(position);
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.EBOOK+ebook.getBanner()).placeholder(shimmerDrawable).into(holder.banner);
        holder.ebookTitle.setText(ebook.getName());

        holder.cut_price.setText(Html.fromHtml("<strike>&#8377 "+ebook.getPrice()+"</strike>"));
        holder.show_price.setText(Html.fromHtml("&#8377")+ebook.getOfferprice());
        holder.ebook_discount.setText(ebook.getDiscountpercent());

        if (ebook.getType().equals("Paid")) {
            holder.show_price.setText(Html.fromHtml("&#8377") + ebook.getOfferprice());
            holder.freeText.setVisibility(View.GONE);
            if (ebook.getDiscountpercent().equals("0")) {
                holder.ebook_discount.setVisibility(View.GONE);
                holder.cut_price.setVisibility(View.GONE);
            } else {
                holder.ebook_discount.setText(ebook.getDiscountpercent());
                holder.cut_price.setText(Html.fromHtml("<strike>&#8377 " + ebook.getPrice() + "</strike>"));
            }

        } else {
            holder.priceLayout.setVisibility(View.GONE);
            holder.freeText.setVisibility(View.VISIBLE);

        }
        holder.layout_all.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ctx, EbookDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Log.e("sda",ebook.getId());
                        intent.putExtra("ebookid",ebook.getId());
                        ctx.startActivity(intent);

                    }
                }
        );
        holder.description.setText(ebook.getShortdesc());
        holder.ratingBar.setRating((float)ebook.getRating());
        holder.rateValue.setText(ebook.getRating()+"");
        holder.ratingValue.setText(ebook.getTotalrating()+"");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout_all;
        private ImageView banner;
        private TextView ebookTitle,description,show_price,cut_price,ebook_discount,ratingValue,rateValue,freeText;
        private RatingBar ratingBar;
        private LinearLayout priceLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout_all=itemView.findViewById(R.id.layout_all);
            banner=itemView.findViewById(R.id.ebook_banner);
            ebookTitle=itemView.findViewById(R.id.ebook_title);
            description=itemView.findViewById(R.id.description);
            priceLayout=itemView.findViewById(R.id.price_layout);
            show_price=itemView.findViewById(R.id.ebook_show_price);
            cut_price=itemView.findViewById(R.id.ebook_cut_price);
            ebook_discount=itemView.findViewById(R.id.ebook_discount);
            ratingBar=itemView.findViewById(R.id.rating);
            ratingValue=itemView.findViewById(R.id.rating_value);
            rateValue=itemView.findViewById(R.id.rate_value);
            freeText=itemView.findViewById(R.id.free_txt);

        }
    }
}
