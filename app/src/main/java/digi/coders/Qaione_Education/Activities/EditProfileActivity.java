package digi.coders.Qaione_Education.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.skydoves.elasticviews.ElasticButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 100;
    ElasticButton back;
    private EditText tName, tEmail, tEducation, tAddress;
    private CircleImageView imageView;
    private ImageView chooseImage;
    private MultipartBody.Part profileImage;
    private SingleTask singleTask;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initView();
        setUserData();
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void setUserData() {

        if (user != null) {

            Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                    .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                    .setBaseAlpha(0.7f) //the alpha of the underlying children
                    .setHighlightAlpha(0.6f) // the shimmer alpha amount
                    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                    .setAutoStart(true)
                    .build();
            ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
            shimmerDrawable.setShimmer(shimmer);
            if (user.getProfilePhoto() != "") {
                Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.PROFILE_PHOTO + user.getProfilePhoto()).placeholder(shimmerDrawable).into(imageView);
            }
            tName.setText(user.getName());
            tEmail.setText(user.getEmail());
            //tEducation.getEditText().setText(user.g());
            tAddress.setText(user.getAddress());
            tEducation.setText(user.getCourse());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 100) {
                galleryHandeledCode(data);
            }
        }


    }

    private void galleryHandeledCode(Intent data) {
        ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
        if (images.get(0).path != "") {
            Log.e("length", images.get(0).path + "");
            Glide.with(this).load(images.get(0).path).asBitmap().into(imageView);
            File file = new File(images.get(0).path);
            if (file != null) {
                Log.e("sad", "file is not empty");
            }
            RequestBody reqFile = RequestBody.create(MediaType.parse("*/*"), file);
            profileImage = MultipartBody.Part.createFormData("profile_photo", file.getName(), reqFile);
        }
    }


    private void selectImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                return;
            }
        }

        Intent intent = new Intent(EditProfileActivity.this, AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 1); //set desired image limit here
        startActivityForResult(intent, GALLERY_REQUEST);

    }

    private void requestPermission(String readExternalStorage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permisssion granted", Toast.LENGTH_SHORT).show();

            } else {
                requestPermission(permissions[0]);
            }
        }
    }


    private void initView() {
        back = findViewById(R.id.back);
        tName = findViewById(R.id.uname);
        tEmail = findViewById(R.id.uemail);
        tEducation = findViewById(R.id.ueducation);
        tAddress = findViewById(R.id.uaddres);
        imageView = findViewById(R.id.uimage);
        singleTask = (SingleTask) getApplication();
        chooseImage = findViewById(R.id.chooseImage);
    }

    public void updateProfile(View view) {


        if(valid())
        {
            updateUserProfile();

        }
    }

    private void updateUserProfile() {
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        RequestBody vId = RequestBody.create(MediaType.parse("text/plain"), user.getId());
        RequestBody vName = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody vEmail = RequestBody.create(MediaType.parse("text/plain"), userEmail);
        RequestBody vEducation = RequestBody.create(MediaType.parse("text/plain"), userEducation);
        RequestBody vAddress = RequestBody.create(MediaType.parse("text/plain"), userAddress);
        ProgressDialog pr = new ProgressDialog(this);
        pr.setTitle("Updating");
        pr.setMessage("Please Wait");
        pr.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pr.setCanceledOnTouchOutside(false);
        pr.show();
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.updateProfile(vId, vName, vEmail, vEducation, vAddress, profileImage);
        Log.e("sds", vId.toString());
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
                            pr.hide();

                        }
                        pr.hide();
                        Toast.makeText(EditProfileActivity.this, msg, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("sads", t.getMessage());
                pr.hide();
            }
        });

    }

    String userName, userEmail, userEducation, userAddress;

    private boolean valid() {
        userName = tName.getText().toString();
        userEmail = tEmail.getText().toString();
        userEducation = tEducation.getText().toString();
        userAddress = tAddress.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            tName.setError("Please Enter your name");
            tName.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(userEmail)) {
            tEmail.setError("Please Enter your email");
            tEmail.requestFocus();
            return false;

        }
        else if (TextUtils.isEmpty(userEducation)) {
            tEducation.setError("Please Enter your education");
            tEducation.requestFocus();
            return false;
        } /*else if (TextUtils.isEmpty(userAddress)) {
            tAddress.setError("Please Enter your adddress");
            tAddress.requestFocus();
            return false;
        }*/
        else
        {
            return true;
        }


    }
}



