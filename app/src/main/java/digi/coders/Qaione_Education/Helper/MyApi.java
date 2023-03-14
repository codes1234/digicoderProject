package digi.coders.Qaione_Education.Helper;

import com.google.gson.JsonArray;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface MyApi {

    @GET("SplashScreen")
    Call<JsonArray> SplashScreen();

    @POST("NewRegistration")
    @FormUrlEncoded
    Call<JsonArray> userRegister(@Field("name") String Name,
                                 @Field("mobile") String Mobile,
                                 @Field("email") String Email,
                                 @Field("password") String Password,
                                 @Field("referral_code") String referral_code,
                                 @Field("section") String section);

    @FormUrlEncoded
    @POST("VerifyReferral")
    Call<JsonArray> getVerifyReferral(@Field("referral_code") String referalCode);


    @FormUrlEncoded
    @POST("OTPVerification")
    Call<JsonArray> userOtpVerification(@Field("mobile") String mobile,
                                        @Field("otp") String otp);

    @POST("ResendOTP")
    @FormUrlEncoded
    Call<JsonArray> resendOtp(@Field("mobile") String Mobile);

    @FormUrlEncoded
    @POST("Login")
    Call<JsonArray> userLogin( @Field("mobile") String mobile,
                               @Field("password") String password);

    @POST("ForgotPassword")
    @FormUrlEncoded
    Call<JsonArray> forgetPass(@Field("mobile") String mobile);

    @POST("FpOTPVerification")
    @FormUrlEncoded
    Call<JsonArray> otpVerification(@Field("mobile") String Mobile,
                                    @Field("otp") String OTP);

    @POST("FpResendOTP")
    @FormUrlEncoded
    Call<JsonArray> resendOTP(@Field("mobile") String mobile);

    @POST("ResetPassword")
    @FormUrlEncoded
    Call<JsonArray> createPass(@Field("mobile") String mobile,
                               @Field("fp_token") String fp_token,
                               @Field("newpassword") String newpassword);

    @GET("Slider")
    Call<JsonArray> getSlider();

    @GET("Categories")
    Call<JsonArray> getCategories();

    @GET("Educators")
    Call<JsonArray> Educators();

    @FormUrlEncoded
    @POST("AllCourses")
    Call<JsonArray> getAllCourses(@Field("userid") String userId,
                                  @Field("mobile") String mobile,
                                  @Field("type") String type);

    @FormUrlEncoded
    @POST("TrendingCourses")
    Call<JsonArray> getTrendingCourses(@Field("userid") String userId,
                                       @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("TopCourses")
    Call<JsonArray> getTopCourses(@Field("userid") String userId,
                                  @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("LatestCourses")
    Call<JsonArray> getLatestCourses(@Field("userid") String userId,
                                     @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("CourseByCategory")
    Call<JsonArray>  coursesByCategory(@Field("categoryid") String id,
                                       @Field("userid") String userid);

    @FormUrlEncoded
    @POST("CourseByEducators")
    Call<JsonArray>  courseByEducators(@Field("educatorid") String id,
                                       @Field("userid") String userid);

    @FormUrlEncoded
    @POST("Ebook/ByCategory")
    Call<JsonArray> ebookByCategory(@Field("categoryid") String id,
                                    @Field("userid") String userid);

    @FormUrlEncoded
    @POST("CourseFullDetails")
    Call<JsonArray>  courseFullDetails(@Field("courseid") String id,
                                       @Field("userid") String userId,
                                        @Field("mobile") String mobile);

    @GET("Banners")
    Call<JsonArray> banners();

    @POST("EducatorData")
    @FormUrlEncoded
    Call<JsonArray> educatorFullDetails(@Field("educator_id") String educator_id);

    @FormUrlEncoded
    @POST("CouponValidation")
    Call<JsonArray> couponValidation(@Field("userid") String userId,
                                     @Field("couponcode") String coupon_code,
                                     @Field("itemtype") String itemtype,
                                     @Field("itemid") String itemid);

    @FormUrlEncoded
    @POST("EnrollStudent")
    Call<JsonArray> enrollStudent(@Field("userid") String userId,
                                      @Field("mobile") String mobile,
                                      @Field("firstname") String firstName,
                                      @Field("lastname") String lastName,
                                      @Field("emailid") String emailid,
                                      @Field("qualification") String qualification,
                                      @Field("courseid") String courseId,
                                      @Field("couponcode") String couponCode,
                                      @Field("price") String price,
                                      @Field("itemtype") String itemType,
                                      @Field("method") String method
                                      );

    @GET("RazorPayAPIKey")
    Call<JsonArray> getRezorPayAPIkey();

    @FormUrlEncoded
    @POST("PaymentStatusUpdate")
    Call<JsonArray> paymentStatusUpdate(@Field("orderid") String orderid,
                                        @Field("paymentid") String paymentid,
                                        @Field("mobile") String mobile,
                                        @Field("status") String status,
                                        @Field("cashFreeBundle") String cashFreeBundle);
    @FormUrlEncoded
    @POST("AdmissionStatusUpdate")
    Call<JsonArray> AdmissionStatusUpdate(@Field("orderid") String orderid,
                                        @Field("paymentid") String paymentid,
                                        @Field("mobile") String mobile,
                                        @Field("status") String status,
                                        @Field("cashFreeBundle") String cashFreeBundle);


    @FormUrlEncoded
    @POST("FreePaymentStatusUpdate")
    Call<JsonArray> freePaymentStatusUpdate(@Field("orderid") String orderid,
                                            @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("Wishlist/Add")
    Call<JsonArray> addWishlist(@Field("userid") String userId,
                                @Field("itemid") String itemId,
                                @Field("itemtype") String itemType );

    @FormUrlEncoded
    @POST("Wishlist/List")
    Call<JsonArray> getWishlist(@Field("userid") String userId,
                                @Field("itemtype") String itemType);

    @FormUrlEncoded
    @POST("Wishlist/Remove")
    Call<JsonArray> removeWishlist(@Field("id") String wishlistId);

    @FormUrlEncoded
    @POST("Ebook/List")
    Call<JsonArray> getEbookList(@Field("userid") String userId,
                                 @Field("mobile") String mobile,
                                 @Field("type") String type);
    @FormUrlEncoded
    @POST("Abook/List")
    Call<JsonArray> getAudiobookList(@Field("userid") String userId,
                                     @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("Ebook/FullDetails")
    Call<JsonArray> getEbookFullDetails(@Field("ebookid") String ebookid,
                                        @Field("userid") String userId,
                                        @Field("mobile") String mobile);
    @FormUrlEncoded
    @POST("Abook/FullDetails")
    Call<JsonArray> getAudioBookFullDetails(@Field("abookid") String abookid,
                                            @Field("userid") String userId,
                                            @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("MyItem/Ebook")
    Call<JsonArray> getMyEbook(@Field("userid") String userId);

    @FormUrlEncoded
    @POST("MyItem/Abook")
    Call<JsonArray> getMyAbook(@Field("userid") String userId);

    @FormUrlEncoded
    @POST("MyItem/Course")
    Call<JsonArray> getMyCourse(@Field("userid") String userId);


    @FormUrlEncoded
    @POST("Review/Add")
    Call<JsonArray> addReview(@Field("userid") String userId,
                              @Field("itemid") String itemId,
                              @Field("itemtype") String itemType,
                              @Field("rating") String rating,
                              @Field("review") String review);

    @Multipart
    @POST("UpdateProfile")
    Call<JsonArray> updateProfile(@Part("userid") RequestBody userId,
                                  @Part("name") RequestBody name,
                                  @Part("email") RequestBody email,
                                  @Part("education") RequestBody education,
                                  @Part("address") RequestBody address,
                                  @Part MultipartBody.Part profileImage);


    @FormUrlEncoded
    @POST("Profile")
    Call<JsonArray> userProfile(@Field("userid") String userId);


    @FormUrlEncoded
    @POST("Admission")
    Call<JsonArray> Admission(@Field("userid") String userId);


    @FormUrlEncoded
    @POST("OrderHistory")
    Call<JsonArray> orderHistory(@Field("userid") String userId);

    @FormUrlEncoded
    @POST("Notification")
    Call<JsonArray> getNotification(@Field("userid") String userId);


    @FormUrlEncoded
    @POST("Token/Save")
    Call<JsonArray> saveToken(@Field("userid") String userId,
                              @Field("token") String token);


    @FormUrlEncoded
    @POST("LiveSessions/List")
    Call<JsonArray> liveSessionList(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("Search")
    Call<JsonArray> search(@Field("keyword") String keyword,
                           @Field("mobile") String mobile,
                           @Field("userid") String userId);

    @FormUrlEncoded
    @POST("LiveSessions/Join")
    Call<JsonArray> liveJoin(@Field("liveid") String liveId,
                             @Field("name") String name,
                             @Field("email") String email,
                             @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("EnrolledCourse")
    Call<JsonArray> enrolledCourse( @Field("courseid") String courseId,
                                    @Field("userid") String userId,
                                    @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("EnrolledEbook")
    Call<JsonArray> enrolledEbook(@Field("ebookid") String ebookId,
                                  @Field("userid") String userId,
                                  @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("LiveSessions/FullDetails")
    Call<JsonArray> liveSessionFullDetails(@Field("liveid") String liveId,
                                           @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("LiveSessions/JoinWithoutInformation")
    Call<JsonArray> JoinWithoutInformation(@Field("liveid") String liveId,
                                           @Field("userid") String userid);

    @FormUrlEncoded
    @POST("VideoPlaylist")
    Call<JsonArray> getVideoPlaylist(@Field("courseid") String courseid,
                                     @Field("userid") String userId,
                                     @Field("videoid") String videoId);

    @Multipart
    @POST("Assignment/Upload")
    Call<JsonArray> assignementUpload(@Part("courseid") RequestBody courseId,
                                      @Part("videoid") RequestBody videoId,
                                      @Part("userid") RequestBody userId,
                                      @Part("assignmentid") RequestBody assignemntId,
                                      @Part MultipartBody.Part answer);

    @FormUrlEncoded
    @POST("VideoQuestion/Add")
    Call<JsonArray> postQuestion(@Field("courseid") String courseId,
                                 @Field("userid") String userId,
                                 @Field("videoid") String videoId,
                                 @Field("message") String message,
                                 @Field("questionid") String questionId
                                 );

    @POST("LiveQuestion/Add")
    @FormUrlEncoded
    Call<JsonArray> liveQuestion(@Field("liveid") String liveid,
                                 @Field("userid") String userid,
                                 @Field("message") String message);

    @FormUrlEncoded
    @POST("Logout")
    Call<JsonArray> logout(@Field("userid") String userId);

    @GET("Offers")
    Call<JsonArray> getOffers();

    @GET("RecommendedVideos/{id}")
    Call<JsonArray> getRecommendedVideos(@Path("id") String id);


    @FormUrlEncoded
    @POST("MyCertificates")
    Call<JsonArray> getMyCertificate(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("CheckUser")
    Call<JsonArray> checkUser(@Field("userid") String userId);

    @FormUrlEncoded
    @POST("MyQuiz/Quiz")
    Call<JsonArray> quiz(@Field("userid") String userId);

    @FormUrlEncoded
    @POST("MyQuiz/Attend/{id}")
    Call<JsonArray> attendQuiz(@Path("id") int  input,
                               @Field("userid") String userId);

    @FormUrlEncoded
    @POST("MyQuiz/Submit/{ID}")
    Call<JsonArray> submitQuiz(@Path("ID") int id,
                               @Field("userid") String userid,
                               @Field("right") String right,
                               @Field("wrong") String wrong,
                               @Field("ansBook") String ansBook,
                               @Field("score") String score);


    @POST("AppDetails")
    Call<JsonArray> getAppDetails();

    @POST("PayFromWallet")
    @FormUrlEncoded
    Call<JsonArray>PayFromWallet(
            @Field("orderid") String orderid,
            @Field("mobile") String mobileno,
            @Field("userid") String userid
    );


}
