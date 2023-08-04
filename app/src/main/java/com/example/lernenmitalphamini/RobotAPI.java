package com.example.lernenmitalphamini;

import android.content.Intent;
import android.util.Log;
import android.widget.LinearLayout;

import com.ubtrobot.commons.Priority;
import com.ubtrobot.mini.libs.behaviors.Behavior;
import com.ubtrobot.mini.libs.behaviors.BehaviorInflater;
import com.ubtrobot.mini.libs.behaviors.BehaviorStopCaused;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class RobotAPI {

    private static String lastCalledRightAnswerBehavior = "";
    private static String lastCalledWrongAnswerBehavior = "";
    private static List<String> xmlRightAnswerList = new ArrayList<String>();
    private static List<String> xmlWrongAnswerList = new ArrayList<String>();
    private static Random randomRightAnswer = new Random();
    private static Random randomWrongAnswer = new Random();

    public static void executeRightBehaviourFromFile() throws FileNotFoundException {

        String rightAnswer1 = "/sdcard/behaviors/behavior_right_answer1.xml";
        String rightAnswer2 = "/sdcard/behaviors/behavior_right_answer2.xml";
        String rightAnswer3 = "/sdcard/behaviors/behavior_right_answer3.xml";

        xmlRightAnswerList.add(rightAnswer1);
        xmlRightAnswerList.add(rightAnswer2);
        xmlRightAnswerList.add(rightAnswer3);

        int randomInt = randomRightAnswer.nextInt(xmlRightAnswerList.size());
        String xmlName = xmlRightAnswerList.get(randomInt);

        while(xmlName.equals(lastCalledRightAnswerBehavior)){
            randomInt = randomRightAnswer.nextInt(xmlRightAnswerList.size());
            xmlName = xmlRightAnswerList.get(randomInt);
        }

        lastCalledRightAnswerBehavior = xmlName;

        Behavior behavior =
                BehaviorInflater.loadBehaviorFromXml(lastCalledRightAnswerBehavior);
        behavior.setPriority(Priority.NORMAL);
        behavior.start();
        behavior.setBehaviorListener(new Behavior.BehaviorListener() {
            @Override
            public void onCompleted(BehaviorStopCaused behaviorStopCaused) {

            }
        });

    }
    public static void executeWrongBehaviourFromFile() throws FileNotFoundException {

        String wrongAnswer1 = "/sdcard/behaviors/behavior_wrong_answer1.xml";
        String wrongAnswer2 = "/sdcard/behaviors/behavior_wrong_answer2.xml";
        String wrongAnswer3 = "/sdcard/behaviors/behavior_wrong_answer3.xml";

        xmlWrongAnswerList.add(wrongAnswer1);
        xmlWrongAnswerList.add(wrongAnswer2);
        xmlWrongAnswerList.add(wrongAnswer3);

        int randomInt = randomWrongAnswer.nextInt(xmlWrongAnswerList.size());
        String xmlName = xmlWrongAnswerList.get(randomInt);

        while(xmlName.equals(lastCalledWrongAnswerBehavior)){
            randomInt = randomWrongAnswer.nextInt(xmlWrongAnswerList.size());
            xmlName = xmlWrongAnswerList.get(randomInt);
        }

        lastCalledWrongAnswerBehavior = xmlName;

        Behavior behavior =
                BehaviorInflater.loadBehaviorFromXml(lastCalledWrongAnswerBehavior);
        behavior.setPriority(Priority.NORMAL);
        behavior.start();
        behavior.setBehaviorListener(new Behavior.BehaviorListener() {
            @Override
            public void onCompleted(BehaviorStopCaused behaviorStopCaused) {

            }

        });

    }

    public static void executeStartupBehaviourFromFile() throws FileNotFoundException {

        Behavior behavior =
                BehaviorInflater.loadBehaviorFromXml("/sdcard/behaviors/behavior_startup.xml");
        behavior.setPriority(Priority.NORMAL);
        behavior.start();
        behavior.setBehaviorListener(new Behavior.BehaviorListener() {
            @Override
            public void onCompleted(BehaviorStopCaused behaviorStopCaused) {

                Log.w("logic", "behavior completed");
            }
        });

    }


    public static void executeTimerRunsOutBehaviorFromFile() throws FileNotFoundException {
        Behavior behavior =
                BehaviorInflater.loadBehaviorFromXml("/sdcard/behaviors/behavior_timer_runout.xml");
        behavior.setPriority(Priority.NORMAL);
        behavior.start();
        behavior.setBehaviorListener(new Behavior.BehaviorListener() {
            @Override
            public void onCompleted(BehaviorStopCaused behaviorStopCaused) {
                Log.w("logic", "behavior completed");
            }
        });
    }


    public static Behavior executeExitGameBehaviorFromFile() throws FileNotFoundException {

        Behavior behavior =
                BehaviorInflater.loadBehaviorFromXml("/sdcard/behaviors/behavior_exit_game.xml");
        behavior.setPriority(Priority.NORMAL);
        return behavior;

    }


    public static void changeContentInXMLBehaviourFile(String filePath, String message, String element, String attribute) throws FileNotFoundException {

        String xmlFilePath = "file://"+filePath;
        String elementName = element;
        String attributeName = attribute;
        String newValue = message;

        try {
            // Lade das XML-File
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xmlFilePath);

            // Navigiere zum gewünschten Element
            NodeList nodeList = doc.getElementsByTagName(elementName);
            if (nodeList.getLength() > 0) {
                Element newElement = (Element) nodeList.item(0);

                // Aktualisiere das Attribut
                newElement.setAttribute(attributeName, newValue);

                // Speichere die Änderungen im XML-File
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                DOMSource source = new DOMSource(doc);

                FileOutputStream fos = new FileOutputStream(new File(filePath));
                StreamResult result = new StreamResult(fos);
                transformer.transform(source, result);

                System.out.println("Änderungen wurden im XML-File gespeichert.");

            } else {
                System.out.println("Element nicht gefunden.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
