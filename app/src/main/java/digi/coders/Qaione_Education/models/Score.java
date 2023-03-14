
package digi.coders.Qaione_Education.models;

import com.google.gson.annotations.SerializedName;

public class Score {

    private String id;
    @SerializedName("franchise_id")
    private String franchiseId;
    @SerializedName("teacher_id")
    private String teacherId;
    @SerializedName("student_id")
    private String studentId;
    @SerializedName("schedule_id")
    private String scheduleId;
    @SerializedName("quiz_id")
    private String quizId;
    private String right;
    private String wrong;
    private String score;
    private String timing;
    private String status;
    private String date;
    private String time;
    private AnsBook[] ansBook;
    private String rank;
    private String out_of_rank;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getOut_of_rank() {
        return out_of_rank;
    }

    public void setOut_of_rank(String out_of_rank) {
        this.out_of_rank = out_of_rank;
    }

    public AnsBook[] getAnsBook() {
        return ansBook;
    }

    public void setAnsBook(AnsBook[] ansBook) {
        this.ansBook = ansBook;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(String franchiseId) {
        this.franchiseId = franchiseId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getWrong() {
        return wrong;
    }

    public void setWrong(String wrong) {
        this.wrong = wrong;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
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
