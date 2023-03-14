package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
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

import digi.coders.Qaione_Education.Activities.LiveVideoPlayActivity;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.LiveSession;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LiveClassesAdapter extends RecyclerView.Adapter<LiveClassesAdapter.ViewHolder> {

    private Context ctx;
    private List<LiveSession> liveSessionList;
    private SingleTask singleTask;
    int status;

    public LiveClassesAdapter(Context ctx, List<LiveSession> liveSessionList, SingleTask singleTask, int status) {
        this.ctx = ctx;
        this.liveSessionList = liveSessionList;
        this.singleTask = singleTask;
        this.status = status;
    }

    @NonNull
    @Override
    public LiveClassesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (status==0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_classes_layout2, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_classes_layout, parent, false);
        }
        return new LiveClassesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveClassesAdapter.ViewHolder holder, final int position) {
        LiveSession liveSession=liveSessionList.get(position);
        holder.tag.setText(liveSession.getTags());
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        //shimmerDrawable.setBounds();
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.LIVE_VIDEO+liveSession.getThumbnail()).placeholder(shimmerDrawable).into(holder.thumbnil);
        //Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.TUTOR+liveSession.getAuthor().getPhoto()).placeholder(shimmerDrawable).into(holder.authorImage);
        holder.desc.setText(liveSession.getDescription());
        holder.time.setText(liveSession.getTiming()+" , "+liveSession.getDuration()+" mins");
        holder.title.setText(liveSession.getTitle());
      //  holder.authorName.setText(liveSession.getAuthor().getName());

        if (status==0){
            if (liveSession.getSession_status().equals("Scheduled")){
                holder.attendSession.setText("Notify");
            }else if (liveSession.getSession_status().equals("Started")){
                holder.attendSession.setText("Live");
            }else {
                holder.attendSession.setText("live");
            }
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JoinWithoutInformation(v,liveSession);
                }
            });

        }else {
            if (liveSession.getSession_status().equals("Scheduled")){
                holder.play.setVisibility(View.GONE);
                holder.notify.setVisibility(View.VISIBLE);
            }else if (liveSession.getSession_status().equals("Started")){
                holder.play.setVisibility(View.VISIBLE);
                holder.notify.setVisibility(View.GONE);
            }else {
                holder.play.setVisibility(View.VISIBLE);
                holder.notify.setVisibility(View.GONE);
            }
            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JoinWithoutInformation(v,liveSession);
                }
            });
            holder.notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JoinWithoutInformation(v,liveSession);
                }
            });
        }

        /*if(liveSession.getJoined().equals("true"))
        {
            holder.joinedSuccess.setVisibility(View.VISIBLE);
            holder.attendSession.setVisibility(View.GONE);
        }
        else
        {
            holder.joinedSuccess.setVisibility(View.GONE);
            holder.attendSession.setVisibility(View.VISIBLE);

        }*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void JoinWithoutInformation(View v, LiveSession liveSession) {
        String json=singleTask.getValue("user");
        User user=new Gson().fromJson(json,User.class);
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.JoinWithoutInformation(liveSession.getId(),user.getId());
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
                        String msg=jsonObject.getString("msg");
                        if(res.equals("success")) {
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                            LiveSession liveSession1 = new Gson().fromJson(jsonObject1.toString(), LiveSession.class);

                            if (liveSession1.getSession_status().equals("Scheduled")) {
                                countDownDialog( liveSession1.getRemaining_time());

                            } else if (liveSession1.getSession_status().equals("Started")) {
                                Intent in=new Intent(ctx, LiveVideoPlayActivity.class);
                                in.putExtra("id", liveSession1.getId());
                                ctx.startActivity(in);

                            } else {
//                                Intent intent = new Intent(ctx, LiveClassJoinActivity.class);
//                                LiveSessionDetailsActivity.liveSession = liveSession1;
//                                ctx.startActivity(intent);
//                                Log.e("dfd", "true");
                            }
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

    @Override
    public int getItemCount() {
        return liveSessionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnil;
        private CardView card;
        private LinearLayout line;
        private ImageView play, notify;
        private TextView tag,title,time,desc,authorName,joinedSuccess;
        private TextView attendSession;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card=itemView.findViewById(R.id.card);
            line=itemView.findViewById(R.id.line);
            thumbnil=itemView.findViewById(R.id.live_thumbnil);
            tag=itemView.findViewById(R.id.live_tags);
            title=itemView.findViewById(R.id.live_title);
            desc=itemView.findViewById(R.id.live_desc);
            joinedSuccess=itemView.findViewById(R.id.joined_success);
            time=itemView.findViewById(R.id.live_time);
            authorName=itemView.findViewById(R.id.live_author_name);
            attendSession=itemView.findViewById(R.id.attend_session);
            play=itemView.findViewById(R.id.play);
            notify=itemView.findViewById(R.id.notify);

        }
    }

    public void countDownDialog(String remainingTime){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(ctx).create();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View dialogView = inflater.inflate(R.layout.countdown_dialog, null);

        dialogBuilder.setCanceledOnTouchOutside(false);
        ImageView close=dialogView.findViewById(R.id.close);
        TextView time=dialogView.findViewById(R.id.time);
        time.setText(remainingTime+" mins");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
}
