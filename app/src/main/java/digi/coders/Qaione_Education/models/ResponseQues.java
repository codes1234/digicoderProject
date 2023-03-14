package digi.coders.Qaione_Education.models;

public class ResponseQues {
    private String questionId;
    private String ansOption;

    public ResponseQues(String questionId, String ansOption) {
        this.questionId = questionId;
        this.ansOption = ansOption;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnsOption() {
        return ansOption;
    }

    public void setAnsOption(String ansOption) {
        this.ansOption = ansOption;
    }
}
