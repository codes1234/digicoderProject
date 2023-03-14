package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.databinding.QuestionDesignBinding;
import digi.coders.Qaione_Education.models.AnsBook;
import digi.coders.Qaione_Education.models.Questio;
import digi.coders.Qaione_Education.models.QuestionData;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyHolder> {
    private Questio questions;
    private int questionno;
    private Context ctx;
    private GetOption getOption;
    private AnsBook[] ansBooks;
    private int status;
    int no;
    public QuestionAdapter(Questio questions, int questionno) {
        this.questions = questions;
        this.questionno = questionno;
    }
    public QuestionAdapter(AnsBook[] ansBooks , int staus) {
        this.ansBooks = ansBooks;
        this.status=staus;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_design,parent,false);
        return new MyHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull  MyHolder holder, int position) {

        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);

        no=position+1;
        if(status==1)
        {
            holder.binding.aOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.white));
            holder.binding.bOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.white));
            holder.binding.cOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.white));
            holder.binding.dOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.white));
            holder.binding.aoptionText.setTextColor(ctx.getResources().getColor(R.color.black));
            holder.binding.aOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.navy_blue)));
            holder.binding.boptionText.setTextColor(ctx.getResources().getColor(R.color.black));
            holder.binding.bOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.navy_blue)));
            holder.binding.coptionText.setTextColor(ctx.getResources().getColor(R.color.black));
            holder.binding.cOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.navy_blue)));
            holder.binding.doptionText.setTextColor(ctx.getResources().getColor(R.color.black));
            holder.binding.dOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.navy_blue)));
            Log.e("sdd","status1");
            AnsBook ansBook=ansBooks[position];
            QuestionData questions=ansBook.getQuestionData();
            if(questions.getAnswerType().equals("Text"))
            {
                holder.binding.quesNo.setText("Ques no: "+no);
                holder.binding.questio.setText(questions.getQuestion());
                holder.binding.aoptionText.setText(questions.getA());
                holder.binding.boptionText.setText(questions.getB());
                holder.binding.coptionText.setText(questions.getC());
                holder.binding.doptionText.setText(questions.getD());
                holder.binding.aoptionText.setVisibility(View.VISIBLE);
                holder.binding.boptionText.setVisibility(View.VISIBLE);
                holder.binding.coptionText.setVisibility(View.VISIBLE);
                holder.binding.doptionText.setVisibility(View.VISIBLE);
                holder.binding.aoptionImageLayout.setVisibility(View.GONE);
                holder.binding.boptionImageLayout.setVisibility(View.GONE);
                holder.binding.coptionImageLayout.setVisibility(View.GONE);
                holder.binding.doptionImageLayout.setVisibility(View.GONE);

            }
            else
            {
                //photo work

                holder.binding.quesNo.setText("Ques no: "+no);
                holder.binding.questio.setText(questions.getQuestion());
                Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.QUESTION+questions.getA()).placeholder(shimmerDrawable).into(holder.binding.aOptionImage);
                Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.QUESTION+questions.getB()).placeholder(shimmerDrawable).into(holder.binding.boptionImage);
                Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.QUESTION+questions.getC()).placeholder(shimmerDrawable).into(holder.binding.coptionImage);
                Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.QUESTION+questions.getD()).placeholder(shimmerDrawable).into(holder.binding.doptionImage);

                holder.binding.aoptionText.setVisibility(View.GONE);
                holder.binding.boptionText.setVisibility(View.GONE);
                holder.binding.coptionText.setVisibility(View.GONE);
                holder.binding.doptionText.setVisibility(View.GONE);
                holder.binding.aoptionImageLayout.setVisibility(View.VISIBLE);
                holder.binding.boptionImageLayout.setVisibility(View.VISIBLE);
                holder.binding.coptionImageLayout.setVisibility(View.VISIBLE);
                holder.binding.doptionImageLayout.setVisibility(View.VISIBLE);
            }
            if(questions.equals(ansBook.getAnsOption()))
            {

                if(ansBook.getAnsOption().equals("a"))
                {
                    holder.binding.aOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.green));
                    holder.binding.aoptionText.setTextColor(ctx.getResources().getColor(R.color.white));
                    holder.binding.aOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.white)));
                }
                else if(ansBook.getAnsOption().equals("b"))
                {
                    holder.binding.bOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.green));
                    holder.binding.boptionText.setTextColor(ctx.getResources().getColor(R.color.white));
                    holder.binding.bOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.white)));
                }
                else if(ansBook.getAnsOption().equals("c"))
                {
                    holder.binding.cOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.green));
                    holder.binding.coptionText.setTextColor(ctx.getResources().getColor(R.color.white));
                    holder.binding.cOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.white)));

                }
                else
                {
                    holder.binding.dOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.green));
                    holder.binding.doptionText.setTextColor(ctx.getResources().getColor(R.color.white));
                    holder.binding.dOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.white)));

                }

                holder.binding.aOptionLayout.setClickable(false);
                holder.binding.bOptionLayout.setClickable(false);
                holder.binding.cOptionLayout.setClickable(false);
                holder.binding.dOptionLayout.setClickable(false);


            }
            else
            {

                if (ansBook.getAnsOption().equals("a")) {
                    holder.binding.aOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.red));
                    holder.binding.aoptionText.setTextColor(ctx.getResources().getColor(R.color.white));
                    holder.binding.aOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.white)));
                } else if (ansBook.getAnsOption().equals("b")) {
                    holder.binding.bOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.red));
                    holder.binding.boptionText.setTextColor(ctx.getResources().getColor(R.color.white));
                    holder.binding.bOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.white)));

                } else if (ansBook.getAnsOption().equals("c")) {
                    holder.binding.cOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.red));
                    holder.binding.coptionText.setTextColor(ctx.getResources().getColor(R.color.white));
                    holder.binding.cOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.white)));

                } else if(ansBook.getAnsOption().equals("d")) {
                    holder.binding.dOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.red));
                    holder.binding.doptionText.setTextColor(ctx.getResources().getColor(R.color.white));
                    holder.binding.dOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.white)));

                }

                holder.binding.aOptionLayout.setClickable(false);
                holder.binding.bOptionLayout.setClickable(false);
                holder.binding.cOptionLayout.setClickable(false);
                holder.binding.dOptionLayout.setClickable(false);
                if (questions.getAnswer().equals("a")) {
                    holder.binding.aOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.green));
                    holder.binding.aoptionText.setTextColor(ctx.getResources().getColor(R.color.white));
                    holder.binding.aOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.white)));
                } else if (questions.getAnswer().equals("b")) {
                    holder.binding.bOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.green));
                    holder.binding.boptionText.setTextColor(ctx.getResources().getColor(R.color.white));
                    holder.binding.bOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.white)));
                }
                else if (questions.getAnswer().equals("c")) {
                    holder.binding.cOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.green));
                    holder.binding.coptionText.setTextColor(ctx.getResources().getColor(R.color.white));
                    holder.binding.cOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.white)));

                } else if(questions.getAnswer().equals("d")) {
                    holder.binding.dOptionLayout.setCardBackgroundColor(ctx.getResources().getColor(R.color.green));
                    holder.binding.doptionText.setTextColor(ctx.getResources().getColor(R.color.white));
                    holder.binding.dOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(ctx.getResources().getColor(R.color.white)));
                }
                
            }



            //getOption.click(holder.binding,position);
            //no++;
        }
        else
        {
            Log.e("sdd","other");

            if(questions.getAnswerType().equals("Text"))
            {
                holder.binding.quesNo.setText("Ques no: "+questionno);
                holder.binding.questio.setText(questions.getQuestion());

                holder.binding.aoptionText.setText(questions.getA());
                holder.binding.boptionText.setText(questions.getB());
                holder.binding.coptionText.setText(questions.getC());
                holder.binding.doptionText.setText(questions.getD());

                holder.binding.aoptionText.setVisibility(View.VISIBLE);
                holder.binding.boptionText.setVisibility(View.VISIBLE);
                holder.binding.coptionText.setVisibility(View.VISIBLE);
                holder.binding.doptionText.setVisibility(View.VISIBLE);
                holder.binding.aoptionImageLayout.setVisibility(View.GONE);
                holder.binding.boptionImageLayout.setVisibility(View.GONE);
                holder.binding.coptionImageLayout.setVisibility(View.GONE);
                holder.binding.doptionImageLayout.setVisibility(View.GONE);

            }
            else
            {
                //photo work


                holder.binding.quesNo.setText("Ques no: "+questionno);
                holder.binding.questio.setText(questions.getQuestion());

                Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.QUESTION+questions.getA()).placeholder(shimmerDrawable).into(holder.binding.aOptionImage);
                Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.QUESTION+questions.getB()).placeholder(shimmerDrawable).into(holder.binding.boptionImage);
                Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.QUESTION+questions.getC()).placeholder(shimmerDrawable).into(holder.binding.coptionImage);
                Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.QUESTION+questions.getD()).placeholder(shimmerDrawable).into(holder.binding.doptionImage);

                holder.binding.aoptionText.setVisibility(View.GONE);
                holder.binding.boptionText.setVisibility(View.GONE);
                holder.binding.coptionText.setVisibility(View.GONE);
                holder.binding.doptionText.setVisibility(View.GONE);
                holder.binding.aoptionImageLayout.setVisibility(View.VISIBLE);
                holder.binding.boptionImageLayout.setVisibility(View.VISIBLE);
                holder.binding.coptionImageLayout.setVisibility(View.VISIBLE);
                holder.binding.doptionImageLayout.setVisibility(View.VISIBLE);
            }

            getOption.click(holder.binding,position);
        }

    }


    public void getSelectedOption(GetOption getOption) {
        this.getOption = getOption;

    }

    public interface  GetOption
    {
        void click(QuestionDesignBinding binding, int position);
    }
    @Override
    public int getItemCount() {
        if(status==1)
        {
        return ansBooks.length;
        }
        else {
            return 1;
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        QuestionDesignBinding binding;
        public MyHolder(@NonNull  View itemView) {
            super(itemView);
            binding=QuestionDesignBinding.bind(itemView);
        }
    }
}
