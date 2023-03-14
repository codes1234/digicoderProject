package digi.coders.Qaione_Education.Activities;

import static androidx.core.view.GravityCompat.END;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.razorpay.PaymentResultListener;
import com.skydoves.elasticviews.ElasticLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import digi.coders.Qaione_Education.Adapters.ItemListAdapter;
import digi.coders.Qaione_Education.EducatorloginActivity;
import digi.coders.Qaione_Education.Fragments.CoursesFragment;
import digi.coders.Qaione_Education.Fragments.EBookFragment;
import digi.coders.Qaione_Education.Fragments.FragmentHome;
import digi.coders.Qaione_Education.Fragments.PDFBookFragment;
import digi.coders.Qaione_Education.Fragments.QuizFragment;
import digi.coders.Qaione_Education.Fragments.SearchFragment;
import digi.coders.Qaione_Education.Fragments.SettingFragment;
import digi.coders.Qaione_Education.Fragments.UserProfileFragment;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import meow.bottomnavigation.MeowBottomNavigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private static final int END_SCALE = 1;
    LinearLayout paidlayout;
    BottomNavigationView bottomNavigationView;
    String web_dashboard = "";
    ImageView notification, user_image, userProfile;
    DrawerLayout drawerLayout;
    CardView search;
    private String purchaseKey;
    private ImageView menu_button;
    private SingleTask singleTask;
    TextView name, email, userName, walletAmount, paidamount, admissionstatus, admissionstatusinactive, userid;
    ElasticLayout lineHome, profile, about, order_history, wishlist, myEbook, logout, myCertificate, offers, refer, webDashboard,educator_Login, setting, donate;
    public static User user;

    Calendar c = Calendar.getInstance();
    int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        paidlayout = findViewById(R.id.paidLayout);
        menu_button = findViewById(R.id.menu_button);
        drawerLayout = findViewById(R.id.drawer_layout);
        search = findViewById(R.id.search);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        walletAmount = findViewById(R.id.wallet_Amount);
        userName = findViewById(R.id.userName);
        notification = findViewById(R.id.notification);
        user_image = findViewById(R.id.user_image);
        userProfile = findViewById(R.id.userProfile);
        donate = findViewById(R.id.donate);

        offers = findViewById(R.id.offers);
        refer = findViewById(R.id.refer);
        webDashboard = findViewById(R.id.web_Dashboard);
        educator_Login=findViewById(R.id.educator_Login);
        myCertificate = findViewById(R.id.my_certificate);
        order_history = findViewById(R.id.order_history);
        lineHome = findViewById(R.id.lineHome);
        profile = findViewById(R.id.profile);
        about = findViewById(R.id.about);
        wishlist = findViewById(R.id.wishlist);
        myEbook = findViewById(R.id.myEbook);
        setting = findViewById(R.id.setting);
        logout = findViewById(R.id.logout);
        paidamount = findViewById(R.id.paidAmount);
        admissionstatus = findViewById(R.id.admissionStatus);
        admissionstatusinactive = findViewById(R.id.admissionStatusInactive);
        userid = findViewById(R.id.user_id);

        singleTask = (SingleTask) getApplication();
        //animateNavigationDrawer();

        checkUser();
        loadProfileDetails();
        purchaseKey = getIntent().getStringExtra("purchaseKey");
        FragmentManager fm = getSupportFragmentManager();

        if (purchaseKey != null) {
            if (purchaseKey.equals("1")) {
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.add(R.id.fragment, new EBookFragment()).commit();
            }
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentHome(), "a").setReorderingAllowed(true).addToBackStack("a").commit();
        }

        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
