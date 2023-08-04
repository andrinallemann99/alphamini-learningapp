package com.example.lernenmitalphamini;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ubtrobot.commons.Priority;
import com.ubtrobot.mini.libs.behaviors.Behavior;
import com.ubtrobot.mini.libs.behaviors.BehaviorInflater;
import com.ubtrobot.mini.libs.behaviors.BehaviorStopCaused;

import java.io.FileNotFoundException;


public class AchievementActivity extends AppCompatActivity {
    private TextView tvusername, tvpoints;
    private Button newGameBtn;
    private String username, points;
    private String questionAmount;
    private int pointsInteger, questionAmountInteger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        username = getIntent().getStringExtra("username");
        pointsInteger = getIntent().getIntExtra("points", 0);
        points = String.valueOf(getIntent().getIntExtra("points", 0));
        questionAmountInteger = getIntent().getIntExtra("questionAmount", 0);
        questionAmount = String.valueOf(getIntent().getIntExtra("questionAmount", 0));

        tvusername = findViewById(R.id.tvUserNameAchievement);
        tvpoints = findViewById(R.id.tvEndPointsAchievement);
        newGameBtn = findViewById(R.id.newGameBtn);
        newGameBtn.setEnabled(false);

        tvusername.setText(username);
        tvpoints.setText(points + " / " + questionAmount);

        double pointRate = (pointsInteger*100)/questionAmountInteger;

        //Double pointRate = Double.parseDouble(points)/Double.parseDouble(questionAmount);
        String filePath = "/sdcard/behaviors/behavior_achievement.xml";

        if (pointRate < 25){
            try {
                RobotAPI.changeContentInXMLBehaviourFile("/sdcard/behaviors/behavior_achievement.xml", "You need to learn more. You got "+ points +" out of "+ questionAmount + ". You want to go again?", "tts", "text");
                RobotAPI.changeContentInXMLBehaviourFile("/sdcard/behaviors/behavior_achievement.xml", "action_020", "action", "name");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else if (pointRate <50){
            try {
                RobotAPI.changeContentInXMLBehaviourFile("/sdcard/behaviors/behavior_achievement.xml", "You will get there. You got "+ points +" out of "+ questionAmount + ". You want to go again?", "tts", "text");
                RobotAPI.changeContentInXMLBehaviourFile("/sdcard/behaviors/behavior_achievement.xml", "Surveillance_003", "action", "name");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else if (pointRate <75){
            try {
                RobotAPI.changeContentInXMLBehaviourFile("/sdcard/behaviors/behavior_achievement.xml", "That is good but it can be a little bit better. You got "+ points +" out of "+ questionAmount + ". You want to go again?", "tts", "text");
                RobotAPI.changeContentInXMLBehaviourFile("/sdcard/behaviors/behavior_achievement.xml", "action_006", "action", "name");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else {
            try {
                RobotAPI.changeContentInXMLBehaviourFile("/sdcard/behaviors/behavior_achievement.xml", "You are good in this topic. You got "+ points +" out of "+ questionAmount + ". You want to go again?", "tts", "text");
                RobotAPI.changeContentInXMLBehaviourFile("/sdcard/behaviors/behavior_achievement.xml", "action_004", "action", "name");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        try{
            Behavior behavior =
                    BehaviorInflater.loadBehaviorFromXml(filePath);
            behavior.setPriority(Priority.NORMAL);
            behavior.start();
            behavior.setBehaviorListener(new Behavior.BehaviorListener() {
                @Override
                public void onCompleted(BehaviorStopCaused behaviorStopCaused) {
                    newGameBtn.setEnabled(true);
                    Log.w("logic", "USERNAME completed");

                }
            });
        }catch(FileNotFoundException e){

        }

        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame();
            }
        });
    }

    public void startNewGame(){
        Intent startIntent = new Intent(AchievementActivity.this, MainActivity.class);
        startIntent.putExtra("username", username);
        startActivity(startIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startNewGame();
    }
}
