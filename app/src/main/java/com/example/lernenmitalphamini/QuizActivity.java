package com.example.lernenmitalphamini;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.ubtrobot.commons.Priority;
import com.ubtrobot.mini.libs.behaviors.Behavior;
import com.ubtrobot.mini.libs.behaviors.BehaviorInflater;
import com.ubtrobot.mini.libs.behaviors.BehaviorStopCaused;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    //30 seconds Timer
    private static final long TIMER_IN_MILLIES = 30000;

      //GUI Elements
    private TextView tvQuestionNumber, tvQuestion, tvPoints, tvUserName, tvTimer;
    private TextView tvInfoQuestion, tvInfoAnswer;
    private LinearLayout infoLayout, fullScreen;
    private ImageView infoImage;
    private Button button1, button2, button3, button4, btnFullScreen;
    private ProgressBar progressBar;

    //necessary for get Intent from StartActivity
    private int questionAmount;

        //variables for Userface
    private static int count = 0;
    private String userName;
    private int points = 0;

    //For Timer to change to red when under 10 seconds
    private ColorStateList textColorDefaultTimer;

    //Timer
    private CountDownTimer countDownTimer;
    private long timeLeftInMillies;

    //Colors of the buttons as finals Strings
    private final String btnCorrectColor = "#46d82f";
    private final String btnWrongColor = "#ef1515";

    private static List<QuestionList> questionList;

    private Behavior infoBehavior;

    private String getSelectedTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        getSelectedTopic = getIntent().getStringExtra("selectedTopic");
        userName = getIntent().getStringExtra("username");

        //GUI Elements
        tvUserName = findViewById(R.id.tvUserName);
        tvUserName.setText(userName);
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(Integer.toString(points));
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestion = findViewById(R.id.question);
        tvQuestionNumber = findViewById(R.id.questionNumber);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        tvInfoQuestion = findViewById(R.id.tvInfoQuestion);
        tvInfoAnswer = findViewById(R.id.tvInfoAnswer);
        infoLayout = findViewById(R.id.infoLayout);
        fullScreen = findViewById(R.id.fullScreen);
        infoImage = findViewById(R.id.categoryImage);

        //btnFullScreen = findViewById(R.id.btnFullScreen);

        //Progressbar on the Screen
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(count);

        textColorDefaultTimer = tvTimer.getTextColors();

        questionList = QuestionDB.getQuestions(getSelectedTopic);
        questionAmount = questionList.size();

        getNewQuestion();

        if(getSelectedTopic.equals("geo")){
            infoImage.setImageResource(R.drawable.globus);
        }else{
            infoImage.setImageResource(R.drawable.biologie);
        }


        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoLayout.setVisibility(View.VISIBLE);
                fullScreen.setVisibility(View.GONE);

                String question = questionList.get(count-1).getQuestion();
                tvInfoQuestion.setText(question);
                String description = questionList.get(count-1).getDescription();
                tvInfoAnswer.setText(description);

                //neu hinzugefügt
                String miniResponse = questionList.get(count-1).getMiniAnswer();

                try {
                    RobotAPI.changeContentInXMLBehaviourFile("/sdcard/behaviors/behavior_mini_answer.xml", miniResponse, "tts", "text");
                    try {
                        infoBehavior = BehaviorInflater.loadBehaviorFromXml("/sdcard/behaviors/behavior_mini_answer.xml");
                        infoBehavior.setPriority(Priority.NORMAL);
                        infoBehavior.start();
                        Log.d("behavior", "started");
                        infoBehavior.setBehaviorListener(new Behavior.BehaviorListener() {
                            @Override
                            public void onCompleted(BehaviorStopCaused behaviorStopCaused) {

                                Log.w("logic", "behavior mini Answer completed");
                            }
                        });
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                infoBehavior.stop();
                getNewQuestion();

                //disable Fullscreen Button so you can select an answer
                infoLayout.setVisibility(View.GONE);
                setButtonToDefault(button1);
                setButtonToDefault(button2);
                setButtonToDefault(button3);
                setButtonToDefault(button4);
            }
        });


        //for each button exists a listener and checks if the answer is correct
        //if correct you earn 1 point
        //if correct changes the button to green, else to red and the right answer shows green
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(button1.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button1.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    points++;
                    try {RobotAPI.executeRightBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }else if(button2.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button1.setBackgroundColor(Color.parseColor(btnWrongColor));
                    button2.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    try {RobotAPI.executeWrongBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }else if(button3.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button1.setBackgroundColor(Color.parseColor(btnWrongColor));
                    button3.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    try {RobotAPI.executeWrongBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }else if(button4.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button1.setBackgroundColor(Color.parseColor(btnWrongColor));
                    button4.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    try {RobotAPI.executeWrongBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }
                checkAnswers();

            }
        });
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(button1.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button1.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    button2.setBackgroundColor(Color.parseColor(btnWrongColor));
                    try {RobotAPI.executeWrongBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }else if(button2.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button2.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    points++;
                    try {RobotAPI.executeRightBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }else if(button3.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button2.setBackgroundColor(Color.parseColor(btnWrongColor));
                    button3.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    try {RobotAPI.executeWrongBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }else if(button4.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button2.setBackgroundColor(Color.parseColor(btnWrongColor));
                    button4.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    try {RobotAPI.executeWrongBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }
                checkAnswers();
            }
        });
        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(button1.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button1.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    button3.setBackgroundColor(Color.parseColor(btnWrongColor));
                    try {RobotAPI.executeWrongBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }else if(button2.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button2.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    button3.setBackgroundColor(Color.parseColor(btnWrongColor));
                    try {RobotAPI.executeWrongBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }else if(button3.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button3.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    points++;
                    try {RobotAPI.executeRightBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }else if(button4.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button3.setBackgroundColor(Color.parseColor(btnWrongColor));
                    button4.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    try {RobotAPI.executeWrongBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }
                checkAnswers();
            }
        });
        button4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(button1.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button1.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    button4.setBackgroundColor(Color.parseColor(btnWrongColor));
                    try {RobotAPI.executeWrongBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }else if(button2.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button2.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    button4.setBackgroundColor(Color.parseColor(btnWrongColor));
                    try {RobotAPI.executeWrongBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }else if(button3.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button3.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    button4.setBackgroundColor(Color.parseColor(btnWrongColor));
                    try {RobotAPI.executeWrongBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }else if(button4.getText().toString().equals(questionList.get(count-1).getAnswer())){
                    button4.setBackgroundColor(Color.parseColor(btnCorrectColor));
                    points++;
                    try {RobotAPI.executeRightBehaviourFromFile();} catch (FileNotFoundException e) {throw new RuntimeException(e);}
                }
                checkAnswers();
            }
        });

    }

    public Button setButtonToDefault(Button btn){
        btn.setBackgroundColor(getResources().getColor(R.color.white));
        return btn;
    }

    public static List<QuestionList> getQuestionList(){
        return questionList;
    }

    public static Integer getCount(){
        return count;
    }

    private void getNewQuestion() {
        //when you are not at the last question parse the HTML Elements to String (ex. &039;) (without special characters)
        if (count<questionAmount){
            String question = questionList.get(count).getQuestion();

            //set the text in teh buttons randomly
            setButtonText();

            //set the textview and show the question
            tvQuestion.setText(question);
            tvQuestion.setVisibility(View.VISIBLE);

            //set the Progressbar to the % of the progress
            progressBar.setProgress(count*(100/questionAmount));

            count++;
            tvQuestionNumber.setText("Frage "+count);

            //start Countdown
            timeLeftInMillies = TIMER_IN_MILLIES;
            startCountDown();

        }else{
            //set Intent for the next Activity which shows the Results
            Intent intent = new Intent(QuizActivity.this, AchievementActivity.class);
            intent.putExtra("username", userName);
            intent.putExtra("points", points);
            intent.putExtra("questionAmount", questionAmount);
            startActivity(intent);

            //set points and count to 0
            this.points = 0;
            this.count = 0;
        }
    }

    //check Answers and cancel the Timer. Set the points and acitvate the Full Screen Button
    private void checkAnswers() {
        countDownTimer.cancel();

        tvPoints.setText(Integer.toString(points));
        fullScreen.setVisibility(View.VISIBLE);
    }

    //start the Timer (30s)
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillies, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillies = l;
                updateTimerText();

            }

            @Override
            public void onFinish() {
                timeLeftInMillies = 0;
                updateTimerText();
                showAnswers();
            }
        }.start();
    }

    //update the Timer Text in the textView
    private void updateTimerText(){
        int minutes = (int) (timeLeftInMillies / 1000) / 60;
        int seconds = (int) (timeLeftInMillies / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimer.setText(timeFormatted);

        //under 10 seconds, the color changes to red
        if(timeLeftInMillies<10000){
            tvTimer.setTextColor(Color.RED);
        }else{
            tvTimer.setTextColor(textColorDefaultTimer);
        }
    }

    //method to show answers when the timer is at 00:00 and no answer is selected
    private void showAnswers() {
        if(button1.getText().toString().equals(questionList.get(count-1).getAnswer())){
            button1.setBackgroundColor(Color.parseColor(btnCorrectColor));
        }else if(button2.getText().toString().equals(questionList.get(count-1).getAnswer())){
            button2.setBackgroundColor(Color.parseColor(btnCorrectColor));
        }else if(button3.getText().toString().equals(questionList.get(count-1).getAnswer())){
            button3.setBackgroundColor(Color.parseColor(btnCorrectColor));
        }else if(button4.getText().toString().equals(questionList.get(count-1).getAnswer())){
            button4.setBackgroundColor(Color.parseColor(btnCorrectColor));
        }
        try{
            RobotAPI.executeTimerRunsOutBehaviorFromFile();
        }catch (FileNotFoundException e){

        }

        checkAnswers();
    }

    //set the answers in the 4 Buttons randomly
    private void setButtonText() {
        Random rand = new Random();
        int value = rand.nextInt(4);

        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);

       //set the buttontext on all 4 Buttons randomly and parse the HTML Elements so it is readable
        button1.setText(questionList.get(count).getOption(list.get(value)));
        Log.d("State", button1.getText().toString());
        list.remove(value);
        value = rand.nextInt(3);
        button2.setText(questionList.get(count).getOption(list.get(value)));
        list.remove(value);
        value = rand.nextInt(2);
        button3.setText(questionList.get(count).getOption(list.get(value)));
        list.remove(value);
        value = rand.nextInt(1);
        button4.setText(questionList.get(count).getOption(list.get(value)));
    }

    //when app is destroyed, the timer needs to stop or there is an error
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    //shows an Dialog for when the user wants to leave the quiz in the Questions
    //Dialog can be dismissed or accepted
    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Willst du schon gehen? Der Fortschritt wird nicht gespeichert!");
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                if(countDownTimer != null){
                    countDownTimer.cancel();
                }

                Intent intent = new Intent(QuizActivity.this, AchievementActivity.class);
                intent.putExtra("username", userName);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }
}
