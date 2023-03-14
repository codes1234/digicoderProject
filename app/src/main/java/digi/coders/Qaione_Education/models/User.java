
package digi.coders.Qaione_Education.models;

import com.google.gson.annotations.SerializedName;

public class User {
    private String id;
    private String name;
    private String college;
    private String email;
    private String password;
    private String course;
    private String number;
    private String status;
    private String date;
    private String refcode;

    private Object fpToken;
    private Object fpTokenStatus;
    private Object fpOtp;
    private Object fpType;
    private String otp;
    @SerializedName("otp_status")
    private String otpStatus;
    private String address;
    @SerializedName("profile_photo")
    private String profilePhoto;
    @SerializedName("live_session")
    private int liveSession;
    private int courses;
    private int books;
    private int certificates;
    private int watch_minutes;
    private int watch_no;
    private int test_completed;
    private int quiz_attempted;

    public String getRefcode() {
        return refcode;
    }

    public void setRefcode(String refcode) {
        this.refcode = refcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Object getFpToken() {
        return fpToken;
    }

    public void setFpToken(Object fpToken) {
        this.fpToken = fpToken;
    }

    public Object getFpTokenStatus() {
        return fpTokenStatus;
    }

    public void setFpTokenStatus(Object fpTokenStatus) {
        this.fpTokenStatus = fpTokenStatus;
    }

    public Object getFpOtp() {
        return fpOtp;
    }

    public void setFpOtp(Object fpOtp) {
        this.fpOtp = fpOtp;
    }

    public Object getFpType() {
        return fpType;
    }

    public void setFpType(Object fpType) {
        this.fpType = fpType;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOtpStatus() {
        return otpStatus;
    }

    public void setOtpStatus(String otpStatus) {
        this.otpStatus = otpStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public int getLiveSession() {
        return liveSession;
    }

    public void setLiveSession(int liveSession) {
        this.liveSession = liveSession;
    }

    public int getCourses() {
        return courses;
    }

    public void setCourses(int courses) {
        this.courses = courses;
    }

    public int getBooks() {
        return books;
    }

    public void setBooks(int books) {
        this.books = books;
    }

    public int getCertificates() {
        return certificates;
    }

    public void setCertificates(int certificates) {
        this.certificates = certificates;
    }

    public int getWatch_minutes() {
        return watch_minutes;
    }

    public void setWatch_minutes(int watch_minutes) {
        this.watch_minutes = watch_minutes;
    }

    public int getWatch_no() {
        return watch_no;
    }

    public void setWatch_no(int watch_no) {
        this.watch_no = watch_no;
    }

    public int getTest_completed() {
        return test_completed;
    }

    public void setTest_completed(int test_completed) {
        this.test_completed = test_completed;
    }

    public int getQuiz_attempted() {
        return quiz_attempted;
    }

    public void setQuiz_attempted(int quiz_attempted) {
        this.quiz_attempted = quiz_attempted;
    }
}
