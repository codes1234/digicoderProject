package digi.coders.Qaione_Education.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Question;
import digi.coders.Qaione_Education.models.Reply;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionAnswerAdapter extends RecyclerView.Adapter<QuestionAnswerAdapter.MyHolder> {


    private Question[] questions;
    private Context ctx;
    private SingleTask singleTask;
    //public static VideoPlaylist videoPlaylist;

    public QuestionAnswerAdapter(Question[] questions, SingleTask singleTask) {
        this.questions = questions;
        this.singleTask = singleTask;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_answer_design,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Question question=questions[position];
        if(question.getReplyStatus().equals("true"))
        {
            Reply[] replies=question.getReply();
            holder.replyButton.setVisibility(View.GONE);
            holder.repl.setVisibility(View.GONE);
            holder.replyList.setLayoutManager(new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false));
            holder.replyList.setAdapter(new ReplayAdapter(replies));

        }
       /* Reply[] replies=question.getReply();
        Reply reply=replies[position];*/
        /*if(question.getReplyStatus().equals("true"))
        {
            reply = question.getReply()[position];
            holder.replyLayout.setVisibility(View.VISIBLE);
            holder.replyMessage.setText(reply.getMessage());
            holder.replyButton.setVisibility(View.GONE);
            //holder.replyUserName.setText(reply.getUser().getName());
        }*/
        User user=question.getUser();
        Log.e("sdsd",user.getName()+"s");
        if(!user.getName().isEmpty()) {
            holder.userName.setText(user.getName());
        }
        holder.userQues.setText(question.getMessage());

        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.PROFILE_PHOTO+question.getUser().getProfilePhoto()).placeholder(R.drawable.user).into(holder.userPic);
        holder.replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*

        MyApi myApi=RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        myApi.postQuestion(videoPlaylist.getCourse().getId(),user.getId(),videoPlaylist.getVideo().getId(),)

*/


                //openBottom(holder,ctx,singleTask,question);
                Toast.makeText(ctx, "Your cann't reply only post question.", Toast.LENGTH_SHORT).show();
            }
        });






    }

    private void openBottom(MyHolder holder, Context ctx, SingleTask singleTask, Question question) {

        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        ProgressDialog progressDialog=new ProgressDialog(ctx);
        progressDialog.setMessage("Waiting");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(ctx,R.style.myBottomSheetDialogTheme);
        View view1= LayoutInflater.from(ctx).inflate(R.layout.post_question_answer_bottom_layout,(RelativeLayout)holder.itemView.findViewById(R.id.bottomshet),false);
        EditText text=view1.findViewById(R.id.question);
        ImageView send=view1.findViewById(R.id.sendText);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String message=text.getText().toString();
                String js=singleTask.getValue("user");
                User user=new Gson().fromJson(js,User.class);
                MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
                if(TextUtils.isEmpty(message))
                {
                    Toast.makeText(ctx, "Please Write your Question here....", Toast.LENGTH_SHORT).show();
                }
                else {


                    Call<JsonArray> call=myApi.postQuestion("", user.getId(), "", message,question.getId());
                    //Call<JsonArray> call=myApi.postQuestion(videoPlaylist.getCourse().getId(), user.getId(), videoPlaylist.getVideo().getId(), message,question.getId());
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
                                        Reply[] replies=question.getReply();
                                        holder.replyList.setLayoutManager(new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false));
                                        ReplayAdapter replayAdapter=new ReplayAdapter(replies);
                                        holder.replyList.setAdapter(replayAdapter);
                                        onBindViewHolder(holder,holder.getAdapterPosition());
                                        replayAdapter.notifyItemInserted(holder.getAdapterPosition());
                                        holder.replyButton.setVisibility(View.GONE);
                                        holder.repl.setVisibility(View.GONE);


                                        /*Toast.makeText(PlayVideoActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        loadAssignment();*/
                                        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();

                                        Log.e("sdsd",question.getReplyStatus());
                                        Log.e("sdsd",question.getReply().length+"");
                                        /*holder.replyLayout.setVisibility(View.VISIBLE);
                                        holder.replyButton.setVisibility(View.GONE);

                                        holder.replyMessage.setText(reply.getMessage());*/
                                        //holder.replyUserName.setText(question.getUser().getName());
                                   /*     notifyDataSetChanged();*/

                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                            InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        }
                                        bottomSheetDialog.dismiss();
                                        progressDialog.hide();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            Log.e("sdsd",t.getMessage());
                            progressDialog.show();
                        }
                    });



                }





            }

        });
        text.requestFocus();
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();

    }





    @Override
    public int getItemCount() {
        /*if(questions.length<0)
        {
            return 1;

        }
        else {

        }
*/
        return questions.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {

         ImageView threeDots;
         ImageView userPic,replayUserPic;
        TextView userName,userQues,replyUserName,replyMessage;
         Button replyButton;
         LinearLayout replyLayout,repl;
         private RecyclerView replyList;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            userPic=itemView.findViewById(R.id.quserpic);
            userName=itemView.findViewById(R.id.quname);
            userQues=itemView.findViewById(R.id.quesmess);
            replyButton=itemView.findViewById(R.id.reply_btn);
            replyList=itemView.findViewById(R.id.answerlist);
            replyLayout=itemView.findViewById(R.id.replayLayout);
            replyUserName=itemView.findViewById(R.id.replyUserName);
            replyMessage=itemView.findViewById(R.id.replyMessage);
            repl=itemView.findViewById(R.id.repl);

        }
    }

}