//                FragmentTransaction fragmentTransaction=fm.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment,new FragmentHome()).commit();
            }
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingLiveBtn);
        floatingActionButton.setOnClickListener(view -> {
            Intent i = new Intent(HomeActivity.this, MultipleLiveClassesActivity.class);
            startActivity(i);
        });
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        notification.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                    }
                }
        );
        lineHome.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment, new FragmentHome()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                });
        userProfile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment, new UserProfileFragment()).commit();
                        search.setVisibility(View.GONE);
                    }
                });

        search.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        search.setVisibility(View.GONE);
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment, new SearchFragment()).commit();
                    }
                }
        );

        profile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment, new UserProfileFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        search.setVisibility(View.GONE);
                    }
                });

        setting.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment, new SettingFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        search.setVisibility(View.GONE);
                    }
                });

        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyOffersActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        refer.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ReferActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        webDashboard.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WebActivity.class);
            intent.putExtra("link", web_dashboard);
            intent.putExtra("tittle", "Web Dashboard");
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        educator_Login.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EducatorloginActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://rzp.io/l/3jgZHmEPP";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        myCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyCertificateActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                AlertDialog alertDialog = alert.create();
                alert.setMessage("Are you sure want to logout");

                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String js = singleTask.getValue("user");
                        User user = new Gson().fromJson(js, User.class);
                        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
                        Call<JsonArray> call = myApi.logout(user.getId());
                        call.enqueue(new Callback<JsonArray>() {
                            @Override
                            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                if (response.isSuccessful()) {
                                    try {

                                        //Toast.makeText(HomeActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String res = jsonObject.getString("res");
                                        String msg = jsonObject.getString("msg");
                                        if (res.equals("success")) {
                                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                            singleTask.removeUser("user");
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<JsonArray> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alert.show();

            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemListAdapter.singleTask = singleTask;
                Intent intent = new Intent(getApplicationContext(), MyWishlistActivity.class);
                intent.putExtra("sendkey", "1");
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });

        myEbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemListAdapter.singleTask = singleTask;
                Intent intent = new Intent(getApplicationContext(), MyEbookActivity.class);
                intent.putExtra("sendkey", "2");
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                intent.putExtra("link", "https://qaioneeducation.com/Home/about");
                intent.putExtra("tittle", "About Us");
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        order_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OrderHistoryActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }

        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("a");
        fragment.onActivityResult(requestCode, resultCode, data);
    }


    private void setUserData() {

        if (user != null) {

            if (user.getProfilePhoto() != "") {
                Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.PROFILE_PHOTO + user.getProfilePhoto()).placeholder(R.drawable.user).into(userProfile);
//                Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.PROFILE_PHOTO + user.getProfilePhoto()).placeholder(R.drawable.user).into(user_image);
            }
            name.setText(user.getName());
            email.setText(user.getEmail());
            String string = user.getName();
            String[] parts = string.split(" ");
            String part1 = parts[0];

            if (timeOfDay >= 0 && timeOfDay < 12) {
                userName.setText("Good Morning " + part1);
            } else if (timeOfDay >= 12 && timeOfDay < 16) {
                userName.setText("Good Afternoon " + part1);
            } else if (timeOfDay >= 16 && timeOfDay < 21) {
                userName.setText("Good Evening " + part1);
            } else if (timeOfDay >= 21 && timeOfDay < 24) {
                userName.setText("Good Night " + part1);
            }
        }

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
                        Log.e("kjigrnk", response.body().toString());
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("msg");
                        if (res.equals("success")) {
                            Log.e("TAG", response.body().toString());
                            User use = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), User.class);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            singleTask.addValue("user", jsonObject1);
                            HomeActivity.user = use;
