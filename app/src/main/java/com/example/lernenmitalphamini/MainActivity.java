package com.example.lernenmitalphamini;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ubtrobot.commons.Priority;
import com.ubtrobot.mini.libs.behaviors.Behavior;
import com.ubtrobot.mini.libs.behaviors.BehaviorInflater;
import com.ubtrobot.mini.libs.behaviors.BehaviorStopCaused;

import java.io.FileNotFoundException;
public class MainActivity extends AppCompatActivity {

    private static String selectedTopicName = "";
    private long backPressedTime;
    private LinearLayout geo;
    private LinearLayout bio;
    private static TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!getIntent().hasExtra("username")){
            try {
                RobotAPI.executeStartupBehaviourFromFile();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        geo = findViewById(R.id.geografie_btn);
        bio = findViewById(R.id.biologie_btn);
        tvName = findViewById(R.id.tvName);
        tvName.setText(getIntent().getStringExtra("username"));

        final Button startBtn = findViewById(R.id.startButton);
        selectedTopicName = "";

        geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTopicName = "geo";
                geo.setBackgroundResource(R.drawable.round_back_white_stroke10);
                bio.setBackgroundResource(R.drawable.round_back_white10);
            }
        });

        bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTopicName = "bio";
                bio.setBackgroundResource(R.drawable.round_back_white_stroke10);
                geo.setBackgroundResource(R.drawable.round_back_white10);
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvName.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, R.string.enterUsername, Toast.LENGTH_SHORT).show();
                }else{
                    if(selectedTopicName.isEmpty()){
                        Toast.makeText(MainActivity.this, R.string.selectTopicToast, Toast.LENGTH_SHORT).show();
                    }else{
                        String username = tvName.getText().toString();
                        startBtn.setClickable(false);
                        bio.setClickable(false);
                        geo.setClickable(false);
                        tvName.setEnabled(false);

                        //has to be here for method onCompleted
                            if (selectedTopicName.equals("geo")){
                                try {
                                    String filePath = "/sdcard/behaviors/behavior_start_game.xml";
                                    if (!getIntent().hasExtra("username")){
                                        try {
                                            RobotAPI.changeContentInXMLBehaviourFile(filePath, "Hello " + username + "! Lets learn some Geography!", "tts", "text");
                                        } catch (FileNotFoundException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }else{
                                        try {
                                            RobotAPI.changeContentInXMLBehaviourFile(filePath, "Great, lets learn again!", "tts", "text");
                                        } catch (FileNotFoundException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }

                                    Behavior behavior =
                                            BehaviorInflater.loadBehaviorFromXml(filePath);
                                    behavior.setPriority(Priority.NORMAL);
                                    behavior.start();
                                    behavior.setBehaviorListener(new Behavior.BehaviorListener() {
                                        @Override
                                        public void onCompleted(BehaviorStopCaused behaviorStopCaused) {
                                            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                                            intent.putExtra("selectedTopic", selectedTopicName);
                                            intent.putExtra("username", tvName.getText().toString());

                                            startActivity(intent);
                                            Log.w("logic", "USERNAME completed");
                                        }
                                    });
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }

                            }else{
                                try {
                                    String filePath = "/sdcard/behaviors/behavior_start_game.xml";
                                    if (!getIntent().hasExtra("username")){
                                        try {
                                            RobotAPI.changeContentInXMLBehaviourFile(filePath, "Hello " + username + "! Lets learn some Biology!", "tts", "text");
                                        } catch (FileNotFoundException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }else{
                                        try {
                                            RobotAPI.changeContentInXMLBehaviourFile(filePath, "Great, lets learn again!", "tts", "text");
                                        } catch (FileNotFoundException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }

                                    Behavior behavior =
                                            BehaviorInflater.loadBehaviorFromXml(filePath);
                                    behavior.setPriority(Priority.NORMAL);
                                    behavior.start();
                                    behavior.setBehaviorListener(new Behavior.BehaviorListener() {
                                        @Override
                                        public void onCompleted(BehaviorStopCaused behaviorStopCaused) {
                                            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                                            intent.putExtra("selectedTopic", selectedTopicName);
                                            intent.putExtra("username", tvName.getText().toString());

                                            startActivity(intent);
                                            Log.w("logic", "USERNAME completed");
                                        }
                                    });
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
        });

    }


    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            try {
                Behavior behavior = RobotAPI.executeExitGameBehaviorFromFile();
                behavior.start();
                behavior.setBehaviorListener(new Behavior.BehaviorListener() {
                    @Override
                    public void onCompleted(BehaviorStopCaused behaviorStopCaused) {
                        Log.w("logic", "behavior completed");
                    }
                });
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            finishAffinity();
        }else{
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        }
        backPressedTime = System.currentTimeMillis();
    }

}