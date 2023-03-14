package digi.coders.Qaione_Education.Fragments;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.Qaione_Education.Adapters.CoursesByCategoryAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Courses;
import digi.coders.Qaione_Education.models.Ebook;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCoursesFragment extends Fragment {

    private RecyclerView courses;
    private LinearLayout noItem;
    private TextView noCourseTxt;
    private SingleTask singleTask;
    private List<Courses> coursesList;
    private List<Ebook> ebooksList;
    private ShimmerFrameLayout shimmer;
    private NestedScrollView lay;
    private int status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_courses, container, false);

        singleTask=(SingleTask)getActivity().getApplication();
        courses=view.findViewById(R.id. course);
        shimmer=view.findViewById(R.id.shimmer_view_containerc);
        lay=view.findViewById(R.id.layout13);
        noItem=view.findViewById(R.id.noItem);
        noCourseTxt=view.findViewById(R.id.no_course_txt);
        loadCategoryCourses();

        return view;
    }

    private void loadCategoryCourses() {
        Log.e("course","course call");
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call;

        if(user!=null)
        {
            call = myApi.getAllCourses(user.getId(),user.getNumber(),"");

        }
        else
        {
            call = myApi.getAllCourses("","","");

        }

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    setResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });


    }

    private void setResponse(Response<JsonArray> response) {
        int i;
        try {

            Log.e("educatorres", response.body().toString());
            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            String res=jsonObject.getString("res");
            String msg=jsonObject.getString("msg");
            coursesList=new ArrayList<>();
            if(res.equals("success"))
            {
                Courses course = null;
                JSONArray jsonArray1=jsonObject.getJSONArray("data");

                /*if(jsonArray1.length()==0)
                {
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);
                    noCourseTxt.setVisibility(View.VISIBLE);
                }
                else {*/
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                lay.setVisibility(View.VISIBLE);
                for(i=0;i<jsonArray1.length();i++)
                {
                    course=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Courses.class);
                    coursesList.add(course);
                }
                setResponseInRecycler(coursesList);

            }
            else
            {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                noItem.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setResponseInRecycler(List<Courses> coursesList) {
        ebooksList=new ArrayList<>();
        Log.e("sads","course set");
        courses.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        CoursesByCategoryAdapter coursesByCategoryAdapter=new CoursesByCategoryAdapter(coursesList,getActivity(),1,ebooksList);
        courses.setAdapter(coursesByCategoryAdapter);

    }

}