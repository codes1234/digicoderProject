
package digi.coders.Qaione_Education.models;

public class RecommendedVideos {

    private String id;
    private SubjectDetails subject;
    private VideoDetails video;
    private String description;
    private String status;
    private String date;
    private String time;
    private AuthorDetails author;

    public AuthorDetails getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDetails author) {
        this.author = author;
    }

    public SubjectDetails getSubject() {
        return subject;
    }

    public void setSubject(SubjectDetails subject) {
        this.subject = subject;
    }

    public VideoDetails getVideo() {
        return video;
    }

    public void setVideo(VideoDetails video) {
        this.video = video;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
