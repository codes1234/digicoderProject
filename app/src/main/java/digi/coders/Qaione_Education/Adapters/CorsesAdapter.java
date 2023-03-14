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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.Qaione_Education.Activities.CourseDetailsActivity;
import digi.coders.Qaione_Education.Activities.FullCoursesListActivity;
import digi.coders.Qaione_Education.Activities.OrderSummaryActivity;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Courses;


public class CorsesAdapter extends RecyclerView.Adapter<CorsesAdapter.ViewHolder> {

    private Context ctx;
    private int status;
    private List<Courses> list;

    public CorsesAdapter(Context ctx, int status, List<Courses> list) {
        this.ctx = ctx;
        this.status = status;
        this.list = list;
    }

    @NonNull
    @Override
    public CorsesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(status==0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.corses_layout_big, parent, false);
        }else if(status==2){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.corses_layout_small, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.corses_layout_small, parent, false);
        }
        return new CorsesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CorsesAdapter.ViewHolder holder, final int position) {
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

            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+"9876543210"));
                    ctx.startActivity(intent);

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

            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ctx, "call", Toast.LENGTH_SHORT).show();

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

            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+"9876543210"));
                    ctx.startActivity(intent);
                }
            });

        }
        if(status==0)
        {
            if(position>2)
            {
                holder.layout_all.setVisibility(View.GONE);
                holder.see_all_text.setVisibility(View.VISIBLE);
                holder.see_all_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ctx, FullCoursesListActivity.class);
                        intent.putExtra("status", "0");
                        ctx.startActivity(intent);
                    }
                });

            }
            else
            {
                //Log.e("sdsds","perfext size");

            }

        }
        else if(status==1)
        {

            if(position>2)
            {
                holder.layout_all.setVisibility(View.GONE);
                holder.see_all_text.setVisibility(View.VISIBLE);
                holder.see_all_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ctx, FullCoursesListActivity.class);
                        intent.putExtra("status", "1");
                        ctx.startActivity(intent);
                    }
                });

            }
            else
            {
                //Log.e("sdsds","perfext size");

            }
        }
        else
        {
            if(position>2)
            {
                holder.layout_all.setVisibility(View.GONE);
                holder.see_all_text.setVisibility(View.VISIBLE);
                holder.see_all_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ctx, FullCoursesListActivity.class);
                        intent.putExtra("status", "2");
                        ctx.startActivity(intent);
                    }
                });

            }
            else
            {//Log.e("sdsds","perfext size");
            }
        }

            /*if (list.size()>3) {   //list.size()-1
                if (status == 2) {
                    holder.layout_all.setVisibility(View.GONE);
                    holder.see_all_text.setVisibility(View.VISIBLE);
                    holder.see_all_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ctx, FullCoursesListActivity.class);
                            intent.putExtra("status", "2");
                            ctx.startActivity(intent);
                        }
                    });


                } else if (status == 0) {
                    holder.layout_all.setVisibility(View.GONE);
                    holder.see_all_text.setVisibility(View.VISIBLE);
                    holder.see_all_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ctx, FullCoursesListActivity.class);
                            intent.putExtra("status", "0");
                            ctx.startActivity(intent);
                        }
                    });
                } else if (status == 1) {

                    holder.layout_all.setVisibility(View.GONE);
                    holder.see_all_text.setVisibility(View.VISIBLE);
                    holder.see_all_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ctx, FullCoursesListActivity.class);
                            intent.putExtra("status", "1");
                            ctx.startActivity(intent);
                        }
                    });
                }
            } else {
                holder.layout_all.setVisibility(View.VISIBLE);
                holder.see_all_text.setVisibility(View.GONE);

            }*/
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ctx, "Hello", Toast.LENGTH_SHORT).show();
                }
            });
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
        LinearLayout call;

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
            call=itemView.findViewById(R.id.call);
        }
    }
}
