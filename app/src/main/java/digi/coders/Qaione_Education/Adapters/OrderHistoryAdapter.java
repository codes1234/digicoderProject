package digi.coders.Qaione_Education.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
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

import digi.coders.Qaione_Education.Activities.OrderHistoryDetail;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Order;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyHoler> {

    private List<Order> list;
    private Context ctx;
    public OrderHistoryAdapter(List<Order> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_layout,parent,false);
        return new MyHoler(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyHoler holder, int position) {
        Order order = list.get(position);
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        holder.title.setText(order.getItem().getName());
        if (order.getItemtype().equals("Ebook")) {
            Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.EBOOK + order.getItem().getBanner()).placeholder(shimmerDrawable).into(holder.banner);
        }
        if (order.getItemtype().equals("Course")) {
            Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.COURSES + order.getItem().getBanner()).placeholder(shimmerDrawable).into(holder.banner);
        }
        holder.amout.setText("Amount: " + Html.fromHtml("&#8377") + order.getPrice());
        holder.dateTime.setText("Date: " + order.getDate() + " " + order.getTime());
        holder.layoutAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderHistoryDetail.order = order;
                Intent in = new Intent(ctx, OrderHistoryDetail.class);
                ctx.startActivity(in);

            }
        });
        holder.orderType.setText("Type: "+order.getItemtype());
        holder.orderStatus.setText("Status: "+order.getPaymentstatus());
        if (order.getPaymentstatus().equals("failed")) {
            holder.layoutAll.setBackgroundResource(R.color.red);
            Log.e("cvd","failed");

        } else if (order.getPaymentstatus().equals("success")) {
            holder.layoutAll.setBackgroundResource(R.color.green);
            Log.e("cvd","success");

        } else {
            holder.layoutAll.setBackgroundResource(R.color.yellow);
            Log.e("cvd","pending");

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHoler extends RecyclerView.ViewHolder
    {

        private ImageView banner;
        private TextView title,amout,dateTime,orderStatus,orderType;
        private LinearLayout layoutAll;
        public MyHoler(@NonNull View itemView) {
            super(itemView);
            banner=itemView.findViewById(R.id.orderh_banner);
            title=itemView.findViewById(R.id.orderh_title);
            amout=itemView.findViewById(R.id.orderh_paid_amount);
            dateTime=itemView.findViewById(R.id.orderh_datetime);
            layoutAll=itemView.findViewById(R.id.layout_all);
            orderStatus=itemView.findViewById(R.id.orderh_status);
            orderType=itemView.findViewById(R.id.orderh_type);
        }

    }

}
