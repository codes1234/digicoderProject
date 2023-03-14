package digi.coders.Qaione_Education.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.skydoves.elasticviews.ElasticButton;

import digi.coders.Qaione_Education.R;


public class FunctionClass {

    boolean status=false;
    public boolean showConfirmation(Context ctx,String str) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.confirmation_for_back, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);

        TextView text=promptView.findViewById(R.id.txt);
        ElasticButton ok=promptView.findViewById(R.id.ok);
        ElasticButton cancel=promptView.findViewById(R.id.cancel);

        text.setText(str);
        final AlertDialog alert = alertDialogBuilder.create();
        ok.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {

                        status=true;
                        alert.hide();


                    }
                }
        );
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {

                        status=false;
                        alert.hide();


                    }
                }
        );

        alert.show();

        return status;
    }
    public boolean showInputDialog2(String text, Context ctx) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.confirmation_for_back, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);


        final AlertDialog  alert2 = alertDialogBuilder.create();
        ElasticButton ok=promptView.findViewById(R.id.ok);
        ElasticButton cancel=promptView.findViewById(R.id.cancel);

        ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        status=true;
                    }
                }
        );
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alert2.dismiss();
                        status=false;
                    }
                }
        );

        alert2.show();
        return status;
    }
}
