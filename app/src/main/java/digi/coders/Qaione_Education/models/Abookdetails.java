
package digi.coders.Qaione_Education.models;

import com.google.gson.annotations.SerializedName;

public class Abookdetails {

    private String id;
    private String category;
    private String author;
    private String name;
    private String logo;
    private String banner;
    private String abook;
    private String type;
    private String price;
    private String offerprice;
    private String discountpercent;
    private String shortdesc;
    private String noofpages;
    private String daystofinish;
    private String description;
    private String requirement;
    @SerializedName("abook_include")
    private String abookInclude;
    @SerializedName("will_learn")
    private String willLearn;
    private String sample;
    private String apprstatus;
    private String trending;
    private String wishliststatus;
    private String reviewstatus;

    public String getWishliststatus() {
        return wishliststatus;
    }

    public void setWishliststatus(String wishliststatus) {
        this.wishliststatus = wishliststatus;
    }

    public String getReviewstatus() {
        return reviewstatus;
    }

    public void setReviewstatus(String reviewstatus) {
        this.reviewstatus = reviewstatus;
    }

    private String date;
    private String time;
    private AuthorDetails authordetails;
    private Categorydetails categorydetails;
    private Reviewdetails reviewdetails;
    @SerializedName("topicList")
    private TopicListModel[] topicListModels;

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

    public String getAbook() {
        return abook;
    }

    public void setAbook(String abook) {
        this.abook = abook;
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

    public String getDiscountpercent() {
        return discountpercent;
    }

    public void setDiscountpercent(String discountpercent) {
        this.discountpercent = discountpercent;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public String getNoofpages() {
        return noofpages;
    }

    public void setNoofpages(String noofpages) {
        this.noofpages = noofpages;
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

    public String getAbookInclude() {
        return abookInclude;
    }

    public void setAbookInclude(String ebookInclude) {
        this.abookInclude = abookInclude;
    }

    public String getWillLearn() {
        return willLearn;
    }

    public void setWillLearn(String willLearn) {
        this.willLearn = willLearn;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getApprstatus() {
        return apprstatus;
    }

    public void setApprstatus(String apprstatus) {
        this.apprstatus = apprstatus;
    }

    public String getTrending() {
        return trending;
    }

    public void setTrending(String trending) {
        this.trending = trending;
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

    public Reviewdetails getReviewdetails() {
        return reviewdetails;
    }

    public void setReviewdetails(Reviewdetails reviewdetails) {
        this.reviewdetails = reviewdetails;
    }

    public TopicListModel[] getTopicListModels() {
        return topicListModels;
    }

    public void setTopicListModels(TopicListModel[] topicListModels) {
        this.topicListModels = topicListModels;
    }

}
