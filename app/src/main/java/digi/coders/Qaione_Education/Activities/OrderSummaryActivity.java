package digi.coders.Qaione_Education.Activities;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cashfree.pg.CFPaymentService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Abookdetails;
import digi.coders.Qaione_Education.models.Courses;
import digi.coders.Qaione_Education.models.EbookPaymentFullDetails;
import digi.coders.Qaione_Education.models.Ebookdetails;
import digi.coders.Qaione_Education.models.FullPaymentData;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSummaryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PaymentResultListener {

    private static final String TAG = OrderSummaryActivity.class.getSimpleName();
    Spinner state;
    String[] stateList = {"Highest Qualification", "High School", "Intermediate", "Diploma", "Graduation", "Post Graduation", "Other"};
    private String couponCode;

    RadioGroup radioGroup;
    RadioButton byOnline, byWallet;
    private Button checkout;
    private TextView coupon_code, enrollWalletAmount;
    private LinearLayout coupon_lay, priceDetail;
    private String ORDER_ID;
    public static Ebookdetails ebook;
    private String stateText = "";
    private ImageView orderImage, back;
    public static Courses course;
    public static Abookdetails abookdetails;
    private EditText firstName, lastName, emailId, coupCode;
    private SingleTask singleTask;
    private ProgressBar progressBar;
    private TextView orderTitle, description, showPrice, cutPrice, orderMrp, orderPrice, orderGst, orderDiscount, orderTotalPrice, orderCouponDiscount, courseDiscount, freeTxt, removeCouponcode;
    private AlertDialog dialogBuilder;
    private String key;
    String courseId, ebookId;
    ProgressDialog progressDialog;
    private int i;
    String walletAmt = "";
    String payment_type = "Online";

    String token = "", app_id = "", mode = "";
    String txStatus;
    String referenceId;
    String txMsg;
    String txTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        initView();
        key = getIntent().getStringExtra("key");
        Log.e("sdweq", key);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Confirmation");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.hide();

        setData();
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_list, stateList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(aa);

        state.setOnItemSelectedListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        coupon_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogBuilder = new AlertDialog.Builder(OrderSummaryActivity.this).create();
                    LayoutInflater inflater = LayoutInflater.from(OrderSummaryActivity.this);
                    View dialogView = inflater.inflate(R.layout.coupon_code_dialog, null);
                    Button submit = dialogView.findViewById(R.id.submit);
                    ImageView close = dialogView.findViewById(R.id.close);
                    coupCode = dialogView.findViewById(R.id.coup_code);
                    progressBar = dialogView.findViewById(R.id.loadProgress);
                    dialogBuilder.setCanceledOnTouchOutside(false);

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    });


                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (validCode()) {
                                String json = singleTask.getValue("user");
                                User user = new Gson().fromJson(json, User.class);
                                progressBar.setVisibility(View.VISIBLE);
                                MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
                                Call<JsonArray> call;
                                if (key.equals("1")) {
                                    call = myApi.couponValidation(user.getId(), couponCode, "Course", courseId);
                                } else {
                                    call = myApi.couponValidation(user.getId(), couponCode, "Ebook", ebookId);
                                }
                                call.enqueue(new Callback<JsonArray>() {
                                    @Override
                                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                        if (response.isSuccessful()) {
                                            try {
                                                JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                String res = jsonObject.getString("res");
                                                String msg = jsonObject.getString("msg");
                                                progressBar.setVisibility(View.GONE);

                                                if (res.equalsIgnoreCase("success")) {

                                                    progressBar.setVisibility(View.GONE);
                                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                                    String discount = jsonObject1.getString("discount");
                                                    String discountType = jsonObject1.getString("discount_type");
                                                    String upto = jsonObject1.getString("upto");

                                                    Log.e("sdd", "asdad");

                                                    if (discountType.equalsIgnoreCase("amount")) {


                                                        orderCouponDiscount.setText(Html.fromHtml("&#8377") + " " + discount);
                                                        String[] d = orderCouponDiscount.getText().toString().split(" ");

                                                        if (key.equals("1")) {

                                                            i = Integer.parseInt(course.getOfferprice()) - Integer.parseInt(d[1]);
                                                        }
                                                        if (key.equals("2")) {
                                                            i = Integer.parseInt(ebook.getOfferprice()) - Integer.parseInt(d[1]);

                                                        }
                                                        orderTotalPrice.setText(Html.fromHtml("&#8377") + String.valueOf(i));
                                                        coupon_lay.setVisibility(View.VISIBLE);
                                                        Toast.makeText(OrderSummaryActivity.this, "Applied", Toast.LENGTH_SHORT).show();
                                                        dialogBuilder.dismiss();
                                                        coupon_code.setVisibility(View.GONE);
                                                        removeCouponcode.setVisibility(View.VISIBLE);
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
                                                        if (key.equals("1")) {

                                                            i = Integer.parseInt(course.getOfferprice()) - Integer.parseInt(d[1]);
                                                        }
                                                        if (key.equals("2")) {
                                                            i = Integer.parseInt(ebook.getOfferprice()) - Integer.parseInt(d[1]);

                                                        }
                                                        orderTotalPrice.setText(Html.fromHtml("&#8377") + String.valueOf(i));
                                                        Toast.makeText(OrderSummaryActivity.this, "Applied", Toast.LENGTH_SHORT).show();
                                                        dialogBuilder.dismiss();
                                                        coupon_code.setVisibility(View.GONE);
                                                        removeCouponcode.setVisibility(View.VISIBLE);
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
                                                    coupon_lay.setVisibility(View.VISIBLE);
                                                    coupon_code.setVisibility(View.GONE);
                                                    removeCouponcode.setVisibility(View.VISIBLE);
                                                }
                                                Toast.makeText(OrderSummaryActivity.this, msg, Toast.LENGTH_SHORT).show();


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        } else {

                                            return;
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JsonArray> call, Throwable t) {
                                        Toast.makeText(OrderSummaryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);

                                    }
                                });
                            }
                        }
                    });
                    dialogBuilder.setView(dialogView);
                    dialogBuilder.show();

                } catch (Exception e) {
                }
            }
        });

        loadProfileDetails();

        byOnline.setChecked(true);
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.byOnline) {
                            payment_type = "Online";
                        } else if (checkedId == R.id.section_B) {
                            payment_type = "Wallet";
                        }
                    }
                }
        );

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()) {
                    if (byWallet.isChecked()) {
                        if (Integer.parseInt(walletAmt) != 0) {
                            String d = orderTotalPrice.getText().toString().substring(1);
                            if (Integer.parseInt(walletAmt) > Integer.parseInt(d)) {
                                enrollStudent(payment_type);
                            } else {
                                Toast.makeText(getApplicationContext(), "You have not enough wallet money ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "You have zero balance ", Toast.LENGTH_SHORT).show();
                        }
                    } else if (byOnline.isChecked()) {
                        progressDialog.show();
                        enrollStudent(payment_type);
                    }
                }


            }
        });


    }

    private void PayFromWallet() {
        String jsonObject = singleTask.getValue("user");
        User user = new Gson().fromJson(jsonObject, User.class);
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.PayFromWallet(ORDER_ID,user.getNumber(),user.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                Log.e("hsgs", response.body().toString() );
                if (response.isSuccessful()) {
                    try {
                        Log.e("gyihi", response.body().toString());
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(OrderSummaryActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if (res.equals("success")) {
//                            if (i == 1) {
                                JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(0);

                                if (key.equals("1")) {
                                    FullPaymentData fullPaymentData = new Gson().fromJson(jsonObject1.toString(), FullPaymentData.class);
                                    ThankyouActivity.fullPaymentData = fullPaymentData;
                                }
                                if (key.equals("2")) {
                                    EbookPaymentFullDetails ebookPaymentFullDetails = new Gson().fromJson(jsonObject1.toString(), EbookPaymentFullDetails.class);
                                    ThankyouActivity.ePaymentDetails = ebookPaymentFullDetails;
                                }

                                Toast.makeText(OrderSummaryActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ThankyouActivity.class);
                                intent.putExtra("transactionid", "");
                                intent.putExtra("totalprice", orderTotalPrice.getText().toString());
                                intent.putExtra("key", key);
                                if (orderCouponDiscount.getText() != null) {
                                    intent.putExtra("coupondiscount", orderCouponDiscount.getText().toString());
                                } else {
                                    intent.putExtra("coupondiscount", "");
                                }

                                startActivity(intent);
                                finish();
                            }else {
                                dialog.dismiss();
                                progressDialog.dismiss();
                            }

//                        }

                    } catch (JSONException e) {
                        Log.e("fghjkl", e.getMessage().toString());
                        e.printStackTrace();
                    }
                    dialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    Call<JsonArray> call;

    private void enrollStudent(String method) {
        String jsonObject = singleTask.getValue("user");
        User user = new Gson().fromJson(jsonObject, User.class);
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);


        if (key.equals("1")) {
            if (couponCode != null)
                call = myApi.enrollStudent(user.getId(), user.getNumber(), fName, lName, email, hQualification, course.getId(), couponCode, orderTotalPrice.getText().toString().substring(1), "Course",method);
            else {
                call = myApi.enrollStudent(user.getId(), user.getNumber(), fName, lName, email, hQualification, course.getId(), null, orderTotalPrice.getText().toString().substring(1), "Course",method);
            }
        }
        if (key.equals("2")) {
            if (couponCode != null)
                call = myApi.enrollStudent(user.getId(), user.getNumber(), fName, lName, email, hQualification, ebook.getId(), couponCode, orderTotalPrice.getText().toString().substring(1), "Ebook",method);
            else {
                call = myApi.enrollStudent(user.getId(), user.getNumber(), fName, lName, email, hQualification, ebook.getId(), null, orderTotalPrice.getText().toString().substring(1), "Ebook",method);
            }

        }

        Log.e("Total price", orderTotalPrice.getText().toString());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    setReponseData(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(OrderSummaryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });


    }

    private void setReponseData(Response<JsonArray> response) {
        try {
            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String res = jsonObject.getString("res");
            String msg = jsonObject.getString("msg");
            Toast.makeText(OrderSummaryActivity.this, msg, Toast.LENGTH_SHORT).show();
            if (res.equals("success")) {
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                String order_id = jsonObject1.getString("OrderId");
                app_id = jsonObject1.getString("AppId");
                token = jsonObject1.getString("Token");
                mode = jsonObject1.getString("Mode");
                ORDER_ID = order_id;

                if (key.equals("1")) {
                    if (course.getType().equals("Paid")) {
                        //getRezorPayAPiKey();
                        if (byWallet.isChecked()){
                            PayFromWallet();
                        }else if(byOnline.isChecked()) {
                            getInputParams();
                        }

                    } else {
                        freePaymentStatusUpdate();
                    }

                } else {
                    if (ebook.getType().equals("Paid")) {
                        //getRezorPayAPiKey();
                        if (byWallet.isChecked()){
                            PayFromWallet();
                        }else if(byOnline.isChecked()) {
                            getInputParams();
                        }
                    } else {
                        freePaymentStatusUpdate();
                    }

                }

                Log.e("order_id", order_id);

            } else {
                progressDialog.hide();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean validCode() {
        couponCode = coupCode.getText().toString();
        if (TextUtils.isEmpty(couponCode)) {
            Toast.makeText(OrderSummaryActivity.this, "Please Enter your coupon code", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    private void setData() {
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        String d[] = user.getName().split(" ");
        Log.e("username", user.getEmail());
        Log.e("sdamykey7", key);
        if (user.getName() != null) {
            firstName.setText(d[0]);
            //lastName.setText(d[1]);
        }
        if (!user.getEmail().isEmpty()) {
            emailId.setText(user.getEmail());
        }

        if (key.equals("1")) {
            Log.e("asd", "hi");
            Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.COURSES + course.getBanner()).into(orderImage);
            courseId = (course.getId());
            orderTitle.setText(course.getName());
            description.setText(course.getShortdesc());
            showPrice.setText(Html.fromHtml("&#8377") + course.getOfferprice());

            orderMrp.setText(Html.fromHtml("&#8377") + course.getPrice());
            orderPrice.setText(Html.fromHtml("&#8377") + course.getOfferprice());
            int discountPrice = Integer.parseInt(course.getPrice()) - Integer.parseInt(course.getOfferprice());
            removeCouponcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    coupon_lay.setVisibility(View.GONE);
                    orderTotalPrice.setText(Html.fromHtml("&#8377") + course.getOfferprice());
                }
            });
            orderDiscount.setText(Html.fromHtml("&#8377") + String.valueOf(discountPrice));
            if (course.getType().equals("Paid")) {
                freeTxt.setVisibility(View.GONE);

            } else {
                coupon_code.setVisibility(View.GONE);
                priceDetail.setVisibility(View.GONE);
                freeTxt.setVisibility(View.VISIBLE);
                cutPrice.setVisibility(View.GONE);
                showPrice.setVisibility(View.GONE);
            }
            if (course.getDiscountpercent().equals("0% Off")) {
                courseDiscount.setVisibility(View.GONE);
                cutPrice.setVisibility(View.GONE);
            } else {
                cutPrice.setText(Html.fromHtml("<strike>&#8377 " + course.getPrice() + "</strike>"));
                courseDiscount.setText(course.getDiscountpercent());


            }
        /*if(orderCouponDiscount.getText().toString()!=null)
        {
            String[] d=orderCouponDiscount.getText().toString().split(" ");
            int i=Integer.parseInt(course.getOfferprice())-Integer.parseInt(d[1]);
            orderTotalPrice.setText(Html.fromHtml("&#8377")+String.valueOf(i));
        }
        else
        {*/
            orderTotalPrice.setText(Html.fromHtml("&#8377") + course.getOfferprice());
            /*}*/
            //orderTotalPrice.setText(Html.fromHtml("&#8377")+course.getOfferprice());


            //orderGst.setText(course.);
        }
        if (key.equals("2")) {
            Log.e("sad", key);
            Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.EBOOK + ebook.getBanner()).into(orderImage);
            ebookId = (ebook.getId());
            orderTitle.setText(ebook.getName());
            description.setText(ebook.getShortdesc());
            showPrice.setText(Html.fromHtml("&#8377") + ebook.getOfferprice());

            orderMrp.setText(Html.fromHtml("&#8377") + ebook.getPrice());
            orderPrice.setText(Html.fromHtml("&#8377") + ebook.getOfferprice());
            int discountPrice = Integer.parseInt(ebook.getPrice()) - Integer.parseInt(ebook.getOfferprice());
            orderDiscount.setText(Html.fromHtml("&#8377") + String.valueOf(discountPrice));
            removeCouponcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    coupon_lay.setVisibility(View.GONE);
                    orderTotalPrice.setText(Html.fromHtml("&#8377") + ebook.getOfferprice());
                }
            });
            if (ebook.getDiscountpercent().equals("0")) {
                courseDiscount.setVisibility(View.GONE);
                cutPrice.setVisibility(View.GONE);
            } else {
                cutPrice.setText(Html.fromHtml("<strike>&#8377 " + ebook.getPrice() + "</strike>"));
                courseDiscount.setText(ebook.getDiscountpercent());
            }
            if (ebook.getType().equals("Paid")) {
                freeTxt.setVisibility(View.GONE);
            } else {
                coupon_code.setVisibility(View.GONE);
                priceDetail.setVisibility(View.GONE);
                freeTxt.setVisibility(View.VISIBLE);
                cutPrice.setVisibility(View.GONE);
                showPrice.setVisibility(View.GONE);
            }
        /*if(orderCouponDiscount.getText().toString()!=null)
        {
            String[] d=orderCouponDiscount.getText().toString().split(" ");
            int i=Integer.parseInt(course.getOfferprice())-Integer.parseInt(d[1]);
            orderTotalPrice.setText(Html.fromHtml("&#8377")+String.valueOf(i));
        }
        else
        {*/
            orderTotalPrice.setText(Html.fromHtml("&#8377") + ebook.getOfferprice());
            /*}*/
            //orderTotalPrice.setText(Html.fromHtml("&#8377")+course.getOfferprice());


        }

    }

    private String fName, lName, email, hQualification;

    private boolean valid() {
        fName = firstName.getText().toString();
        lName = lastName.getText().toString();
        email = emailId.getText().toString();
        hQualification = state.getSelectedItem().toString();
        if (TextUtils.isEmpty(fName)) {
            firstName.setError("Please Enter your first name");
            firstName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(lName)) {
            lastName.setError("Please Enter your last name");
            lastName.requestFocus();
            return false;

        } else if (TextUtils.isEmpty(email)) {
            emailId.setError("Please Enter your email id");
            emailId.requestFocus();
            return false;

//        } else if (hQualification.equalsIgnoreCase("Highest Qualification")) {
//            Toast.makeText(this, "Please Select Your highest Qualification", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        }else
            return true;
    }

    private void initView() {
        coupon_lay = findViewById(R.id.coupon_lay);
        state = findViewById(R.id.state);
        coupon_code = findViewById(R.id.coupon_code);
        checkout = findViewById(R.id.checkout);
        orderCouponDiscount = findViewById(R.id.couponDiscount);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        removeCouponcode = findViewById(R.id.remove_copon);
        emailId = findViewById(R.id.email_id);
        orderImage = findViewById(R.id.order_image);
        orderTitle = findViewById(R.id.order_title);
        description = findViewById(R.id.description);
        showPrice = findViewById(R.id.show_price);
        cutPrice = findViewById(R.id.cut_price);
        orderMrp = findViewById(R.id.order_mrp);
        orderPrice = findViewById(R.id.order_price);
        orderGst = findViewById(R.id.order_gst);
        orderDiscount = findViewById(R.id.order_discount);
        orderTotalPrice = findViewById(R.id.order_totalPrice);
        back = findViewById(R.id.back23);
        singleTask = (SingleTask) getApplication();
        courseDiscount = findViewById(R.id.course_discount);
        priceDetail = findViewById(R.id.price_details);
        freeTxt = findViewById(R.id.free_txt);
        enrollWalletAmount = findViewById(R.id.enrollWalletAmount);
        radioGroup = findViewById(R.id.radio_Group);
        byOnline = findViewById(R.id.byOnline);
        byWallet = findViewById(R.id.byWallet);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        stateText = stateList[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(), "Qualification is mandatory", Toast.LENGTH_SHORT).show();
    }

    private User user;
    private String mobileNo;

    private void startPayment() {
        final Activity activity = this;
        String jsonObject = singleTask.getValue("user");
        user = new Gson().fromJson(jsonObject, User.class);
        mobileNo = user.getNumber();
        //Log.e("user",user.getNumber());
        //Log.e("sds",singleTask.getValue("mobile"));
        if (api_key != null) {
            Log.e("api_key", api_key);
            final com.razorpay.Checkout co = new Checkout();
            co.setKeyID(api_key);
            try {
                JSONObject options = new JSONObject();
                options.put("name", fName);
                if (key.equals("1")) {
                    options.put("description", "Payment  For " + course.getName() + "Course at http://qaione.digitalcoders.in/");
                }
                if (key.equals("2")) {
                    options.put("description", "Payment  For " + ebook.getName() + "Course at  http://qaione.digitalcoders.in/");

                }

                options.put("image", "http://qaione.digitalcoders.in/uploads/logo.png");
                options.put("theme.color", "#c300f5");
                options.put("currency", "INR");
                //Log.e("asdsad",ORDER_ID);
                options.put("order_id", ORDER_ID);
                String d = orderTotalPrice.getText().toString().substring(1);
                //Log.e("sd",d);
                //Log.e("sds",d);
                Log.e("sds", Double.parseDouble(d) * 100.0 + "");
                options.put("amount", Double.parseDouble(d) * 100.0);
                JSONObject preFill = new JSONObject();
                preFill.put("email", email);
                preFill.put("contact", user.getNumber());
                options.put("prefill", preFill);
                progressDialog.hide();
                co.open(activity, options);

            } catch (Exception e) {
                Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                Log.e("Error in payment", e.getMessage());
                e.printStackTrace();
            }
        } else {
            getRezorPayAPiKey();

        }
    }

    String api_key;

    private void getRezorPayAPiKey() {
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.getRezorPayAPIkey();
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
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String API_KEY = jsonObject1.getString("razorpay_key");
                            Log.e("sdsd", API_KEY);
                            api_key = API_KEY;
                            startPayment();
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

    int key1;

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            key1 = 1;
            //updatePaymentStatusUpdate(razorpayPaymentID, key1);
            //startActivity(new Intent());
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */

    @Override
    public void onPaymentError(int code, String response) {
        try {
            key1 = 2;
            //updatePaymentStatusUpdate("", key1);
            Toast.makeText(this, "Payment failed: " + "retry again" + response, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
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
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("msg");
                        if (res.equals("success")) {
                            User use = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), User.class);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            singleTask.addValue("user", jsonObject1);

                            enrollWalletAmount.setText(" ₹ " + jsonObject1.getString("wallet"));
                            walletAmt = jsonObject1.getString("wallet");

                        }
                        //Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();

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

    private void getInputParams() {
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_APP_ID, app_id);
        params.put(PARAM_ORDER_ID, ORDER_ID);
        String d = orderTotalPrice.getText().toString().substring(1);
        params.put(PARAM_ORDER_AMOUNT, d);
        //  params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, user.getName());
        params.put(PARAM_CUSTOMER_PHONE, user.getNumber());
        params.put(PARAM_CUSTOMER_EMAIL, user.getEmail());
        params.put(PARAM_ORDER_CURRENCY, "INR");
        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);
        cfPaymentService.doPayment(OrderSummaryActivity.this, params, token, mode, "#F8A31A", "#FFFFFF", false);
        Log.e("tokenss", token);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("m", "API Response : ");
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
                for (String key : bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        Log.e("sdsd", bundle.toString() + "");
                        Log.d("resp", key + " : " + bundle.getString(key));
                    }
                    txStatus = bundle.getString("txStatus");
                    referenceId = bundle.getString("referenceId");
                    txMsg = bundle.getString("txMsg");
                    txTime = bundle.getString("txTime");
                    if (txStatus.equals("SUCCESS")) {
                        key1 = 1;
                        updatePaymentStatusUpdate(referenceId, bundle.toString(), txStatus, key1);
                        return;
                    } else {
                        key1 = 2;
                        updatePaymentStatusUpdate(referenceId, bundle.toString(), txStatus, key1);

                        Toast.makeText(OrderSummaryActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    private void updatePaymentStatusUpdate(String paymentId, String bundle, String status, int i) {
        String jsonObject = singleTask.getValue("user");
        user = new Gson().fromJson(jsonObject, User.class);
        mobileNo = user.getNumber();
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        if (i == 1) {
            status = status;
        } else {
            status = status;
        }
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.paymentStatusUpdate(ORDER_ID, paymentId, mobileNo, status, bundle);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.e("gyihi", response.body().toString());
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(OrderSummaryActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if (res.equals("success")) {
                            if (i == 1) {
                                JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(0);

                                if (key.equals("1")) {
                                    FullPaymentData fullPaymentData = new Gson().fromJson(jsonObject1.toString(), FullPaymentData.class);
                                    ThankyouActivity.fullPaymentData = fullPaymentData;
                                }
                                if (key.equals("2")) {
                                    EbookPaymentFullDetails ebookPaymentFullDetails = new Gson().fromJson(jsonObject1.toString(), EbookPaymentFullDetails.class);
                                    ThankyouActivity.ePaymentDetails = ebookPaymentFullDetails;
                                }

                                Toast.makeText(OrderSummaryActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ThankyouActivity.class);
                                intent.putExtra("transactionid", paymentId);
                                intent.putExtra("totalprice", orderTotalPrice.getText().toString());
                                intent.putExtra("key", key);
                                if (orderCouponDiscount.getText() != null) {
                                    intent.putExtra("coupondiscount", orderCouponDiscount.getText().toString());
                                } else {
                                    intent.putExtra("coupondiscount", "");
                                }

                                startActivity(intent);
                                finish();
                            }else {
                                dialog.dismiss();
                                progressDialog.dismiss();
                            }

                        }

                    } catch (JSONException e) {
                        Log.e("fghjkl", e.getMessage().toString());
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

    private void freePaymentStatusUpdate() {
        String jsonObject = singleTask.getValue("user");
        user = new Gson().fromJson(jsonObject, User.class);
        mobileNo = user.getNumber();
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.freePaymentStatusUpdate(ORDER_ID, mobileNo);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(OrderSummaryActivity.this, msg, Toast.LENGTH_SHORT).show();
                        Log.i("hkjgxzvj", response.body().toString());
                        if (res.equals("success")) {
//                            if (i == 1) {
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);

                            if (key.equals("1")) {
                                FullPaymentData fullPaymentData = new Gson().fromJson(jsonObject1.toString(), FullPaymentData.class);
                                ThankyouActivity.fullPaymentData = fullPaymentData;
                            }
                            if (key.equals("2")) {
                                EbookPaymentFullDetails ebookPaymentFullDetails = new Gson().fromJson(jsonObject1.toString(), EbookPaymentFullDetails.class);
                                ThankyouActivity.ePaymentDetails = ebookPaymentFullDetails;
                            }

                            Toast.makeText(OrderSummaryActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ThankyouActivity.class);
                            intent.putExtra("transactionid", "");
                            intent.putExtra("totalprice", orderTotalPrice.getText().toString());
                            intent.putExtra("key", key);
                            if (orderCouponDiscount.getText() != null) {
                                intent.putExtra("coupondiscount", orderCouponDiscount.getText().toString());
                            } else {
                                intent.putExtra("coupondiscount", "");
                            }

                            startActivity(intent);
                            finish();
//                            }

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

}
