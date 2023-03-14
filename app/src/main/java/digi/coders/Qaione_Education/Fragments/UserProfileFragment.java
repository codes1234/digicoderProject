package digi.coders.Qaione_Education.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import digi.coders.Qaione_Education.Activities.EditProfileActivity;
import digi.coders.Qaione_Education.Adapters.MyCoursesAdapter;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.MyCertificate;
import digi.coders.Qaione_Education.models.MyItem;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment {
LinearLayout paidlayout;
    RecyclerView my_courses;
    List<MyItem> itemList;
    RelativeLayout editProfile;
    private SingleTask singleTask;
    private ShimmerFrameLayout shimmer;
    private CircleImageView profileImage;
    private RelativeLayout noCoursePurchasedLay;
    private TextView userName, userEmail, course_name, watch_minutes, watch_no, user_books, user_courses, memberFrom,
            quiz_attempted, edit,paidamount,admissionstatus,admissionstatusinactive,userid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.user_profile_fragment, container, false);

        userName = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.user_email);
        course_name = view.findViewById(R.id.course_name);
//        watch_minutes=view.findViewById(R.id.watch_minutes);
//        watch_no=view.findViewById(R.id.watch_no);
        user_books = view.findViewById(R.id.user_books);
        user_courses = view.findViewById(R.id.user_courses);
        memberFrom = view.findViewById(R.id.member_from);
        quiz_attempted = view.findViewById(R.id.quiz_attempted);
        profileImage = view.findViewById(R.id.profile_image);
        noCoursePurchasedLay = view.findViewById(R.id.no_course_purchase_lay);
        shimmer = view.findViewById(R.id.shimmer_for_my_course);
        my_courses = view.findViewById(R.id.my_corses);
        edit = view.findViewById(R.id.edit);
        editProfile = view.findViewById(R.id.editProfile);
        paidamount=view.findViewById(R.id.paidAmount);
        admissionstatus=view.findViewById(R.id.admissionStatus);
        admissionstatusinactive=view.findViewById(R.id.admissionStatusInactive);
        userid=view.findViewById(R.id.user_id);
        paidlayout=view.findViewById(R.id.paidLayout);

        singleTask = (SingleTask) getActivity().getApplication();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });

        loadMyCourses();
        loadProfileDetails();
        return view;

    }

    private void setInCertificateRecycler(List<MyCertificate> listOfCertificate) {

    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfileDetails();
    }

    public void openChromeCustomeTab(String uri) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(getActivity(), Uri.parse(uri));
    }

    /* private void setUserData(User use) {
            Log.e("userName", this.use.getName());
            if (this.use != null) {
                if (this.use.getProfilePhoto() != "") {
                    Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.PROFILE_PHOTO + this.use.getProfilePhoto()).into(profileImage);
                }
                userName.setText(this.use.getName());

                userEmail.setText(this.use.getEmail());
                userLiveSession.setText(this.use.getLiveSession() + "");
                userBooks.set
                Text(this.use.getBooks() + "");
                userCourses.setText(this.use.getCourses() + "");
                userCertificate.setText(this.use.getCertificates() + "");
            }
        }
    */
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
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("msg");
                        if (res.equals("success")) {
                            Log.e("TAG", "onResponse: "+response.body().toString());
                            User use = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), User.class);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            singleTask.addValue("user", jsonObject1);
                            EditProfileActivity.user = use;

                            userid.setText(jsonObject1.getString("refcode"));
                            if(jsonObject1.getString("admission_status").equals("true")){
                                admissionstatus.setVisibility(View.VISIBLE);
                                paidlayout.setVisibility(View.VISIBLE);
                                paidamount.setText(" ₹ "+jsonObject1.getString("admission_fee"));
                            }else {
                                admissionstatusinactive.setVisibility(View.VISIBLE);
                                paidlayout.setVisibility(View.GONE);

                            }
                            setUserData(use);
                        }else {
                            //Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            paidamount.setText("");
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

    private void setUserData(User use) {

        if (use != null) {
            //if (use.getProfilePhoto() != "") {
            Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.PROFILE_PHOTO + use.getProfilePhoto()).placeholder(R.drawable.user).into(profileImage);
            //}
            if (use.getName() != "") {
                userName.setText(use.getName());
            }
            if (use.getEmail() != "") {
                userEmail.setText(use.getEmail());
            }
            if(use.getCourse().equals("")){
                course_name.setText("Student");
            }else {
                course_name.setText(use.getCourse());
            }
//            watch_minutes.setText(use.getWatch_minutes() + " mins");
//            watch_no.setText(use.getWatch_no() + "");
            user_books.setText(use.getBooks() + "");
            user_courses.setText(use.getCourses() + "");
            SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
            Log.e("cur time",use.getDate());
            Date date = null;
//            try {
////                date = dt.parse(use.getDate());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
//            String dateN = dt1.format(date);
            memberFrom.setText(use.getDate());
            quiz_attempted.setText(use.getQuiz_attempted() + "");
        }
    }

    private void loadMyCourses() {
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.getMyCourse(user.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    setMyCourseReponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("asds", t.getMessage());
            }
        });

    }

    private void setMyCourseReponse(Response<JsonArray> response) {

        try {
            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String res = jsonObject.getString("res");
            String msg = jsonObject.getString("msg");

            if (res.equals("success")) {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                my_courses.setVisibility(View.VISIBLE);
                int i;
                itemList = new ArrayList<>();
                JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                for (i = 0; i < jsonArray1.length(); i++) {
                    MyItem myItem = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), MyItem.class);
                    itemList.add(myItem);
                }
                setMyItemInRecycler(itemList);
            } else {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                noCoursePurchasedLay.setVisibility(View.VISIBLE);
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    private void setMyItemInRecycler(List<MyItem> itemList) {
        my_courses.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        my_courses.setHasFixedSize(true);
        MyCoursesAdapter myCoursesAdapter = new MyCoursesAdapter(getActivity(), itemList, 1);
        my_courses.setAdapter(myCoursesAdapter);
    }
}

