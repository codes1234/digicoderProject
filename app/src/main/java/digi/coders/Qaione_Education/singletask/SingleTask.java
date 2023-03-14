package digi.coders.Qaione_Education.singletask;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingleTask extends Application {
    public static Retrofit retrofit;
    public static final String BASE_URL="https://qaioneeducation.com/apis/V1/";

    //OkHttpClient client = new OkHttpClient();
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
//        if(retrofit==null)
//        {
//            retrofit=new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .client(client)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//    }


    public static Retrofit getRetrofit(){
        if (retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .build();
        }
        return retrofit;
    }
//    public Retrofit getRetrofit()
//    {
//        return retrofit;
//    }



    public SharedPreferences getSharedPreferene()
    {
        return getSharedPreferences("my_pref",MODE_PRIVATE);
    }
    public void addValue(String key, JSONObject jsonObject)
    {

            getSharedPreferene().edit().putString(key, String.valueOf(jsonObject)).commit();
    }
    public void removeUser(String key)
    {
        getSharedPreferene().edit().remove(key).commit();
    }
    public String getValue(String key)
    {
        return  getSharedPreferene().getString(key,null);
    }


    public SharedPreferences getSecondShare()
    {
        return getSharedPreferences("button",MODE_PRIVATE);
    }
    public void addStatus(String value)
    {

            SharedPreferences.Editor spe=getSecondShare().edit();
            spe.putString("status",value);
            spe.commit();


    }
    public String getStatus(String key)
    {
        return getSecondShare().getString(key,null);
    }
        public void remove(String key)
        {
            getSecondShare().edit().remove(key).commit();
        }


}
