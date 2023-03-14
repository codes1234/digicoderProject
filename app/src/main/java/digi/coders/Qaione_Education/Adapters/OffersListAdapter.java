package digi.coders.Qaione_Education.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Coupon;

public class OffersListAdapter extends RecyclerView.Adapter<OffersListAdapter.MyHolder> {

    private List<Coupon> couponList;
    private Context ctx;
    int i=0;
    public OffersListAdapter(List<Coupon> couponList) {
        this.couponList = couponList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_design,parent,false);
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
        Coupon coupon=couponList.get(position);
        holder.coupon_code.setText(coupon.getCoupon());
        holder.offerDescription.setText(coupon.getDescription());
        holder.expirydate.setText("Expire on "+coupon.getExpiryDate());
        if(coupon.getDiscountType().equals("Amount"))
        {
            holder.discount.setText("Get ₹ "+coupon.getDiscount()+" upto ₹ "+coupon.getUpto());
        }
        else
        {
            holder.discount.setText("Get "+coupon.getDiscount()+"% upto ₹ "+coupon.getUpto());
        }


        Log.e("sdd",coupon.getBanner());
    Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.OFFERS+coupon.getBanner()).placeholder(shimmerDrawable).into(holder.coponlogo);
    holder.copyCode.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClipboardManager clipboard = (ClipboardManager)
                    ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("simple text",holder.coupon_code.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(ctx, "Coupon code copied successfully", Toast.LENGTH_SHORT).show();
        }
    });

    holder.offerBanner.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(i==0)
            {
                holder.offerFull.setVisibility(View.VISIBLE);
                i++;
            }
            else
            {
                holder.offerFull.setVisibility(View.GONE);
                i=0;
            }

        }
    });

    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        private TextView coupon_code,offerDescription,expirydate,discount,copyCode;
        private ImageView coponlogo;
        private LinearLayout offerFull,offerBanner;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            coupon_code=itemView.findViewById(R.id.coupon_code);
            offerDescription=itemView.findViewById(R.id.coupon_description);
            copyCode=itemView.findViewById(R.id.copy_btn);
            expirydate=itemView.findViewById(R.id.offer_expiry_date);
            discount=itemView.findViewById(R.id.offer_discount);
//            upto=itemView.findViewById(R.id.discount_upto);
            coponlogo=itemView.findViewById(R.id.coupon_logo);
            offerFull=itemView.findViewById(R.id.offer_full);
            offerBanner=itemView.findViewById(R.id.offer_banner);

        }
    }
}
