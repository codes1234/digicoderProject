package digi.coders.Qaione_Education.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import digi.coders.Qaione_Education.Activities.CheckActivity;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Assignment;

public class AssignmentListAdapter extends RecyclerView.Adapter<AssignmentListAdapter.MyHolder> {

    private Assignment[] assignments;
    private String courseId;
    private String  videoId;

    public AssignmentListAdapter(Assignment[] assignments, String courseId, String videoId) {
        this.assignments = assignments;
        this.courseId = courseId;
        this.videoId = videoId;
    }

    private Context ctx;
    public static Activity activityCompat;



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.assignement_layout,parent,false);
        return new MyHolder(view);
    }


    int i=1;
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final  int position) {
        Assignment assignment=assignments[position];

        holder.assignment_no.setText("Assignement -"+i);
        Log.e("sd",assignment.getAssignment()+"");

        holder.assignDesc.setText(Html.fromHtml(assignment.getDescription()));
        holder.downloadAttatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(ctx.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                    {
                      ActivityCompat.requestPermissions(activityCompat,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
                    }
                    else
                    {
                        Log.e("sad","download");
                        Uri uri= Uri.parse(Constraints.BASE_IMAGE_URL+Constraints.ASSIGNEMNT+assignment.getAssignment());
                        DownloadManager downloadManager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                        request.setTitle("Download  -"+holder.assignment_no.getText());
                        request.setDescription("Your Attachment is downloading please wait");
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,System.currentTimeMillis()+"ebook"+".pdf");
                        request.setMimeType("*/*");
                        downloadManager.enqueue(request);
                        Toast.makeText(ctx, "Downloading started", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        holder.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myInterface.click(position,holder);
                //myInterface1.click1(position,holder,assignment,v);
                //openChromeCustomeTab(assignment.getUploadLink() );
                CheckActivity.assignment=assignment;
                Intent in=new Intent(new Intent(ctx, CheckActivity.class));
                in.putExtra("no",position+1);
                in.putExtra("assigndesc",assignment.getDescription());
                in.putExtra("courseid",courseId);
                in.putExtra("videoid",videoId);
                in.putExtra("assignmentid",assignment.getId());
                in.putExtra("attachment",assignment.getAssignment());
                ctx.startActivity(in);
            }
        });


        holder.downloadAnswerKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(ctx.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                    {

                        ActivityCompat.requestPermissions(activityCompat,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
                    }
                    else
                    {
                        Log.e("sad","download");
                        Log.e("anserlink",assignment.getAnswer().getAssignment());
                        Uri uri= Uri.parse(Constraints.BASE_IMAGE_URL+Constraints.ANSWER_KEY+assignment.getAnswer().getAnswer());
                        DownloadManager downloadManager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                        request.setTitle("Download Answer key  -"+holder.assignment_no.getText());
                        request.setDescription("Your Answer key is downloading please wait");
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,System.currentTimeMillis()+"ebook"+".pdf");
                        request.setMimeType("*/*");
                        downloadManager.enqueue(request);
                        Toast.makeText(ctx, "Downloading started", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        if(assignment.getUploadStatus().equals("true")) {
            holder.upload.setVisibility(View.GONE);

            holder.downloadAnswerKey.setVisibility(View.VISIBLE);
            holder.downloadAttatch.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.upload.setVisibility(View.VISIBLE);
            holder.downloadAttatch.setVisibility(View.GONE);
            holder.downloadAnswerKey.setVisibility(View.GONE);

        }

        i++;

    }


    @Override
    public int getItemCount() {
        return assignments.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {

        public TextView assignment_no,assignDesc;
        public Button downloadAttatch,upload,downloadAnswerKey;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            assignment_no=itemView.findViewById(R.id.assignment_no);
            assignDesc=itemView.findViewById(R.id.assignment_desc);
            downloadAttatch=itemView.findViewById(R.id.download_attchment);
            downloadAnswerKey=itemView.findViewById(R.id.downloadAnswerKey);
            upload=itemView.findViewById(R.id.upload);
        }
    }

}
