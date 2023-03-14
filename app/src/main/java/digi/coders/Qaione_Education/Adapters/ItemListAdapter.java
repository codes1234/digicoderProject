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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import digi.coders.Qaione_Education.Activities.CourseDetailsActivity;
import digi.coders.Qaione_Education.Activities.EbookDetailActivity;
import digi.coders.Qaione_Education.Activities.MyCourseDetailsActivity;
import digi.coders.Qaione_Education.Activities.MyEbookDetailsActivity;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Item;
import digi.coders.Qaione_Education.models.WishlistItem;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.MyHolder> {
    private List<WishlistItem> itemList;
    private Context ctx;
    WishlistItem wishlistItem;
    public static SingleTask singleTask;
    GetMyPosition getMyPosition;
    int pos;
    public ItemListAdapter(List<WishlistItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        wishlistItem=itemList.get(position);
        pos=position;
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        Item item=wishlistItem.getItem();
        if(wishlistItem.getItemtype().equals("Ebook")) {
            Picasso.get().load(Constraints.BASE_IMAGE_URL+ Constraints.EBOOK+item.getBanner()).placeholder(shimmerDrawable).into(holder.itemBanner);
        }
        if(wishlistItem.getItemtype().equals("Course"))
        {
            Picasso.get().load(Constraints.BASE_IMAGE_URL+ Constraints.COURSES+item.getBanner()).placeholder(shimmerDrawable).into(holder.itemBanner);
        }

        holder.itemTitle.setText(item.getName());
        holder.itemDiscount.setText(item.getDiscountpercent());

        if(wishlistItem.getItem().getType().equals("Paid"))
        {
            holder.freeText.setVisibility(View.GONE);
            holder.priceLayout.setVisibility(View.VISIBLE);
            holder.cut_price.setText(Html.fromHtml("<strike>&#8377 "+item.getPrice()+"</strike>"));
            holder.show_price.setText(Html.fromHtml("&#8377")+item.getOfferprice());

        }
        else
        {
            holder.priceLayout.setVisibility(View.GONE);
            holder.freeText.setVisibility(View.VISIBLE);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(wishlistItem.getItemtype().equals("Ebook")) {
                    if(wishlistItem.getItem().getPurchaseStatus().equals("true")) {
                        Intent intent=new Intent(ctx, MyEbookDetailsActivity.class);
                        intent.putExtra("ebookid",item.getId()+"");
                        ctx.startActivity(intent);
                    }
                    else
                    {
                        Intent intent=new Intent(ctx, EbookDetailActivity.class);
                        intent.putExtra("ebookid",item.getId()+"");
                        ctx.startActivity(intent);
                    }

                }
                else
                {
                    if(wishlistItem.getItem().getPurchaseStatus().equals("true"))
                    {
                       // Log.e("sdsd",wishlistItem.getItem().getPurchaseStatus()+"");
                        Intent intent=new Intent(ctx, MyCourseDetailsActivity.class);
                        intent.putExtra("courseid",item.getId()+"");
                        ctx.startActivity(intent);
                    }
                    else
                    {
                        Intent intent=new Intent(ctx, CourseDetailsActivity.class);
                        intent.putExtra("courseid",item.getId()+"");
                        ctx.startActivity(intent);
                    }

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void  getMy(GetMyPosition getMyPosition)
    {
        this.getMyPosition=getMyPosition;
    }
    public interface GetMyPosition
    {
        void position(View view,int position);
    }
    public class MyHolder extends RecyclerView.ViewHolder
    {
        private ImageView itemBanner,delete;
        private TextView itemTitle,cut_price,show_price,itemDiscount,freeText;
        private LinearLayout priceLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            itemBanner=itemView.findViewById(R.id.item_flc_banner);
            itemTitle=itemView.findViewById(R.id.item_flc_title);
            cut_price=itemView.findViewById(R.id.item_flc_cut_price);
            priceLayout=itemView.findViewById(R.id.price_layout);
            show_price=itemView.findViewById(R.id.item_flc_show_price);
            freeText=itemView.findViewById(R.id.free_txt);
            itemDiscount=itemView.findViewById(R.id.item_discount);
            delete=itemView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
                    Call<JsonArray> call=myApi.removeWishlist(wishlistItem.getId());
                    call.enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            if(response.isSuccessful())
                            {
                                try {
                                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                                    String res=jsonObject.getString("res");
                                    String msg=jsonObject.getString("msg");
                                    if(res.equals("success"))
                                    {
                                        getMyPosition.position(v,pos);
                                        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            Log.e("er",t.getMessage());
                        }
                    });
                }
            });
        }
    }
}
