package digi.coders.Qaione_Education.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import digi.coders.Qaione_Education.R;

public class ScreenShotImageFullViewActivity extends AppCompatActivity {
    private ImageView imageView;
    public static Bitmap bitmap;
    boolean mPermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(Window.FEATURE_NO_TITLE, Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_screen_shot_image_full_view);
        imageView = findViewById(R.id.screenshot);
        imageView.setImageBitmap(bitmap);

    }


    public void close(View view) {
        finish();
    }

    public void savePic(View view) {
        /*if (!mPermission) {
            requestPermission();
            return;
        } else {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, byteArrayOutputStream);
            byte[] image=byteArrayOutputStream.toByteArray();
        if(image!=null) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), System.currentTimeMillis() + ".jpg");


            try {
                FileOutputStream fo = new FileOutputStream(file);
                Log.e("er", "writeing starting");
                fo.write(image);
                fo.close();
                Toast.makeText(this, "File write succesfully", Toast.LENGTH_SHORT).show();
                Log.e("eeor", "sucesfully");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this, "Sorry", Toast.LENGTH_SHORT).show();
        }
        }*/
        /*File file = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fileoutputstream;
        try {
            file.createNewFile();
            fileoutputstream = new FileOutputStream(file);
            fileoutputstream.write(byteArrayOutputStream.toByteArray());
            Log.e("file write successfully", "yes");
            fileoutputstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1011) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                mPermission = true;
            }
        }

    }
}
