package digi.coders.Qaione_Education.models;

public class QuizData {

    private QuizListData[] completedList;
    private QuizListData[] unattemptedList;

    public QuizData(QuizListData[] completedList, QuizListData[] unattemptedList) {
        this.completedList = completedList;
        this.unattemptedList = unattemptedList;
    }

    public QuizListData[] getCompletedList() {
        return completedList;
    }

    public void setCompletedList(QuizListData[] completedList) {
        this.completedList = completedList;
    }

    public QuizListData[] getUnattemptedList() {
        return unattemptedList;
    }

    public void setUnattemptedList(QuizListData[] unattemptedList) {
        this.unattemptedList = unattemptedList;
    }
}
