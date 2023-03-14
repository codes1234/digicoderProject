
package digi.coders.Qaione_Education.models;


public class VideoPlaylist {

    private String id;
    private Coursedetails course;
    private VideoDetails video;
    private String lectureNo;
    private String status;
    private String date;
    private String time;
    private Assignment[] assignment;
    private Question[] question;
    private VideoDetails[] upcommingList;
    private VideoDetails[] watchAgainList;


    public Question[] getQuestion() {
        return question;
    }

    public void setQuestion(Question[] question) {
        this.question = question;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Coursedetails getCourse() {
        return course;
    }

    public void setCourse(Coursedetails course) {
        this.course = course;
    }

    public VideoDetails getVideo() {
        return video;
    }

    public void setVideo(VideoDetails video) {
        this.video = video;
    }

    public String getLectureNo() {
        return lectureNo;
    }

    public void setLectureNo(String lectureNo) {
        this.lectureNo = lectureNo;
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

    public Assignment[] getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment[] assignment) {
        this.assignment = assignment;
    }

    public VideoDetails[] getUpcommingList() {
        return upcommingList;
    }

    public void setUpcommingList(VideoDetails[] upcommingList) {
        this.upcommingList = upcommingList;
    }

    public VideoDetails[] getWatchAgainList() {
        return watchAgainList;
    }

    public void setWatchAgainList(VideoDetails[] watchAgainList) {
        this.watchAgainList = watchAgainList;
    }

}
