package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
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
import digi.coders.Qaione_Education.Activities.OrderSummaryActivity;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Courses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

    private Context ctx;
    private int status;
    private List<Courses> list;
    String help_mobile, help_email;

    public CoursesAdapter(Context ctx, int status, List<Courses> list) {
        this.ctx = ctx;
        this.status = status;
        this.list = list;
    }

    @NonNull
    @Override
    public CoursesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(status==0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.corses_layout_big, parent, false);
        }else if(status==2){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.corses_layout_small, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.corses_layout_small, parent, false);
        }
        return new CoursesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesAdapter.ViewHolder holder, final int position) {
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        if (status == 1) {
            Courses courses = list.get(position);
            Log.e("topcourselist",list.size()+"");
            Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.COURSES + courses.getBanner()).placeholder(shimmerDrawable).into(holder.coursesBanner);
            holder.coursesTitle.setText(courses.getName());
            if (courses.getType().equals("Paid")) {
                holder.show_price.setText(Html.fromHtml("&#8377") + courses.getOfferprice());
                holder.freeText.setVisibility(View.GONE);
                if (courses.getDiscountpercent().equals("0")) {
                    holder.courseDiscount.setVisibility(View.GONE);
                    holder.cut_price.setVisibility(View.GONE);
                } else {
                    holder.courseDiscount.setText(courses.getDiscountpercent());
                    holder.cut_price.setText(Html.fromHtml("<strike>&#8377 " + courses.getPrice() + "</strike>"));
                }

            } else {
                holder.priceLayout.setVisibility(View.GONE);
                holder.freeText.setVisibility(View.VISIBLE);

            }
            holder.layout_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(ctx, CourseDetailsActivity.class);
                    in.putExtra("courseid", courses.getId());
                    ctx.startActivity(in);
                }
            });
            holder.coursesRatingbar.setRating((float)courses.getRating());
            holder.rateValue.setText(courses.getRating()+"");
            holder.coursesRatingValue.setText(courses.getTotalrating()+"");
            holder.description.setText(courses.getShortdesc());

            holder.buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(ctx, OrderSummaryActivity.class);
                    OrderSummaryActivity.course=courses;
                    in.putExtra("key","1");
                    ctx.startActivity(in);

                }
            });

        }
        if (status ==0) {
            Log.e("trendingcourselist",list.size()+"");
            Courses courses = list.get(position);
            Log.e("size", list.size() + "");
            Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.COURSES + courses.getBanner()).placeholder(shimmerDrawable).into(holder.coursesBanner);
            holder.coursesTitle.setText(courses.getName());
            holder.description.setText(courses.getShortdesc());

            holder.buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(ctx, OrderSummaryActivity.class);
                    OrderSummaryActivity.course=courses;
                    in.putExtra("key","1");
                    ctx.startActivity(in);

                }
            });

            holder.contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContactUsDialog();

                }
            });

            holder.layout_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(ctx, CourseDetailsActivity.class);
                    in.putExtra("courseid", courses.getId());
                    ctx.startActivity(in);

                }
            });

            if (courses.getType().equals("Paid")) {
                holder.show_price.setText(Html.fromHtml("&#8377") + courses.getOfferprice());
                holder.freeText.setVisibility(View.GONE);

                if (courses.getDiscountpercent().equals("0")) {
                    holder.courseDiscount.setVisibility(View.GONE);
                    holder.cut_price.setVisibility(View.GONE);
                } else {
                    holder.courseDiscount.setText(courses.getDiscountpercent());
                    holder.cut_price.setText(Html.fromHtml("<strike>&#8377 " + courses.getPrice() + "</strike>"));
                }


            } else {
                holder.priceLayout.setVisibility(View.GONE);
                holder.freeText.setVisibility(View.VISIBLE);

            }
            holder.coursesRatingbar.setRating((float) courses.getRating());
            holder.rateValue.setText(courses.getRating()+"");
            holder.coursesRatingValue.setText(courses.getTotalrating()+"");
        }
        if (status == 2) {
            Log.e("latestcourselist",list.size()+"");
            Courses courses = list.get(position);
            Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.COURSES + courses.getBanner()).placeholder(shimmerDrawable).into(holder.coursesBanner);

            holder.coursesTitle.setText(courses.getName());
            if (courses.getType().equals("Paid")) {
                holder.show_price.setText(Html.fromHtml("&#8377") + courses.getOfferprice());
                holder.freeText.setVisibility(View.GONE);

                if (courses.getDiscountpercent().equals("0")) {
                    holder.courseDiscount.setVisibility(View.GONE);
                    holder.cut_price.setVisibility(View.GONE);
                } else {
                    holder.courseDiscount.setText(courses.getDiscountpercent());
                    holder.cut_price.setText(Html.fromHtml("<strike>&#8377 " + courses.getPrice() + "</strike>"));
                }

            } else {
                holder.priceLayout.setVisibility(View.GONE);
                holder.freeText.setVisibility(View.VISIBLE);

            }

            holder.layout_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(ctx, CourseDetailsActivity.class);
                    in.putExtra("courseid", courses.getId());
                    ctx.startActivity(in);

                }
            });
            holder.coursesRatingbar.setRating((float) courses.getRating());
            holder.rateValue.setText(courses.getRating()+"");
            holder.coursesRatingValue.setText(courses.getTotalrating()+"");
            holder.description.setText(courses.getShortdesc());

            holder.buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(ctx, OrderSummaryActivity.class);
                    OrderSummaryActivity.course=courses;
                    in.putExtra("key","1");
                    ctx.startActivity(in);

                }
            });

        }
        if(status==0)
        {
//            if(position>2)
//            {
//                holder.layout_all.setVisibility(View.GONE);
//                holder.see_all_text.setVisibility(View.VISIBLE);
//                holder.see_all_text.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(ctx, FullCoursesListActivity.class);
//                        intent.putExtra("status", "0");
//                        ctx.startActivity(intent);
//                    }
//                });
//
//            }
//            else
//            {
//                //Log.e("sdsds","perfext size");
//
//            }

        }
        else if(status==1)
        {

//            if(position>2)
//            {
//                holder.layout_all.setVisibility(View.GONE);
//                holder.see_all_text.setVisibility(View.VISIBLE);
//                holder.see_all_text.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(ctx, FullCoursesListActivity.class);
//                        intent.putExtra("status", "1");
//                        ctx.startActivity(intent);
//                    }
//                });
//
//            }
//            else
//            {
//                //Log.e("sdsds","perfext size");
//
//            }
        }
        else
        {
//            if(position>2)
//            {
//                holder.layout_all.setVisibility(View.GONE);
//                holder.see_all_text.setVisibility(View.VISIBLE);
//                holder.see_all_text.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(ctx, FullCoursesListActivity.class);
//                        intent.putExtra("status", "2");
//                        ctx.startActivity(intent);
//                    }
//                });
//
//            }
//            else
//            {//Log.e("sdsds","perfext size");
//            }
        }

            getAppDetails();
     }

    @Override
    public int getItemCount() {

        Log.e("sds",list.size()+"");
        return list.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RatingBar coursesRatingbar;
        TextView show_price,cut_price,coursesRatingValue,coursesTitle,courseDiscount,freeText,rateValue,description;
        ImageView coursesBanner;
        MaterialRippleLayout see_all_text;
        LinearLayout layout_all,priceLayout;
        Button buyNow;
        LinearLayout contact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_price=itemView.findViewById(R.id.show_price);
            cut_price=itemView.findViewById(R.id.cut_price);
            see_all_text=itemView.findViewById(R.id.see_all_text);
            layout_all=itemView.findViewById(R.id.layout_all);
            priceLayout=itemView.findViewById(R.id.pri_layout);
            coursesBanner=itemView.findViewById(R.id.banner);
            coursesTitle=itemView.findViewById(R.id.course_title);
            coursesRatingbar=itemView.findViewById(R.id.courses_rating_bar);
            coursesRatingValue=itemView.findViewById(R.id.courses_rating_value);
            layout_all=itemView.findViewById(R.id.layout_all);
            freeText=itemView.findViewById(R.id.free);
            courseDiscount=itemView.findViewById(R.id.discount);
            rateValue=itemView.findViewById(R.id.rate_value);
            description=itemView.findViewById(R.id.description);
            buyNow=itemView.findViewById(R.id.buyNow);
            contact=itemView.findViewById(R.id.contact);
        }
    }

    public void ContactUsDialog(){
//        try {
            final AlertDialog dialogBuilder = new AlertDialog.Builder(ctx).create();
            LayoutInflater inflater = LayoutInflater.from(ctx);
            View dialogView = inflater.inflate(R.layout.contact_us_dialog, null);

            dialogBuilder.setCanceledOnTouchOutside(false);
            ImageView review_close_btn=dialogView.findViewById(R.id.review_close_btn);
            LinearLayout line_call=dialogView.findViewById(R.id.line_call);
            LinearLayout line_mail=dialogView.findViewById(R.id.line_mail);
            review_close_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            });
            line_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ help_mobile));
                    ctx.startActivity(intent);
                }
            });
            line_mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, help_email);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
                    ctx.startActivity(Intent.createChooser(intent, "Send Email"));
                }
            });


            dialogBuilder.setView(dialogView);
            dialogBuilder.show();

//        } catch (Exception e) {
//
//        }
    }

    private void getAppDetails() {
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.getAppDetails();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        Log.i("redfhg", response.body().toString());
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        if(res.equals("success")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            help_mobile=jsonObject1.getString("help_mobile");
                            help_email=jsonObject1.getString("help_email");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

    }

}
