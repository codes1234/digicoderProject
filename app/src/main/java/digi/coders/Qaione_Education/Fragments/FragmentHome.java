package digi.coders.Qaione_Education.Fragments;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cashfree.pg.CFPaymentService;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.rd.PageIndicatorView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import digi.coders.Qaione_Education.Activities.CategoryCoursesActivity;
import digi.coders.Qaione_Education.Activities.CourseDetailsActivity;
import digi.coders.Qaione_Education.Activities.EbookDetailActivity;
import digi.coders.Qaione_Education.Activities.HomeActivity;
import digi.coders.Qaione_Education.Activities.OtpActivity;
import digi.coders.Qaione_Education.Activities.Payment;
import digi.coders.Qaione_Education.Adapters.CategoryCourseAdapter;
import digi.coders.Qaione_Education.Adapters.CoursesAdapter;
import digi.coders.Qaione_Education.Adapters.EducatorsAdapter;
import digi.coders.Qaione_Education.Adapters.RecommendedVideosAdapter;
import digi.coders.Qaione_Education.Adapters.ViewPagerAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.CategoriesModel;
import digi.coders.Qaione_Education.models.Courses;
import digi.coders.Qaione_Education.models.EducatorModel;
import digi.coders.Qaione_Education.models.RecommendedVideos;
import digi.coders.Qaione_Education.models.Slider;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {

    ViewPager viewPager;
    PageIndicatorView indicator;
    int currentPage = 0;
    Timer timer;
    int DELAY_MS = 3000, PERIOD_MS = 3000;
    Runnable runnable;
    Handler handler;
    String[] SliderNo;
    private List<Slider> sliderList;
    ImageView banner1, banner2;


    private SingleTask singleTask;
    //private ArrayList<String> photos;
    private List<CategoriesModel> listOfCategories;
    private List<EducatorModel> listOfEducator;
    private List<Courses> coursesList;
    private List<RecommendedVideos> recommendedVideosList;
    private ShimmerFrameLayout shimmer;
    private NestedScrollView layout;
    int length = 3;
    private TextView tendingTxt, topTxt, latestCourseTxt, courseByCategoryTxt, ebookByCateoryTxt, educator_txt, recommendedVideoTxt;
    RecyclerView trending_courses, latest_corses, top_corses, courseCategory, courseCategory1, category_educators, recommendedVideos;

    String banner_1_parameter, banner_1_link, banner_2_parameter, banner_2_link;

    CardView takeAdmission_card;
    private static final String TAG = FragmentHome.class.getSimpleName();
    public static String order_id;
    private String admission_fee;

    String token="", app_id="", mode="";
    String txStatus;
    String referenceId;
    String txMsg;
    String txTime;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        loadSlider();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                if (currentPage == SliderNo.length) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();

        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin_overlap_payment));
        viewPager.setOffscreenPageLimit(5);
        indicator.setViewPager(viewPager);
        viewPager.setOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                        indicator.setSelected(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );

        loadProfileDetails();
        Admission();
        loadTrendingCourses();
        loadCategories();
        loadEducator();
        loadTopCourses();
        loadLatestCourses();
        loadRecommendedVideos();
        getBanners();

       /* banner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(banner_1_parameter.equals("External"))
                {
                    openChromeCustomeTab( banner_1_link );
                }
                else if(banner_1_parameter.equals("Category"))
                {
                    Intent in=new Intent(getActivity(), CategoryCoursesActivity.class);
                    in.putExtra("id",banner_1_link);
                    in.putExtra("title","");
                    startActivity(in);
                }
                else if(banner_1_parameter.equals("Course"))
                {
                    Intent in = new Intent(getActivity(), CourseDetailsActivity.class);
                    in.putExtra("courseid", banner_1_link);
                    startActivity(in);
                }
                else if(banner_1_parameter.equals("Ebook"))
                {
                    Intent in=new Intent(getActivity(), EbookDetailActivity.class);
                    in.putExtra("ebookid",banner_1_link);
                    startActivity(in);
                }
                else if(banner_1_parameter.equals("LiveSession"))
                {
//                            Intent intent = new Intent(ctx, LiveSessionDetailsActivity.class);
//                            //LiveSessionDetailsActivity.liveSession = liveSession1;
//                            ctx.startActivity(intent);
                }
                else if(banner_1_parameter.equals("None"))
                {

                }
            }
        });

        banner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(banner_2_parameter.equals("External"))
                {
                    openChromeCustomeTab( banner_2_link );
                }
                else if(banner_2_parameter.equals("Category"))
                {
                    Intent in=new Intent(getActivity(), CategoryCoursesActivity.class);
                    in.putExtra("id",banner_2_link);
                    in.putExtra("title","");
                    startActivity(in);
                }
                else if(banner_2_parameter.equals("Course"))
                {
                    Intent in = new Intent(getActivity(), CourseDetailsActivity.class);
                    in.putExtra("courseid", banner_2_link);
                    startActivity(in);
                }
                else if(banner_2_parameter.equals("Ebook"))
                {
                    Intent in=new Intent(getActivity(), EbookDetailActivity.class);
                    in.putExtra("ebookid",banner_2_link);
                    startActivity(in);
                }
                else if(banner_2_parameter.equals("LiveSession"))
                {
//                            Intent intent = new Intent(ctx, LiveSessionDetailsActivity.class);
//                            //LiveSessionDetailsActivity.liveSession = liveSession1;
//                            ctx.startActivity(intent);
                }
                else if(banner_2_parameter.equals("None"))
                {

                }
            }
        });*/

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("sd", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        Log.d("sads", token + "");
                        sendRegistrationToServer(token);
                    }
                });

        takeAdmission_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInputParams();
            }
        });
    }

    private void loadRecommendedVideos() {

        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.getRecommendedVideos("");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {

                        Log.i("recommended", response.body().toString());
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("msg");
                        if (res.equals("success")) {
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            int i;
                            recommendedVideosList = new ArrayList<>();
                            for (i = 0; i < jsonArray1.length(); i++) {
                                RecommendedVideos recommendedVideos = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), RecommendedVideos.class);
                                recommendedVideosList.add(recommendedVideos);

                            }
                            setInRecommendedVideoRecycler(recommendedVideosList);

                        } else {
                            recommendedVideoTxt.setVisibility(View.GONE);
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

    private void setInRecommendedVideoRecycler(List<RecommendedVideos> recommendedVideosList) {
        recommendedVideos.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recommendedVideos.setAdapter(new RecommendedVideosAdapter(recommendedVideosList));

    }

    private void sendRegistrationToServer(String token) {
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.saveToken(user.getId(), token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {

                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("msg");
                        Log.e("ds", res + msg);

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

    private void loadCategories() {
        loadCategoriesApi();
    }

    private void loadCategoriesApi() {
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.getCategories();

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    setCategoryResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    private void setCategoryResponse(Response<JsonArray> response) {
        try {
            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String res = jsonObject.getString("res");
            String msg = jsonObject.getString("msg");
            if (res.equalsIgnoreCase("success")) {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                CategoriesModel categoriesModel;
                listOfCategories = new ArrayList<>();
                JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    categoriesModel = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), CategoriesModel.class);
                    listOfCategories.add(categoriesModel);

                }
                setInRecyclerCategories(listOfCategories);

            } else {
                courseByCategoryTxt.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void setInRecyclerCategories(List<CategoriesModel> listOfCategories) {
        RecyclerView.LayoutManager layoutManager31 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        courseCategory1.setLayoutManager(layoutManager31);
        courseCategory1.setHasFixedSize(true);
        CategoryCourseAdapter corsesAdapter31 = new CategoryCourseAdapter(getActivity(), listOfCategories, 1);
        courseCategory1.setAdapter(corsesAdapter31);

        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        courseCategory.setLayoutManager(layoutManager3);
        courseCategory.setHasFixedSize(true);
        CategoryCourseAdapter courseAdapter1 = new CategoryCourseAdapter(getActivity(), listOfCategories, 2);
        courseCategory.setAdapter(courseAdapter1);


    }

    private void loadLatestCourses() {
        loadLatestCoursesApi();
    }

    private void loadLatestCoursesApi() {
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        Call<JsonArray> call;
        if (user != null) {
            call = myApi.getLatestCourses(user.getId(), user.getNumber());
        } else {
            call = myApi.getLatestCourses("", "");
        }

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    setLatestCourseResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

    }

    private void setLatestCourseResponse(Response<JsonArray> response) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String res = jsonObject.getString("res");
            String msg = jsonObject.getString("msg");
            if (res.equalsIgnoreCase("success")) {
                Courses courses;
                coursesList = new ArrayList<>();

                JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    courses = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Courses.class);
                    coursesList.add(courses);

                }
//                if(jsonArray1.length()>3)
//                {
//                    for(int i=0;i<length;i++)
//                    {
//                        courses=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Courses.class);
//                        coursesList.add(courses);
//
//                    }
//                }
//                else
//                {
//                    for(int i=0;i<jsonArray1.length();i++)
//                    {
//                        courses=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Courses.class);
//                        coursesList.add(courses);
//
//                    }
//
//                }

                setInRecyclerLatestCourses(coursesList);
            } else {
                latestCourseTxt.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setInRecyclerLatestCourses(List<Courses> coursesList) {
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        latest_corses.setLayoutManager(layoutManager2);
        latest_corses.setHasFixedSize(true);
        CoursesAdapter corsesAdapter2 = new CoursesAdapter(getActivity(), 2, coursesList);
        latest_corses.setAdapter(corsesAdapter2);
    }

    private void loadTrendingCourses() {
        loadTrendingCoursesApi();

    }

    private void loadTrendingCoursesApi() {
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);

        Call<JsonArray> call;
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        if (user != null) {
            call = myApi.getTrendingCourses(user.getId(), user.getNumber());
        } else {
            call = myApi.getTrendingCourses("", "");
        }
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    setTrendingResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("trendingcourses", t.getMessage());
            }
        });
    }

    private void setTrendingResponse(Response<JsonArray> response) {
        try {
            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String res = jsonObject.getString("res");
            String msg = jsonObject.getString("msg");
            if (res.equalsIgnoreCase("success")) {
                coursesList = new ArrayList<>();
                Courses courses;
                JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    courses = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Courses.class);
                    coursesList.add(courses);
                    setInRecyclerTendingCourses(coursesList);
                }
//                if(jsonArray1.length()>3)
//                {
//
//                    for(int i=0;i<length;i++)
//                    {
//                        courses=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Courses.class);
//                        coursesList.add(courses);
//                        setInRecyclerTendingCourses(coursesList);
//                    }
//                }
//                else
//                {
//                    for(int i=0;i<jsonArray1.length();i++)
//                    {
//                        courses=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Courses.class);
//                        coursesList.add(courses);
//                        setInRecyclerTendingCourses(coursesList);
//                    }
//                }

            } else {
                tendingTxt.setVisibility(View.GONE);
            }
            Log.e("tredning", msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setInRecyclerTendingCourses(List<Courses> coursesList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        trending_courses.setLayoutManager(layoutManager);
        trending_courses.setHasFixedSize(true);
        CoursesAdapter corsesAdapter = new CoursesAdapter(getActivity(), 1, coursesList);
        trending_courses.setAdapter(corsesAdapter);
    }

    private void loadTopCourses() {
        loadTopCoursesApi();

    }

    private void loadTopCoursesApi() {
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        Call<JsonArray> call;
        if (user != null) {
            call = myApi.getTopCourses(user.getId(), user.getNumber());
        } else {
            call = myApi.getTopCourses("", "");
        }

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    setTopCourseResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("topcousesexception", t.getMessage());

            }
        });
    }

    private void setTopCourseResponse(Response<JsonArray> response) {
        try {
            Log.i("top", response.body().toString());
            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String res = jsonObject.getString("res");
            String msg = jsonObject.getString("msg");

            if (res.equalsIgnoreCase("success")) {
                coursesList = new ArrayList<>();
                JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                Courses courses;
                for (int i = 0; i < jsonArray1.length(); i++) {
                    courses = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Courses.class);
                    coursesList.add(courses);
                }
                setInRecyclerTopCourses(coursesList);

//                if(jsonArray1.length()>3)
//                {
//                    for(int i=0;i<length;i++)
//                    {
//                        courses=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Courses.class);
//                        coursesList.add(courses);
//                    }
//                }
//                else
//                {
//                    for(int i=0;i<jsonArray1.length();i++)
//                    {
//                        courses=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Courses.class);
//                        coursesList.add(courses);
//                    }
//
//                }

            } else {
                topTxt.setVisibility(View.GONE);
            }
            Log.e("sds", msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setInRecyclerTopCourses(List<Courses> coursesList) {
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        top_corses.setLayoutManager(layoutManager1);
        top_corses.setHasFixedSize(true);
        Log.e("topsize", coursesList.size() + "");
        CoursesAdapter corsesAdapter = new CoursesAdapter(getActivity(), 0, coursesList);
        top_corses.setAdapter(corsesAdapter);
    }

    private void loadEducator() {
        loadEducatorApi();
    }

    private void loadEducatorApi() {
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.Educators();

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    setEducatorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    private void setEducatorResponse(Response<JsonArray> response) {
        try {

            Log.i("educator", response.body().toString());
            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String res = jsonObject.getString("res");
            String msg = jsonObject.getString("msg");
            if (res.equalsIgnoreCase("success")) {

                EducatorModel educatorModel;
                listOfEducator = new ArrayList<>();
                JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    educatorModel = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), EducatorModel.class);
                    listOfEducator.add(educatorModel);

                }
                setInRecyclerEducator(listOfEducator);

            } else {
                educator_txt.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void setInRecyclerEducator(List<EducatorModel> listOfEducator) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        category_educators.setLayoutManager(layoutManager);
        category_educators.setHasFixedSize(true);
        EducatorsAdapter educatorsAdapter = new EducatorsAdapter(getActivity(), listOfEducator, 3);
        category_educators.setAdapter(educatorsAdapter);

    }

    private void getBanners() {
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call;
        call = myApi.banners();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                try {

                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String status = jsonObject.getString("res");
                    if (status.equalsIgnoreCase("success")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if (jsonObject1.getString("banner_1").equals("")) {
                            banner1.setVisibility(View.GONE);
                        }
                        if (jsonObject1.getString("banner_2").equals("")) {
                            banner2.setVisibility(View.GONE);
                        }
                        Picasso.get().load(jsonObject1.getString("banner_1")).into(banner1);
                        Picasso.get().load(jsonObject1.getString("banner_2")).into(banner2);

                        banner_1_parameter = jsonObject1.getString("banner_1_parameter");
                        banner_1_link = jsonObject1.getString("banner_1_link");
                        banner_2_parameter = jsonObject1.getString("banner_2_parameter");
                        banner_2_link = jsonObject1.getString("banner_2_link");


                    }
                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void openChromeCustomeTab(String uri) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(getActivity(), Uri.parse(uri));
    }

    private void initView(View view) {
        singleTask = (SingleTask) getActivity().getApplication();
        indicator = view.findViewById(R.id.pageIndicatorView);
        viewPager = view.findViewById(R.id.viewpager);
//        banner1=view.findViewById(R.id.banner1);
//        banner2=view.findViewById(R.id.banner2);
        trending_courses = view.findViewById(R.id.trending_courses);
        latest_corses = view.findViewById(R.id.latest_corses);
        top_corses = view.findViewById(R.id.top_courses);
        courseCategory = view.findViewById(R.id.category_course);
        courseCategory1 = view.findViewById(R.id.category_course1);
        shimmer = view.findViewById(R.id.shimmer_view_container);
        layout = view.findViewById(R.id.nesLay);
        category_educators = view.findViewById(R.id.category_educators);
        recommendedVideos = view.findViewById(R.id.recommended_videos_list);

        tendingTxt = view.findViewById(R.id.trending_txt);
        topTxt = view.findViewById(R.id.top_courses_txt);
        latestCourseTxt = view.findViewById(R.id.latest_corses_txt);
        courseByCategoryTxt = view.findViewById(R.id.courses_by_category_txt);
        ebookByCateoryTxt = view.findViewById(R.id.ebook_by_category_txt);
        educator_txt = view.findViewById(R.id.educator_txt);
        recommendedVideoTxt = view.findViewById(R.id.recommended_videos_txt);
        takeAdmission_card = view.findViewById(R.id.takeAdmission);

    }

    private void loadSlider() {
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.getSlider();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                if (response.isSuccessful()) {
                    setSliderResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("sdsd", t.getMessage());
            }
        });
    }

    private void setSliderResponse(Response<JsonArray> response) {
        try {

            Log.i("qazqaz", response.body().toString());

            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String res = jsonObject.getString("res");
            String msg = jsonObject.getString("msg");
            Log.e("dsd", res);
            Log.e("dsd", msg);
            if (res.equalsIgnoreCase("success")) {
                JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                sliderList = new ArrayList<>();
                SliderNo = new String[jsonArray1.length()];
                for (int i = 0; i < jsonArray1.length(); i++) {
                    Log.e("image", jsonArray1.getJSONObject(i).getString("image"));
                    SliderNo[i] = jsonArray1.getJSONObject(i).getString("image");
                    Slider slider = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Slider.class);
                    /*photos.add(jsonArray1.getJSONObject(i).getString("image"));*/
                    sliderList.add(slider);
                }
                setViewPagerAdapter(sliderList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setViewPagerAdapter(List<Slider> list) {
        ViewPagerAdapter myViewPagerAdapter = new ViewPagerAdapter(list, getActivity());
        viewPager.setAdapter(myViewPagerAdapter);
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    private void loadProfileDetails() {
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.userProfile(user.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.i("ghjkldfghjk", response.body().toString());
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("msg");
                        if (res.equals("success")) {
                            User use = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), User.class);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            singleTask.addValue("user", jsonObject1);
                            if (jsonObject1.getString("admission_status").equals("true")) {
                                takeAdmission_card.setVisibility(View.GONE);
                            } else {
                                takeAdmission_card.setVisibility(View.VISIBLE);
                            }


                        }
                        //Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

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

    private void Admission() {
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.Admission(user.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {

                        Log.e("sdsdhgjh", response.body().toString());
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("msg");
                        if (res.equals("success")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            order_id = jsonObject1.getString("order_id");
                            admission_fee = jsonObject1.getString("admission_fee");
                            app_id = jsonObject1.getString("AppId");
                            token = jsonObject1.getString("Token");
                            mode = jsonObject1.getString("Mode");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("excep", t.getMessage());

            }
        });

    }

    private void getInputParams() {
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        Map<String, String> params = new HashMap<>();
        params.put( PARAM_APP_ID, app_id );
        params.put( PARAM_ORDER_ID, order_id );
        params.put( PARAM_ORDER_AMOUNT, admission_fee );
        //  params.put(PARAM_ORDER_NOTE, orderNote);
        params.put( PARAM_CUSTOMER_NAME, user.getName());
        params.put( PARAM_CUSTOMER_PHONE, user.getNumber());
        params.put( PARAM_CUSTOMER_EMAIL, user.getEmail() );
        params.put( PARAM_ORDER_CURRENCY, "INR" );
        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation( 0 );
        cfPaymentService.doPayment( getActivity(), params, token, mode, "#F8A31A", "#FFFFFF", false );
        Log.e( "tokenss", token );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        Log.d( "m", "API Response : " );
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
                for (String key : bundle.keySet()) {
                    if (bundle.getString( key ) != null) {
                        Log.e( "sdsd", bundle.toString() + "" );
                        Log.d( "resp", key + " : " + bundle.getString( key ) );
                    }
                    txStatus = bundle.getString( "txStatus" );
                    referenceId = bundle.getString( "referenceId" );
                    txMsg = bundle.getString( "txMsg" );
                    txTime = bundle.getString( "txTime" );
                    if (txStatus.equals( "SUCCESS" )) {
                        updatePaymentStatusUpdate(referenceId,txStatus,bundle.toString());
                        return;
                    } else {
                        updatePaymentStatusUpdate(referenceId,txStatus,bundle.toString());
                       // Toast.makeText( getActivity(), "error", Toast.LENGTH_SHORT ).show();
                    }
                }
        }
    }

    private void updatePaymentStatusUpdate(String paymentId, String status, String bundle) {
        Log.i("check", "1");

        String json=singleTask.getValue("user");
        User user=new Gson().fromJson(json,User.class);

        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        Log.i("check", "21");
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.AdmissionStatusUpdate(order_id, paymentId, user.getNumber(), status,bundle);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.i("check", "1rt");

                if (response.isSuccessful()) {
                    Log.i("jhhhgfj", response.body().toString());
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        if (res.equals("success")) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "123", Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);

                            takeAdmission_card.setVisibility(View.GONE);
                            //sendToHomeActivity();
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            takeAdmission_card.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("fai", t.getMessage());
                dialog.dismiss();
            }
        });


    }





   /* white_check_mark
            eyes
    raised_hands
*/

  /*  @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            updatePaymentStatusUpdate(razorpayPaymentID, "success");
            //startActivity(new Intent());
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }
*/
    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */

   /* @Override
    public void onPaymentError(int code, String response) {
        try {
            updatePaymentStatusUpdate("", "failed");
            Toast.makeText(getActivity(), "Payment failed: " + "retry again" + response, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }
*/


}
