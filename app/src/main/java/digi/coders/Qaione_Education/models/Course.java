
package digi.coders.Qaione_Education.models;

import com.google.gson.annotations.SerializedName;

public class Course {

    private String id;
    private String category;
    private String author;
    private String name;
    private String logo;
    private String banner;
    private String type;
    private String price;
    private String offerprice;
    private String shortdesc;
    private String nooflecture;
    private String daystofinish;
    private String description;
    private String requirement;
    private String courseInclude;
    @SerializedName("will_learn")
    private String willLearn;
    private String demovedio;
    private String apprstatus;
    private String certification;
    private String certificate;
    @SerializedName("certificate_charge")
    private String certificateCharge;
    @SerializedName("km_charge")
    private String kmCharge;

    private String discountpercent;
    private String trending;
    private String videos;
    private String date;
    private String time;
    private String age;
    private String hardcopy;
    private String softcopy;
    private String onorderdesc;
    private String syllabus;
    private String greetings;
    private Integer rating;
    private Integer totalrating;
    private AuthorDetails authordetails;
    //private VideoPlaylist[] videoplaylist;
    private Categorydetails categorydetails;
    private Review reviewdetails;
    private String wishliststatus;
    private LiveSession[] LiveList;
    private Enroll enroll;


    public Enroll getEnroll() {
        return enroll;
    }

    public void setEnroll(Enroll enroll) {
        this.enroll = enroll;
    }

    public LiveSession[] getLiveList() {
        return LiveList;
    }

    public void setLiveList(LiveSession[] liveList) {
        LiveList = liveList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOfferprice() {
        return offerprice;
    }

    public void setOfferprice(String offerprice) {
        this.offerprice = offerprice;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public String getNooflecture() {
        return nooflecture;
    }

    public void setNooflecture(String nooflecture) {
        this.nooflecture = nooflecture;
    }

    public String getDaystofinish() {
        return daystofinish;
    }

    public void setDaystofinish(String daystofinish) {
        this.daystofinish = daystofinish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getCourseInclude() {
        return courseInclude;
    }

    public void setCourseInclude(String courseInclude) {
        this.courseInclude = courseInclude;
    }

    public String getWillLearn() {
        return willLearn;
    }

    public void setWillLearn(String willLearn) {
        this.willLearn = willLearn;
    }

    public String getDemovedio() {
        return demovedio;
    }

    public void setDemovedio(String demovedio) {
        this.demovedio = demovedio;
    }

    public String getApprstatus() {
        return apprstatus;
    }

    public void setApprstatus(String apprstatus) {
        this.apprstatus = apprstatus;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getCertificateCharge() {
        return certificateCharge;
    }

    public void setCertificateCharge(String certificateCharge) {
        this.certificateCharge = certificateCharge;
    }

    public String getKmCharge() {
        return kmCharge;
    }

    public void setKmCharge(String kmCharge) {
        this.kmCharge = kmCharge;
    }

    public String getDiscountpercent() {
        return discountpercent;
    }

    public void setDiscountpercent(String discountpercent) {
        this.discountpercent = discountpercent;
    }

    public String getTrending() {
        return trending;
    }

    public void setTrending(String trending) {
        this.trending = trending;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHardcopy() {
        return hardcopy;
    }

    public void setHardcopy(String hardcopy) {
        this.hardcopy = hardcopy;
    }

    public String getSoftcopy() {
        return softcopy;
    }

    public void setSoftcopy(String softcopy) {
        this.softcopy = softcopy;
    }

    public String getOnorderdesc() {
        return onorderdesc;
    }

    public void setOnorderdesc(String onorderdesc) {
        this.onorderdesc = onorderdesc;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public String getGreetings() {
        return greetings;
    }

    public void setGreetings(String greetings) {
        this.greetings = greetings;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getTotalrating() {
        return totalrating;
    }

    public void setTotalrating(Integer totalrating) {
        this.totalrating = totalrating;
    }

    public AuthorDetails getAuthordetails() {
        return authordetails;
    }

    public void setAuthordetails(AuthorDetails authordetails) {
        this.authordetails = authordetails;
    }

    public Categorydetails getCategorydetails() {
        return categorydetails;
    }

    public void setCategorydetails(Categorydetails categorydetails) {
        this.categorydetails = categorydetails;
    }

    public Review getReviewdetails() {
        return reviewdetails;
    }

    public void setReviewdetails(Review reviewdetails) {
        this.reviewdetails = reviewdetails;
    }

    public String getWishliststatus() {
        return wishliststatus;
    }

    public void setWishliststatus(String wishliststatus) {
        this.wishliststatus = wishliststatus;
    }

}
