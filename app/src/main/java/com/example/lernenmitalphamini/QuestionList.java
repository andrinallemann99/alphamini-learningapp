package com.example.lernenmitalphamini;

public class QuestionList {

    private String question, option1, option2, option3, option4, answer, description;
    //private String userSelectAnswer;
    private String miniAnswer;

    public QuestionList(String question, String option1, String option2, String option3, String option4,
                        String answer, String description, String miniAnswer) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.description = description;
        this.miniAnswer = miniAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public String getAnswer() {
        return answer;
    }

    public String getDescription() {
        return description;
    }

    public String getMiniAnswer() {
        return miniAnswer;
    }

    public String getOption(int i){
        switch (i){
            case 0:
                return getOption1();
            case 1:
                return getOption2();
            case 2:
                return getOption3();
            case 3:
                return getOption4();
        }
        return null;
    }
}
