
package digi.coders.Qaione_Education.models;


import com.google.gson.annotations.SerializedName;

public class Item {

    private String id;
    private String category;
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
    private String willLearn;
    private String demovedio;
    private String apprstatus;
    private AuthorDetails author;
    private String certification;
    private String discountpercent;
    private String trending;
    private String videos;
    private String date;
    private String time;
    @SerializedName("puchase_status")
    private String purchaseStatus;
    public String getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(String purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public AuthorDetails getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDetails author) {
        this.author = author;
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

    public String getDiscountpercent() {
        return discountpercent;
    }

    public void setDiscountpercent(String discountpercent) {
        this.discountpercent = discountpercent;
    }

    /*public AuthorDetails getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDetails author) {
        this.author = author;
    }*/

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

}