//https://qaioneeducation.com/Home/WebDashboard/3e09071e50b95877b1c0de2337e6b4ba
                            walletAmount.setText(" ₹ " + jsonObject1.getString("wallet"));
                            userid.setText(jsonObject1.getString("refcode"));
                            web_dashboard = jsonObject1.getString("web_dashboard");
                            if (jsonObject1.getString("admission_status").equals("true")) {
                                admissionstatus.setVisibility(View.VISIBLE);
                                paidlayout.setVisibility(View.VISIBLE);
                                paidamount.setText(" ₹ " + jsonObject1.getString("admission_fee"));
                            } else {
                                admissionstatusinactive.setVisibility(View.VISIBLE);
                                paidlayout.setVisibility(View.GONE);
                            }

                            setUserData();
                        } else {
                            //Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    Call<JsonArray> call;

    private void checkUser() {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        if (!user.getId().isEmpty()) {

            call = myApi.checkUser(user.getId());
        }
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.i("response", response.body().toString());
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("msg");
                        if (res.equals("success")) {

                        } else {
                            startActivity(new Intent(HomeActivity.this, RegisterActivity.class));
                            finish();
                            Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    public void openChromeCustomeTab(String uri) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(getApplicationContext(), Uri.parse(uri));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.home:
                            search.setVisibility(View.VISIBLE);
                            selectedFragment = new FragmentHome();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment, "a").setReorderingAllowed(true).addToBackStack("a").commit();
                            break;
                        case R.id.courses:
                            search.setVisibility(View.GONE);
                            selectedFragment = new CoursesFragment();
                            break;
                        case R.id.e_book:
                            search.setVisibility(View.GONE);
                            selectedFragment = new PDFBookFragment();
                            break;
                        case R.id.quiz:
                            search.setVisibility(View.GONE);
                            selectedFragment = new QuizFragment();
                            break;
                    }

                    exchangeFragment(selectedFragment);
                    return true;
                }
            };

    public void exchangeFragment(androidx.fragment.app.Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).setReorderingAllowed(true).addToBackStack("a").commit();
        }
    }

    public void exchangeFragment1(androidx.fragment.app.Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileDetails();
    }

    /*

    @Override
    protected void onRestart() {
        super.onRestart();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentHome()).commit();
    }
*/
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            exchangeFragment(new FragmentHome());
            bottomNavigationView.setSelectedItemId(R.id.home);
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
    }

    private void animateNavigationDrawer() {
//        drawer.setScrimColor(getResources().getColor(R.color.purple_200));/**/
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                // scale the view  based on current  slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                drawerLayout.setScaleX(offsetScale);
                drawerLayout.setScaleY(offsetScale);


                // Translate the view  according for the scaled width
                final float xoffset = drawerView.getWidth() * slideOffset;
                final float xoffsetDiff = drawerLayout.getWidth() * diffScaledOffset / 2;
                final float xTransition = xoffset + xoffsetDiff;
                drawerLayout.setTranslationX(xTransition);

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

//    private void updatePaymentStatusUpdate(String razorpayPaymentID, String status) {
//        String jsonObject = singleTask.getValue("user");
//        String json=singleTask.getValue("user");
//        User user=new Gson().fromJson(json,User.class);
//        ProgressDialog dialog = new ProgressDialog(HomeActivity.this);
//        dialog.setMessage("Loading...");
//        dialog.setCancelable(false);
//        dialog.show();
//        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
//        Call<JsonArray> call = myApi.AdmissionStatusUpdate(FragmentHome.order_id, razorpayPaymentID, user.getNumber(), status);
//        call.enqueue(new Callback<JsonArray>() {
//            @Override
//            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                if (response.isSuccessful()) {
//                    try {
//                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
//                        JSONObject jsonObject = jsonArray.getJSONObject(0);
//                        String res = jsonObject.getString("res");
//                        String msg = jsonObject.getString("msg");
//                        Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
//                        if (res.equals("success")) {
//
//                            Toast.makeText(HomeActivity.this, "123", Toast.LENGTH_SHORT).show();
//                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
//                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
//                            singleTask.addValue("user",jsonObject1);
//                            //sendToHomeActivity();
//                            Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
//                            Fragment selectedFragment = new FragmentHome();
//                            exchangeFragment(selectedFragment);
//
//
//
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    dialog.dismiss();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonArray> call, Throwable t) {
//                Log.e("fai", t.getMessage());
//                dialog.dismiss();
//            }
//        });
//
//
//    }

}

