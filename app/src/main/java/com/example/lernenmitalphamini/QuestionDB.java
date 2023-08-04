package com.example.lernenmitalphamini;

import java.util.ArrayList;
import java.util.List;

public class QuestionDB {

    private static List<QuestionList> geoQuestionsGerman(){
        final List<QuestionList> questionLists = new ArrayList<>();

        final QuestionList question1 = new QuestionList("Was ist die Hauptstadt von der Schweiz?", "Zürich", "Basel", "Bern", "Chur", "Bern", "Die Hauptstadt der Schweiz ist Bern und das bereits seit 1848.", "Berne is the capital of switzerland since 1848." );
        final QuestionList question2 = new QuestionList("Welches Land ist kein Nachbarland der Schweiz?", "Deutschland", "Luxemburg", "Italien", "Österreich", "Luxemburg", "Luxemburg ist kein Nachbarland der Schweiz. Es liegt in Westeuropa neben Belgien, Deutschland und Frankreich.", "Luxembourg does not border Switzerland.");
        final QuestionList question3 = new QuestionList("Welche Stadt hat die meisten Einwohner in der Schweiz?", "Zürich", "Luzern", "Genf", "Basel", "Zürich", "Die Stadt mit den meisten Einwohnern in der Schweiz ist Zürich. Die Einwohnerzahl von Zürich liegt bei über 400.000 Menschen, und in der Metropolregion Zürich leben sogar über 1,5 Millionen Menschen.","Zurich is the biggest city with over fourhundert thousend people living there.");

        questionLists.add(question1);
        questionLists.add(question2);
        questionLists.add(question3);

        return questionLists;
    }

    private static List<QuestionList> bioQuestionsGerman(){
        final List<QuestionList> questionLists = new ArrayList<>();

        final QuestionList question1 = new QuestionList("Wie viele Knochen hat der menschliche Körper?", "206", "219", "189", "196", "206", "Der menschliche Körper hat unglaublich viele Knochen. Das Skelett besteht normalerweise aus insgesamt 206 Knochen!", "There are over 200 bones in a human body.");
        final QuestionList question2 = new QuestionList("Was ist das grösste Organ des menschlichen Körpers?", "Herz", "Lunge", "Darm", "Haut", "Haut", "Das grösste Organ des menschlichen Körpers ist die Haut. Sie bedeckt den Körper und schützt diesen vor Verletzungen oder UV-Strahlen. ","The skin is an organ and it is even the biggest.");
        final QuestionList question3 = new QuestionList("Was ist kein Wirbeltier?", "Hai", "Laubfrosch", "Pinguin", "Biene", "Biene", "Die Bienen gehören zur Klasse der Insekten und sind wirbellose Tiere. Pinguin, Hai und Laubfrosch hingegen sind Wirbeltiere und haben ein inneres Skelett aus Knochen und Knorpeln.", "The bee is wrong. The other three animals are right.");

        questionLists.add(question1);
        questionLists.add(question2);
        questionLists.add(question3);

        return questionLists;
    }


    public static List<QuestionList> getQuestions(String selectedTopic){
        switch (selectedTopic){
            case "geo":
                return geoQuestionsGerman();
            default:
                return bioQuestionsGerman();
        }
    }

}
