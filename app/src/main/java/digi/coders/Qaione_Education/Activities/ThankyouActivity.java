package digi.coders.Qaione_Education.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Coursedetails;
import digi.coders.Qaione_Education.models.EbookPaymentFullDetails;
import digi.coders.Qaione_Education.models.Ebookdetails;
import digi.coders.Qaione_Education.models.FullPaymentData;

public class ThankyouActivity extends AppCompatActivity {
    TextView order_text;
    Button ok;
    public static FullPaymentData fullPaymentData;
    public static EbookPaymentFullDetails ePaymentDetails;
    private String razorPaymentId,couponDiscount,totalPrice,key;
    private ImageView orderImage,back;
    private LinearLayout couponLay,mid;
    private TextView orderTitle,showPrice,cutPrice,orderMrp,orderPrice,orderGst,orderDiscount,orderTotalPrice,orderCouponDiscount,paymentId,orderId,couponCode,title,paymentStatus,paymentDate,paymentTime;
    String courseId, ebookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        initView();
        thankYouDialog();
        key=getIntent().getStringExtra("key");
        couponDiscount=getIntent().getStringExtra("coupondiscount");
        razorPaymentId=getIntent().getStringExtra("transactionid");
        totalPrice=getIntent().getStringExtra("totalprice");
        Log.e("sd",razorPaymentId+"");
        ok=findViewById(R.id.ok);
        setData();
//          order_text.setText("Your Transaction Id is "+razorPaymentId);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.equals("1")){
                    ok.setText("View Course Detail");
                    Intent intent = new Intent(getApplicationContext(), MyCourseDetailsActivity.class);
                    intent.putExtra("courseid",courseId);
                    startActivity(intent);
                    finish();
                } else  if (key.equals("2")){
                    ok.setText("View PDF book Detail");
                    Intent intent = new Intent(getApplicationContext(), MyEbookDetailsActivity.class);
                    intent.putExtra("ebookid",ebookId);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    public void thankYouDialog(){
//        try {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(ThankyouActivity.this).create();
        LayoutInflater inflater = LayoutInflater.from(ThankyouActivity.this);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View dialogView = inflater.inflate(R.layout.thank_you_dialog, null);

        dialogBuilder.setCanceledOnTouchOutside(false);
        TextView okay=dialogView.findViewById(R.id.okay);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

//        } catch (Exception e) {
//
//        }
    }

    private void setData() {
        if(key.equals("1")) {
            Coursedetails course = fullPaymentData.getCoursedetails();
            Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.COURSES + course.getBanner()).into(orderImage);
            courseId=course.getId();
            orderTitle.setText(course.getName());
            showPrice.setText(Html.fromHtml("&#8377") + course.getOfferprice());
            cutPrice.setText(Html.fromHtml("<strike>&#8377 " + course.getPrice() + "</strike>"));
            orderMrp.setText(Html.fromHtml("&#8377") + course.getPrice());
            orderPrice.setText(Html.fromHtml("&#8377") + course.getOfferprice());
            int discountPrice = Integer.parseInt(course.getPrice()) - Integer.parseInt(course.getOfferprice());
            orderDiscount.setText(Html.fromHtml("&#8377") + String.valueOf(discountPrice));
            couponCode.setText(fullPaymentData.getCouponcode());
            paymentId.setText(fullPaymentData.getPaymentid());
            paymentStatus.setText(fullPaymentData.getPaymentstatus());
            paymentDate.setText(fullPaymentData.getDate());
            paymentTime.setText(fullPaymentData.getTime());

            orderId.setText(fullPaymentData.getRzpOrderid());

            if(fullPaymentData.getCouponcode().isEmpty())
            {
                couponLay.setVisibility(View.GONE);
                couponCode.setText("No Coupon");
            }
            else
            {
                couponLay.setVisibility(View.VISIBLE);
                orderCouponDiscount.setText(couponDiscount);
            }
            if(course.getType().equals("Paid"))
            {

            }
            else
            {
                mid.setVisibility(View.GONE);
                showPrice.setVisibility(View.GONE);
                cutPrice.setVisibility(View.GONE);
            }


            title.setText("You have successfully purchased " + course.getName() + " from CodersAdda");
        /*if(orderCouponDiscount.getText().toString()!=null)
        {
            String[] d=orderCouponDiscount.getText().toString().split(" ");
            int i=Integer.parseInt(course.getOfferprice())-Integer.parseInt(d[1]);
            orderTotalPrice.setText(Html.fromHtml("&#8377")+String.valueOf(i));
        }
        else
        {*/
            orderTotalPrice.setText(Html.fromHtml("&#8377") + course.getOfferprice());
        }
        if(key.equals("2"))
        {

            Ebookdetails ebook=ePaymentDetails.getEbookdetails();
            Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.EBOOK + ebook.getBanner()).into(orderImage);
            ebookId=ebook.getId();
            orderTitle.setText(ebook.getName());
            showPrice.setText(Html.fromHtml("&#8377") + ebook.getOfferprice());
            cutPrice.setText(Html.fromHtml("<strike>&#8377 " + ebook.getPrice() + "</strike>"));
            orderMrp.setText(Html.fromHtml("&#8377") + ebook.getPrice());
            orderPrice.setText(Html.fromHtml("&#8377") + ebook.getOfferprice());
            int discountPrice = Integer.parseInt(ebook.getPrice()) - Integer.parseInt(ebook.getOfferprice());
            orderDiscount.setText(Html.fromHtml("&#8377") + String.valueOf(discountPrice));
            couponCode.setText(ePaymentDetails.getCouponcode());
            paymentId.setText(ePaymentDetails.getPaymentid());
            paymentStatus.setText(ePaymentDetails.getPaymentstatus());
            paymentDate.setText(ePaymentDetails.getDate());
            paymentTime.setText(ePaymentDetails.getTime());
            orderId.setText(ePaymentDetails.getRzpOrderid());

            if(ebook.getType().equals("Paid"))
            {

            }
            else
            {
                mid.setVisibility(View.GONE);
                showPrice.setVisibility(View.GONE);
                cutPrice.setVisibility(View.GONE);
            }
            if(ePaymentDetails.getCouponcode().isEmpty())
            {
                couponLay.setVisibility(View.GONE);
                couponCode.setText("No Coupon");
            }
            else
            {
                couponLay.setVisibility(View.VISIBLE);
                orderCouponDiscount.setText(couponDiscount);
            }

            title.setText("You have successfully purchased " + ebook.getName() + " from CodersAdda");
        /*if(orderCouponDiscount.getText().toString()!=null)
        {
            String[] d=orderCouponDiscount.getText().toString().split(" ");
            int i=Integer.parseInt(course.getOfferprice())-Integer.parseInt(d[1]);
            orderTotalPrice.setText(Html.fromHtml("&#8377")+String.valueOf(i));
        }
        else
        {*/
            orderTotalPrice.setText(Html.fromHtml("&#8377") + ebook.getOfferprice());
        }
        /*}*/
        //orderTotalPrice.setText(Html.fromHtml("&#8377")+course.getOfferprice());
        //orderGst.setText(course.);
    }

    private void initView() {

        paymentStatus=findViewById(R.id.payment_status);
        paymentTime=findViewById(R.id.payment_time);
        paymentDate=findViewById(R.id.payment_date);
        orderImage=findViewById(R.id.torder_image);
        orderTitle=findViewById(R.id.torder_title);
        showPrice=findViewById(R.id.tshow_price);
        cutPrice=findViewById(R.id.tcut_price);
        orderMrp=findViewById(R.id.torder_mrp);
        orderPrice=findViewById(R.id.torder_price);
        orderGst=findViewById(R.id.torder_gst);
        orderDiscount=findViewById(R.id.torder_discount);
        orderTotalPrice=findViewById(R.id.torder_totalPrice);
        couponCode=findViewById(R.id.tcouponCode);
        couponLay=findViewById(R.id.tcoupon_lay);
        paymentId=findViewById(R.id.paymentId);
        orderId=findViewById(R.id.razor_order_id);
        mid=findViewById(R.id.mid);
        orderCouponDiscount=findViewById(R.id.tcouponDiscount);
        title=findViewById(R.id.title);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }
    View view;
    Bitmap bitmap;
    public void downloadReceipt(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            }
            else
            {
            view = v.getRootView();

            view.setDrawingCacheEnabled(true);

            bitmap = view.getDrawingCache();
            if(bitmap!=null) {

                ScreenShotImageFullViewActivity.bitmap = bitmap;

                ByteArrayOutputStream by = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, by);
                byte[] s = by.toByteArray();
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + System.currentTimeMillis() + ".jpg");
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(s);
                    fos.flush();
                    Toast.makeText(this, "Receipt download successfully", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(this, ScreenShotImageFullViewActivity.class));
            }


            }


        }

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                ViewGroup rootView = (ViewGroup) ((ViewGroup) this
                        .findViewById(android.R.id.content)).getChildAt(0);

                view = rootView;
                view.setDrawingCacheEnabled(true);

                bitmap = view.getDrawingCache();
                if(bitmap!=null) {

                    ScreenShotImageFullViewActivity.bitmap=bitmap;

                    ByteArrayOutputStream by = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, by);
                    byte[] s = by.toByteArray();

                    File   file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),System.currentTimeMillis()+".jpg");
                    try {
                        FileOutputStream fos=new FileOutputStream(file);
                        fos.write(s);
                        fos.flush();
                        Toast.makeText(this, "Receipt download successfully", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, ScreenShotImageFullViewActivity.class));

                }

            } else {
                requestPermission(permissions[0]);
            }

    }
}

    private void requestPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{permission}, 1001);
        }
    }
    }

