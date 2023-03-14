package digi.coders.Qaione_Education.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Order;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class OrderHistoryDetail extends AppCompatActivity {
    public static Order order;
    private ImageView banner;
    private TextView orderTitle,orderAmount,orderDateTime,orderMrp,orderPrice,orderDiscount,orderCouponDiscount,orderTotalPrice,couponCode,orderId,paymentId,paymentStatus,orderDetail,paymentDate,paymentTime;
    private LinearLayout layout,couponDiscountLayout,priceDetails;
    private Button back;
    private SingleTask singleTask;
    View v;
    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_detail);
        initView();
        setData();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setData() {
        if(order!=null)
        {

            Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                    .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                    .setBaseAlpha(0.7f) //the alpha of the underlying children
                    .setHighlightAlpha(0.6f) // the shimmer alpha amount
                    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                    .setAutoStart(true)
                    .build();
            ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
            shimmerDrawable.setShimmer(shimmer);
            if(order.getItem().getType().equals("Paid"))
            {

            }
            else
            {
                priceDetails.setVisibility(View.GONE);
                orderAmount.setText("Free");
            }
            if(order.getItemtype().equals("Ebook"))
            {
                Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.EBOOK+order.getItem().getBanner()).placeholder(shimmerDrawable).into(banner);
            }
            if(order.getItemtype().equals("Course"))
            {
                Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.COURSES+order.getItem().getBanner()).placeholder(shimmerDrawable).into(banner);

            }
            orderTitle.setText(order.getItem().getName());
            orderAmount.setText("Amount: "+Html.fromHtml("&#8377")+" "+order.getPrice());
            orderDateTime.setText("Date:"+order.getDate()+"  "+order.getTime());
            orderMrp.setText(Html.fromHtml("&#8377")+order.getItem().getPrice());
            orderPrice.setText(Html.fromHtml("&#8377")+order.getItem().getOfferprice());

            orderTotalPrice.setText(Html.fromHtml("&#8377")+order.getPrice());

            orderDiscount.setText(Html.fromHtml("&#8377")+String.valueOf(Integer.parseInt(order.getItem().getPrice())-Integer.parseInt(order.getItem().getOfferprice())));
            paymentTime.setText(order.getTime());
            paymentDate.setText(order.getDate());
            if(order.getCouponcode().isEmpty())
            {
             couponCode.setText("No Coupon Applied");
             couponDiscountLayout.setVisibility(View.GONE);

            }
            else {
                String json = singleTask.getValue("user");
                User user = new Gson().fromJson(json, User.class);
                couponCode.setText(order.getCouponcode());
                MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
                Call<JsonArray> call = myApi.couponValidation(user.getId(),couponCode.getText().toString(),order.getItemtype(), order.getItemid());
                //Log.e("sds", couponCode);
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String res = jsonObject.getString("res");
                                String msg = jsonObject.getString("msg");
                                if (res.equalsIgnoreCase("success")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    String discount = jsonObject1.getString("discount");
                                    String discountType = jsonObject1.getString("discount_type");
                                    String upto = jsonObject1.getString("upto");

                                    Log.e("sdd", "asdad");

                                    if (discountType.equalsIgnoreCase("amount")) {


                                        orderCouponDiscount.setText(Html.fromHtml("&#8377") + " " + discount);
                                        String[] d = orderCouponDiscount.getText().toString().split(" ");

                                        if(order.getItemtype().equals("Course")) {

                                            i = Integer.parseInt(order.getItem().getOfferprice()) - Integer.parseInt(d[1]);
                                        }
                                        if(order.getItemtype().equals("Ebook"))
                                        {
                                            i = Integer.parseInt(order.getItem().getOfferprice()) - Integer.parseInt(d[1]);

                                        }
                                        //orderTotalPrice.setText(Html.fromHtml("&#8377") + String.valueOf(i));
                                        couponDiscountLayout.setVisibility(View.VISIBLE);

                                        orderTotalPrice.setText(Html.fromHtml("&#8377") +order.getPrice());
                                        //Toast.makeText(OrderSummaryActivity.this, "Applied", Toast.LENGTH_SHORT).show();
                                    }
                                    if (discountType.equalsIgnoreCase("percentage")) {
                                        String disPer = discount;
                                        Log.e("sds", disPer);
                                        //Double discountx = (Double.parseDouble(orderPrice.getText().toString().substring(1)) * Double.parseDouble(disPer)) / 100;
                                        int discountx = (Integer.parseInt(orderPrice.getText().toString().substring(1)) * Integer.parseInt(disPer)) / 100;
                                        Log.e("wew", discountx + "");
                                        if (discountx < Integer.parseInt(upto)) {
                                            orderCouponDiscount.setText(Html.fromHtml("&#8377") + " " + String.valueOf(discountx));
                                        } else {

                                            orderCouponDiscount.setText(Html.fromHtml("&#8377") + " " + upto);
                                        }
                                        String[] d = orderCouponDiscount.getText().toString().split(" ");
                                        if(order.getItem().getType().equals("Course")) {

                                            i = Integer.parseInt(order.getItem().getOfferprice()) - Integer.parseInt(d[1]);
                                        }
                                        if(order.getItem().getType().equals("Ebook"))
                                        {
                                            i = Integer.parseInt(order.getItem().getOfferprice()) - Integer.parseInt(d[1]);

                                        }
                                        //int tota=Integer.parseInt(order.getPrice())-Integer.parseInt(d[1]);
                                        orderTotalPrice.setText(Html.fromHtml("&#8377") +order.getPrice());

                                    }
                                                        /*if (key.equals("1")) {
                                                            Log.e("er","sfwer");
                                                            if (discountType.equalsIgnoreCase("amount")) {
                                                                orderCouponDiscount.setText(Html.fromHtml("&#8377") + " " + discount);
                                                                String[] d = orderCouponDiscount.getText().toString().split(" ");
                                                                int i = Integer.parseInt(ebook.getOfferprice()) - Integer.parseInt(d[1]);
                                                                orderTotalPrice.setText(Html.fromHtml("&#8377") + String.valueOf(i));
                                                                coupon_lay.setVisibility(View.VISIBLE);
                                                                Toast.makeText(OrderSummaryActivity.this, "Applied", Toast.LENGTH_SHORT).show();
                                                                dialogBuilder.dismiss();
                                                            }
                                                            if (discountType.equalsIgnoreCase("percentage")) {

                                                                String disPer = discount;
                                                                Log.e("sds", disPer);
                                                                //Double discountx = (Double.parseDouble(orderPrice.getText().toString().substring(1)) * Double.parseDouble(disPer)) / 100;
                                                                int discountx = (Integer.parseInt(orderPrice.getText().toString().substring(1)) * Integer.parseInt(disPer)) / 100;

                                                                Log.e("wew", discountx + "");
                                                                if (discountx < Integer.parseInt(upto)) {
                                                                    orderCouponDiscount.setText(Html.fromHtml("&#8377") + " " + String.valueOf(discountx));

                                                                } else {

                                                                    orderCouponDiscount.setText(Html.fromHtml("&#8377") + " " + upto);


                                                                }*/
                                                                /*String[] d = orderCouponDiscount.getText().toString().split(" ");
                                                                int i = Integer.parseInt(course.getOfferprice()) - Integer.parseInt(d[1]);
                                                                orderTotalPrice.setText(Html.fromHtml("&#8377") + String.valueOf(i));
                                                                Toast.makeText(OrderSummaryActivity.this, "Applied", Toast.LENGTH_SHORT).show();
                                                                dialogBuilder.dismiss();
                                                            }*/
                                  //  coupon_lay.setVisibility(View.VISIBLE);
                                }
                                //Toast.makeText(OrderSummaryActivity.this, msg, Toast.LENGTH_SHORT).show();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {

                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                    }
                });






            }
            orderId.setText(order.getRzpOrderid());
            if(order.getPaymentid().isEmpty()) {
                paymentId.setText("######");
            }
            else
            {
                paymentId.setText(order.getPaymentid());

            }

            if (order.getPaymentstatus().equals("failed")) {
                layout.setBackgroundResource(R.color.red);
                Log.e("cvd","failed");

            } else if (order.getPaymentstatus().equals("success")) {
                layout.setBackgroundResource(R.color.green);
                Log.e("cvd","success");

            } else {
                layout.setBackgroundResource(R.color.yellow);
                Log.e("cvd","pending");

            }

            paymentStatus.setText(order.getPaymentstatus());
            orderDetail.setText("Order Detail ( "+order.getItemtype()+" )" );



        }
    }

    private void initView() {
        banner=findViewById(R.id.o_banner);
        orderTitle=findViewById(R.id.o_title);
        orderAmount=findViewById(R.id.o_amout);
        orderDateTime=findViewById(R.id.o_date);
        orderMrp=findViewById(R.id.o_mrp);
        orderPrice=findViewById(R.id.o_price);
        orderDiscount=findViewById(R.id.o_discount);
        orderCouponDiscount=findViewById(R.id.o_coupon_discount);
        orderTotalPrice=findViewById(R.id.o_totalPrice);
        back=findViewById(R.id.back);
        singleTask=(SingleTask)getApplication();
        couponCode=findViewById(R.id.tcouponCode);
        orderId=findViewById(R.id.razor_order_id);
        paymentId=findViewById(R.id.paymentId);
        paymentStatus=findViewById(R.id.payment_status);
        layout=findViewById(R.id.layout);
        orderDetail=findViewById(R.id.order_details);
        couponDiscountLayout=findViewById(R.id.coupon_lay);
        priceDetails=findViewById(R.id.price_details);
        paymentDate=findViewById(R.id.payment_date);
        paymentTime=findViewById(R.id.payment_time);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1001)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                ViewGroup rootView = (ViewGroup) ((ViewGroup) this
                        .findViewById(android.R.id.content)).getChildAt(0);

                view = rootView;
                view.setDrawingCacheEnabled(true);
                bitmap = view.getDrawingCache();
                {
                    if (bitmap != null) {
                        ScreenShotImageFullViewActivity.bitmap = bitmap;
                        ByteArrayOutputStream by = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, by);
                        byte[] s = by.toByteArray();
                        //File fe=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM+"/ScreenShot"),"sdfsd");
                        //File file1=new File("","")
                        File   file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),System.currentTimeMillis()+order.getItem().getBanner()+".jpg");
                        try {
                            FileOutputStream fos=new FileOutputStream(file);
                            fos.write(s);
                            fos.flush();
                            Toast.makeText(this, "Invoice has been successfully download", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(this, ScreenShotImageFullViewActivity.class));
                    }
                }

            }
        }
    }

    View view;
    Bitmap bitmap;
    public void getInvoice(View v) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);

            }
            else
            {
                view = v.getRootView();
                view.setDrawingCacheEnabled(true);
                bitmap = view.getDrawingCache();
                {
                    if (bitmap != null) {
                        ScreenShotImageFullViewActivity.bitmap = bitmap;
                        ByteArrayOutputStream by = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, by);
                        byte[] s = by.toByteArray();
                        File   file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),System.currentTimeMillis()+order.getItem().getBanner()+".jpg");
                        try {
                            FileOutputStream fos=new FileOutputStream(file);
                            fos.write(s);
                            fos.flush();
                            Toast.makeText(this, "Invoice has been successfully download", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(this, ScreenShotImageFullViewActivity.class));
                    }
                }
            }

        }


    }
}
