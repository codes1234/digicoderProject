
package digi.coders.Qaione_Education.models;

import com.google.gson.annotations.SerializedName;

public class QuizListData {

    private String id;
    @SerializedName("franchise_id")
    private String franchiseId;
    @SerializedName("teacher_id")
    private String teacherId;
    @SerializedName("quiz_id")
    private String quizId;
    @SerializedName("course_id")
    private String courseId;
    private String timing;
    private String status;
    @SerializedName("is_done")
    private String isDone;
    private String date;
    private String time;
    private Quiz quiz;
    private Course course;
    private Score resultList;

    public Score getResultList() {
        return resultList;
    }

    public void setResultList(Score resultList) {
        this.resultList = resultList;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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

    public String getIsDone() {
        return isDone;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
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
