package digi.coders.Qaione_Education.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.databinding.ItemQuestionAnalysisBinding;
import digi.coders.Qaione_Education.databinding.QuestionDesignBinding;
import digi.coders.Qaione_Education.models.AnsBook;
import digi.coders.Qaione_Education.models.Questio;

public class QuestionAnalysisAdapter extends RecyclerView.Adapter<QuestionAnalysisAdapter.MyHolder> {
    private Questio questions;
    private int questionno;
    private Context ctx;
    private GetOption getOption;
    private AnsBook[] ansBooks;
    private int status;
    int no;
    public QuestionAnalysisAdapter(Questio questions, int questionno) {
        this.questions = questions;
        this.questionno = questionno;
    }
    public QuestionAnalysisAdapter(AnsBook[] ansBooks , int staus) {
        this.ansBooks = ansBooks;
        this.status=staus;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_analysis,parent,false);
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

        no = position + 1;
        holder.binding.quesNo.setText(no+"");


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
        return ansBooks.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        ItemQuestionAnalysisBinding binding;
        public MyHolder(@NonNull  View itemView) {
            super(itemView);
            binding=ItemQuestionAnalysisBinding.bind(itemView);
        }
    }
}
