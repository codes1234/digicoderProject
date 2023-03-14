package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.Qaione_Education.Adapters.OrderHistoryAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Order;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class OrderHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SingleTask singleTask;
    private Order order;
    private List<Order> listOfOrder;
    private ProgressBar progress;
    private Button back;
    private TextView noOrderText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        recyclerView=findViewById(R.id.order_history_list);
        progress=findViewById(R.id.progress_order);
        back=findViewById(R.id.back);
        noOrderText=findViewById(R.id.no_order_text);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        singleTask=(SingleTask)getApplication();
        loadOrderHistoryList();
    }

    private void loadOrderHistoryList() {
        String json=singleTask.getValue("user");
        User user=new Gson().fromJson(json,User.class);
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);

        Call<JsonArray> call=myApi.orderHistory(user.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("msg");
                        if(res.equals("success"))
                        {
                            int i;
                            listOfOrder=new ArrayList<>();
                            progress.setVisibility(View.GONE);
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");

                                noOrderText.setVisibility(View.GONE);
                                progress.setVisibility(View.GONE);
                                for (i = 0; i < jsonArray1.length(); i++) {
                                    order = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Order.class);
                                    listOfOrder.add(order);
                                }
                                setOrderInRecycler(listOfOrder);


                        }
                        else
                        {
                            progress.setVisibility(View.GONE);
                            noOrderText.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("error",t.getMessage());
                progress.setVisibility(View.GONE);
            }
        });
    }
    private void setOrderInRecycler(List<Order> listOfOrder) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new OrderHistoryAdapter(listOfOrder));
    }
}
