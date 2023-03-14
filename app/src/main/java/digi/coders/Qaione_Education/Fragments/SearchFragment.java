package digi.coders.Qaione_Education.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import digi.coders.Qaione_Education.Adapters.CoursesAdapter;
import digi.coders.Qaione_Education.Adapters.SearchItemAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Courses;
import digi.coders.Qaione_Education.models.SearchItem;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    EditText search;
    LinearLayout categorylay;
    TextView noresult,searchHere,trendingTxt;
    private SingleTask singleTask;
    private  List<Courses> coursesList;
    private List<SearchItem> searchItemList;
    private RecyclerView searchList,trendingCourses;
    private ProgressBar searchProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.search_fragment, container, false);

        noresult=view.findViewById(R.id.noresult);
        search=view.findViewById(R.id.serach);
        searchList=view.findViewById(R.id.search_item);
        trendingCourses=view.findViewById(R.id.trending_courses);
        categorylay=view.findViewById(R.id.categorylay);
        searchProgress=view.findViewById(R.id.searc_progress);
        searchHere=view.findViewById(R.id.search_here);
        trendingTxt=view.findViewById(R.id.trending);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchList.setVisibility(View.GONE);
                trendingTxt.setVisibility(View.GONE);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchHere.setVisibility(View.GONE);
                if (s.length() > 2) {
                    searchProgress.setVisibility(View.VISIBLE);
                    noresult.setVisibility(View.GONE);
                    trendingTxt.setVisibility(View.GONE);
                    trendingCourses.setVisibility(View.GONE);
                    searchCourses(s);
                }
                else
                {
                    searchProgress.setVisibility(View.GONE);
                    noresult.setVisibility(View.GONE);
                    searchHere.setVisibility(View.VISIBLE);


                }
                categorylay.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void searchCourses(CharSequence s) {
        String json=singleTask.getValue("user");
        User user=new Gson().fromJson(json,User.class);
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call;
        if(user!=null)
        {
            call = myApi.search(s.toString(),user.getNumber(),user.getId());
        }
        else {
            call = myApi.search(s.toString(),"","");
        }



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
                            searchHere.setVisibility(View.GONE);
                            searchProgress.setVisibility(View.GONE);
                            int i;
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            searchItemList=new ArrayList<>();
                            for(i=0;i<jsonArray1.length();i++)
                            {
                                SearchItem searchItem=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),SearchItem.class);
                                searchItemList.add(searchItem);
                            }
                            setInRecycler(searchItemList);
                            noresult.setVisibility(View.GONE);
                            trendingCourses.setVisibility(View.GONE);
                        }
                        else
                        {
                            searchHere.setVisibility(View.GONE);
                            noresult.setVisibility(View.VISIBLE);
                            trendingTxt.setVisibility(View.VISIBLE);
                            searchList.setVisibility(View.GONE);
                            searchProgress.setVisibility(View.GONE);
                            loadTrendinCourses();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("err",t.getMessage());
            }
        });

    }

    private void loadTrendinCourses() {
        MyApi myApi=RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call;
        String json=singleTask.getValue("user");
        User user=new Gson().fromJson(json,User.class);
        if(user!=null) {
            call = myApi.getTrendingCourses(user.getId(),user.getNumber());
        }
        else
        {
            call = myApi.getTrendingCourses("","");
        }
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
                        if(res.equalsIgnoreCase("success"))
                        {
                            trendingTxt.setVisibility(View.VISIBLE);
                            coursesList=new ArrayList<>();
                            Courses courses;
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                courses=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Courses.class);
                                coursesList.add(courses);

                            }
                            setInRecyclerTendingCourses(coursesList);
                        }
                        Log.e("tredning",msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("trendingcourses",t.getMessage());
            }
        });

    }

    private void setInRecyclerTendingCourses(List<Courses> coursesList) {
        trendingCourses.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        trendingCourses.setLayoutManager(layoutManager);
        trendingCourses.setHasFixedSize(true);
        CoursesAdapter corsesAdapter=new CoursesAdapter(getActivity(),0,coursesList);
        trendingCourses.setAdapter(corsesAdapter);

    }

    private void setInRecycler(List<SearchItem> searchItemList) {
        /*InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);*/
        searchList.setVisibility(View.VISIBLE);
        searchList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        searchList.setAdapter(new SearchItemAdapter(searchItemList));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        singleTask=(SingleTask)getActivity().getApplication();

        loadTrendinCourses();
        search.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        search.setInputType(InputType.TYPE_CLASS_TEXT);
    }

   /* @Override
    public void onDestroy() {
        super.onDestroy();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        //search.setInputType(InputType.TYPE_CLASS_TEXT);
    }*/

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

//        search.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    @Override
    public void onResume() {
        super.onResume();
        search.setText("");
    }

    @Override
    public void onStart() {
        super.onStart();
        search.setText("");
    }
}
