package digi.coders.Qaione_Education.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notification> list;
    Context ctx;

    public NotificationAdapter(List<Notification> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout,parent,false);
        return new NotificationAdapter.ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Notification notification=list.get(position);
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.NOTIFICATION+notification.getImage()).placeholder(R.drawable.logo).into(holder.icon);
        holder.title.setText(notification.getTitle());
        holder.date.setText(notification.getSince());
        if(notification.getLink().isEmpty())
        {
            holder.showMore.setVisibility(View.GONE);
        }
        else
        {
            holder.showMore.setVisibility(View.VISIBLE);
            holder.showMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = notification.getLink();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    ctx.startActivity(i);
                    //openChromeCustomeTab(notification.getLink());
                }
            });
        }
        Log.e("sdas",notification.getTitle());
        holder.showText.setText(notification.getTitle().charAt(0) + "");

        /*holder.showText.setTextColor(currentColor);*/
        holder.desc.setText(notification.getMessage());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title,desc,date,showText, showMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon=itemView.findViewById(R.id.icon);
            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.description);
            date=itemView.findViewById(R.id.date);
            showText=itemView.findViewById(R.id.show_text);
            showMore=itemView.findViewById(R.id.show_more);

        }
    }
    public void openChromeCustomeTab(String uri){
        CustomTabsIntent.Builder builder=new CustomTabsIntent.Builder();
        CustomTabsIntent intent=builder.build();
        intent.launchUrl(ctx, Uri.parse( uri ) );
    }
}
