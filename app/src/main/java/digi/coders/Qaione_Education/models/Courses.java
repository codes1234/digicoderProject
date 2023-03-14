
package digi.coders.Qaione_Education.models;

import com.google.gson.annotations.SerializedName;

public class Courses {

    private String id;
    private String category;
    private String author;
    private String name;
    private String logo;
    private String banner;
    private String type;
    private String price;
    private String timing;
    private String upcoming;
    private String offerprice;
    private String shortdesc;
    private String nooflecture;
    private String daystofinish;
    private String description;
    private String requirement;
    @SerializedName("course_include")
    private String courseInclude;
    private String will_learn;
    @SerializedName("wishliststatus")
    private String wishlistStatus;
    private String demovedio;
    private String YoutubeId;
    private String apprstatus;
    private String certification;
    private String discountpercent;
    private String trending;
    private String videos;
    private String date;
    private String time;
    private String reviewstatus;

    public String getYoutubeId() {
        return YoutubeId;
    }

    public void setYoutubeId(String youtubeId) {
        YoutubeId = youtubeId;
    }

    public String getReviewstatus() {
        return reviewstatus;
    }

    public void setReviewstatus(String reviewstatus) {
        this.reviewstatus = reviewstatus;
    }

    @SerializedName("authordetails")
    private AuthorDetails authorDetails;
    @SerializedName("videoplaylist")
    private VideoDetails[] videoDetails;
    private Categorydetails categorydetails;
    private Reviewdetails reviewdetails;
    private int  rating;
    private int  totalrating;
    private String certificatestatus;
    private Enroll enroll;

    public Enroll getEnroll() {
        return enroll;
    }

    public void setEnroll(Enroll enroll) {
        this.enroll = enroll;
    }

    public String getCertificatestatus() {
        return certificatestatus;
    }

    public void setCertificatestatus(String certificatestatus) {
        this.certificatestatus = certificatestatus;
    }

    public int  getRating() {
        return rating;
    }

    public void setRating(int  rating) {
        this.rating = rating;
    }

    public int getTotalrating() {
        return totalrating;
    }

    public void setTotalrating(int totalrating) {
        this.totalrating = totalrating;
    }

    public String getWishlistStatus() {
        return wishlistStatus;
    }
    public void setWishlistStatus(String wishlistStatus) {
        this.wishlistStatus = wishlistStatus;
    }
    public String getWill_learn() {
        return will_learn;
    }

    public void setWill_learn(String will_learn) {
        this.will_learn = will_learn;
    }

    public AuthorDetails getAuthorDetails() {
        return authorDetails;
    }

    public void setAuthorDetails(AuthorDetails authorDetails) {
        this.authorDetails = authorDetails;
    }

    public VideoDetails[] getVideoDetails() {
        return videoDetails;
    }

    public void setVideoDetails(VideoDetails[] videoDetails) {
        this.videoDetails = videoDetails;
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

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(String upcoming) {
        this.upcoming = upcoming;
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


    public Categorydetails getCategorydetails() {
        return categorydetails;
    }

    public void setCategorydetails(Categorydetails categorydetails) {
        this.categorydetails = categorydetails;
    }

    public Reviewdetails getReviewdetails() {
        return reviewdetails;
    }

    public void setReviewdetails(Reviewdetails reviewdetails) {
        this.reviewdetails = reviewdetails;
    }

}
