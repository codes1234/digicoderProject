package digi.coders.Qaione_Education.models;

public class QuestionListData {

    private String action;
    private QuizLData[] list;
    private Quiz[] quizData;
    private Questio[] questionslist;

    public QuizLData[] getList() {
        return list;
    }

    public void setList(QuizLData[] list) {
        this.list = list;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Quiz[] getQuizData() {
        return quizData;
    }

    public void setQuizData(Quiz[] quizData) {
        this.quizData = quizData;
    }

    public Questio[] getQuestionslist() {
        return questionslist;
    }

    public void setQuestionslist(Questio[] questionslist) {
        this.questionslist = questionslist;
    }
}
