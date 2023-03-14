package digi.coders.Qaione_Education.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.FilePath;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Assignment;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckActivity extends AppCompatActivity {

    public TextView assignment_no,assignDesc,choosePdf;
    public Button downloadAttatch,downloadAnswerKey,upload;
    public ImageView pdficon;
    public LinearLayout choosePdfLay;
    private int no;
    public static Assignment assignment;
    private SingleTask singleTask;
    private MultipartBody.Part assignmentAnswer;
    String courseid,videid,desc,assignemntid,assignmen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        initView();
        int  num=getIntent().getIntExtra("no",0);
        desc=getIntent().getStringExtra("assigndesc");
        courseid=getIntent().getStringExtra("courseid");
        videid=getIntent().getStringExtra("videoid");
        assignemntid=getIntent().getStringExtra("assignmentid");
        assignmen=getIntent().getStringExtra("attachment");
        no=num;

        setData();

    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        assignDesc.setText(Html.fromHtml(desc));
        assignment_no.setText("Assignement -"+no);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1001)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                //Log.e("sad","download");
                Uri uri= Uri.parse(Constraints.BASE_IMAGE_URL+Constraints.ASSIGNEMNT+assignmen);
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setTitle("Download  -"+assignment_no.getText());
                request.setDescription("Your Attachment is downloading please wait");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,System.currentTimeMillis()+"ebook"+".pdf");
                request.setMimeType("*/*");
                downloadManager.enqueue(request);
                Toast.makeText(this, "Downloading started", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode==1002)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(Intent.createChooser(intent, "select file"), 103);

            }

        }
    }

    private void initView() {
        singleTask=(SingleTask)getApplication();
        assignment_no=findViewById(R.id.assignment_no);
        assignDesc=findViewById(R.id.assignment_desc);
        choosePdf=findViewById(R.id.choosePdf);
        downloadAttatch=findViewById(R.id.download_attchment);
        downloadAnswerKey=findViewById(R.id.downloadAnswerKey);
        pdficon=findViewById(R.id.pdficon);
        choosePdfLay=findViewById(R.id.choosePdfLayout);
        upload=findViewById(R.id.upload);
    }


    public void choosePdf(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);

            }
            else {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(Intent.createChooser(intent, "select file"), 103);
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 103) {
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                String path = FilePath.getPath(this, uri);
                try {
                    File file = new File(FilePath.getPath(this, uri));
                    if (file != null) {
//                myHolder.pdficon.setVisibility(View.VISIBLE);
                        pdficon.setVisibility(View.VISIBLE);
                        RequestBody reqFile = RequestBody.create(MediaType.parse("*/*"), file);
                        assignmentAnswer = MultipartBody.Part.createFormData("answer", file.getName(), reqFile);
                    }

                }
                catch (Exception e)
                {

                }

//                Log.e("path",path+"");
                return;
            }
        }
    }

    public void uploadAssignme(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this,R.style.CustomProgressBarTheme);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), user.getId());
        RequestBody videoId = RequestBody.create(MediaType.parse("text/plain"),videid);
        RequestBody courseId = RequestBody.create(MediaType.parse("text/plain"), courseid);
        RequestBody assignementId = RequestBody.create(MediaType.parse("text/plain"), assignemntid);
        //myHolder.pdficon.setVisibility(View.VISIBLE);
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.assignementUpload(courseId, videoId, userId, assignementId, assignmentAnswer);
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
                            progressDialog.dismiss();
                            /*assignmentListAdapter.notifyDataSetChanged();
                            myHolder1.upload.setVisibility(View.GONE);
                            downloadAnswerKey.setVisibility(View.VISIBLE)*/;
                            Toast.makeText(CheckActivity.this, msg, Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            progressDialog.hide();
                            Toast.makeText(CheckActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(CheckActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }




            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("errretro", t.getMessage());
                Toast.makeText(CheckActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });

    }

    public void downloadAssignet(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
            }
            else
            {
                Log.e("sad","download");
                Uri uri= Uri.parse(Constraints.BASE_IMAGE_URL+Constraints.ASSIGNEMNT+assignmen);
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setTitle("Download  -"+assignment_no.getText());
                request.setDescription("Your Attachment is downloading please wait");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,System.currentTimeMillis()+"ebook"+".pdf");
                request.setMimeType("*/*");
                downloadManager.enqueue(request);
                Toast.makeText(this, "Downloading started", Toast.LENGTH_SHORT).show();


            }
        }
    }

    public void backFrom(View view) {
        finish();
    }

}
