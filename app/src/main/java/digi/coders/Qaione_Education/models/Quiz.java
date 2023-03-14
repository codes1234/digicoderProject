
package digi.coders.Qaione_Education.models;

import com.google.gson.annotations.SerializedName;

public class Quiz {

    private String id;
    private String name;
    private String questions;
    @SerializedName("no_of_questions")
    private String noOfQuestions;
    @SerializedName("per_question_no")
    private String perQuestionNo;
    private String timing;
    private String description;
    private String status;
    private String date;
    private String time;

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

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getNoOfQuestions() {
        return noOfQuestions;
    }

    public void setNoOfQuestions(String noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }

    public String getPerQuestionNo() {
        return perQuestionNo;
    }

    public void setPerQuestionNo(String perQuestionNo) {
        this.perQuestionNo = perQuestionNo;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
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
