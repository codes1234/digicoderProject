package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Reply;

public class ReplayAdapter extends RecyclerView.Adapter<ReplayAdapter.MyHolder> {

    private Reply[] replies;
    private Context ctx;
    MyInterface myInterface;
    public ReplayAdapter(Reply[] replies) {
        this.replies = replies;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_design,parent,false);
        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Reply reply=replies[position];
        holder.replyLayout.setVisibility(View.VISIBLE);
        holder.replyMessage.setText(reply.getMessage());
        holder.replyUserName.setText(reply.getUser().getName());
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.PROFILE_PHOTO+reply.getUser().getProfilePhoto()).placeholder(R.drawable.user).into(holder.replyUserPic);

    }


    public void getPosition(MyInterface myInterface)
    {
        this.myInterface=myInterface;
    }
    public interface  MyInterface
    {
        void click(int position);
    }

    @Override
    public int getItemCount() {
        return replies.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView replyUserName,replyMessage;
        CircleImageView replyUserPic;
        LinearLayout replyLayout;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            replyUserPic=itemView.findViewById(R.id.replyUserPic);
            replyLayout=itemView.findViewById(R.id.replayLayout);
            replyUserName=itemView.findViewById(R.id.replyUserName);
            replyMessage=itemView.findViewById(R.id.replyMessage);
        }
    }
}
