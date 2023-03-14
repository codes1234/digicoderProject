package digi.coders.Qaione_Education.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import digi.coders.Qaione_Education.Adapters.QuzListAdapter;
import digi.coders.Qaione_Education.Helper.QuizDetails;
import digi.coders.Qaione_Education.databinding.FragmentUnAttemptedBinding;
import digi.coders.Qaione_Education.models.QuizData;
import digi.coders.Qaione_Education.models.QuizListData;
import digi.coders.Qaione_Education.singletask.SingleTask;

public class UnAttemptedFragment extends Fragment {

    FragmentUnAttemptedBinding binding;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentUnAttemptedBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //loadUpcomingQuizes

        loadUpcomingQuizes();
    }

    private void loadUpcomingQuizes() {
        progressDialog.show();
        QuizDetails quizDetails=new QuizDetails((SingleTask)getActivity().getApplication());
        quizDetails.getQuiz();
        quizDetails.getQuizData(new QuizDetails.QuizFetch() {
            @Override
            public void getQuiz(QuizData quizData, String message) {

                try {
                    QuizListData[] quizsList=quizData.getUnattemptedList();
                    if(quizData!=null && quizsList.length>0)
                    {
                        progressDialog.hide();
//                    binding.progressBar.setVisibility(View.GONE);
                        binding.upcomingQuizList.setVisibility(View.VISIBLE);
                        binding.upcomingQuizList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        binding.upcomingQuizList.setAdapter(new QuzListAdapter(1,quizsList));
                    }
                    else
                    {
                        progressDialog.hide();
                        //                  binding.progressBar.setVisibility(View.GONE);
                        binding.noQuizLine.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    progressDialog.hide();
                    binding.noQuizLine.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }

}