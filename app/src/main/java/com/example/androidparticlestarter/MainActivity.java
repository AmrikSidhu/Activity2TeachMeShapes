package com.example.androidparticlestarter;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.ParticleEvent;
import io.particle.android.sdk.cloud.ParticleEventHandler;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.utils.Async;

public class MainActivity extends AppCompatActivity {
    // MARK: Debug info
    private final String TAG="AssignmentTowShapes";
    // MARK: Particle Account Info
    private final String PARTICLE_USERNAME = "eramriksidhu@gmail.com";
    private final String PARTICLE_PASSWORD = "gill@000";

    // MARK: Particle device-specific info
    private final String DEVICE_ID = "2b0040000f47363333343437";


    // MARK: Particle Publish / Subscribe variables
    private long subscriptionId;

    // MARK: Particle device
    private ParticleDevice mDevice;

    TextView txtAnswer;
    TextView txtQuez;
    TextView getTxtQuezText;
    ImageView img;
    TextView txtScores;

    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.txtAnswer = findViewById(R.id.txtAnswer);
        this.img = findViewById(R.id.imageViewAct1);
        this.txtQuez = findViewById(R.id.textOptions);
        this.getTxtQuezText = findViewById(R.id.txtQuez2);
        this.txtScores = findViewById(R.id.txtScores);

        // 1. Initialize your connection to the Particle API
        ParticleCloudSDK.init(this.getApplicationContext());

        // 2. Setup your device variable
        getDeviceFromCloud();

    }


    /**
     * Custom function to connect to the Particle Cloud and get the device
     */
    public void getDeviceFromCloud() {
        // This function runs in the background
        // It tries to connect to the Particle Cloud and get your device
        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                particleCloud.logIn(PARTICLE_USERNAME, PARTICLE_PASSWORD);
                mDevice = particleCloud.getDevice(DEVICE_ID);

                //Set the Activity Number on Particle to 1.
                List<String> functionParameters = new ArrayList<String>();
                functionParameters.add("1");
                try {
                    mDevice.callFunction("setActivityNumber", functionParameters);
                    Log.d(TAG, "Successfully set Activity Number");
                }
                catch (ParticleDevice.FunctionDoesNotExistException e1) {
                    e1.printStackTrace();
                }
                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "Successfully got device from Cloud");
                subscribeToParticleEvents();
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                Log.d(TAG, exception.getBestMessage());
            }
        });
    }
    public void showShape1(){
        this.img.setImageResource(R.drawable.start);
        this.getTxtQuezText.setText("\n\tChoose correct title of this shape and press button \n\tnumber.\n\tButton 1: Rectangle\n\tButton 2: Star\n\n\n\tClick Button from the particle device\n\t");
    }

    public void showShape2(){
        this.img.setImageResource(0);
        this.img.setImageResource(R.drawable.circle);
        this.getTxtQuezText.setText("\n\tChoose correct title of this shape and press button \n\tnumber.\n\tButton 1: Circle\n\tButton 2: Rectangle\n\n\n\tClick Button from the particle device\n\t");
    }
    public void subscribeToParticleEvents() {
        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                subscriptionId = ParticleCloudSDK.getCloud().subscribeToAllEvents(
                        "selectedOption",  // the first argument, "eventNamePrefix", is optional
                        new ParticleEventHandler() {
                            public void onEvent(String eventName, ParticleEvent event) {
                                Log.i(TAG, "Received event with payload: " + event.dataPayload);
                                String choice = event.dataPayload;

                                //If no answer is selected, only then increase the score
                                if (txtAnswer.getCurrentTextColor() == Color.BLACK){

                                    if (choice.contentEquals("2")) {
                                        if (img.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.start,getTheme()).getConstantState()){
                                            System.out.println("SHAPE IS STAR");
                                            indicateCorrectAnswer();
                                            txtAnswer.setVisibility(View.VISIBLE);
                                            txtAnswer.setTextColor(Color.BLUE);
                                            txtAnswer.setText("True!");
                                            getTxtQuezText.setText("Star is correct answer\nPress & Hold Button 4 for next Shape.");
                                            score = score + 1;
                                        }
                                        else{
                                            System.out.println("SHAPE IS STAR");
                                            indicateIncorrectAnswer();
                                            txtAnswer.setVisibility(View.VISIBLE);
                                            txtAnswer.setTextColor(Color.RED);
                                            txtAnswer.setText("False!");
                                            getTxtQuezText.setText("Incorrect Answer\nPress & Hold Button 3 for previous Shape.");
                                            score = score - 1;
                                        }


                                    }
                                    else if (choice.contentEquals("1")) {
                                        if (img.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.start,getTheme()).getConstantState()){
                                            System.out.println("Circle was the answer!");
                                            indicateIncorrectAnswer();
                                            txtAnswer.setVisibility(View.VISIBLE);
                                            txtAnswer.setTextColor(Color.RED);
                                            txtAnswer.setText("False!");
                                            getTxtQuezText.setText("Incorrect Answer\nPress & Hold Button 4 for next Shape.");
                                            score = score - 1;
                                        }
                                        else{
                                            indicateCorrectAnswer();
                                            txtAnswer.setVisibility(View.VISIBLE);
                                            txtAnswer.setTextColor(Color.BLUE);
                                            txtAnswer.setText("True!");
                                            getTxtQuezText.setText("Circle is correct answer!\nPress & Hold Button 3 for previous Shape.");
                                            score = score + 1;
                                        }

                                    }
                                }

                                if (choice.contentEquals("next")) {
                                    showShape2();
                                    txtAnswer.setTextColor(Color.BLACK);
                                    txtAnswer.setText(null);
                                }
                                if (choice.contentEquals("prev")) {
                                    showShape1();
                                    txtAnswer.setTextColor(Color.BLACK);
                                    txtAnswer.setText(null);
                                }
                                if (choice.contentEquals("showScore")){
                                    try{
                                        txtScores.setText("Score: "+mDevice.getIntVariable("scoreCount"));
                                    }
                                    catch (Exception e){
                                        System.out.println("Error getting Score");
                                    }

                                }

                                System.out.println("Score: "+score);
                            }

                            public void onEventError(Exception e) {
                                Log.e(TAG, "Event error: ", e);
                            }
                        });


                return -1;
            }

            public void indicateCorrectAnswer() {

                Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
                    @Override
                    public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                        // put your logic here to talk to the particle
                        // --------------------------------------------
                        List<String> functionParameters = new ArrayList<String>();
                        functionParameters.add("green");
                        try {
                            mDevice.callFunction("showAnswerResult", functionParameters);

                        } catch (ParticleDevice.FunctionDoesNotExistException e1) {
                            e1.printStackTrace();
                        }


                        return -1;
                    }

                    @Override
                    public void onSuccess(Object o) {
                        // put your success message here
                        Log.d(TAG, "Success: Turned light green!!");
                    }

                    @Override
                    public void onFailure(ParticleCloudException exception) {
                        // put your error handling code here
                        Log.d(TAG, exception.getBestMessage());
                    }
                });
            }

            public void indicateIncorrectAnswer() {

                Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
                    @Override
                    public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                        // put your logic here to talk to the particle
                        // --------------------------------------------
                        List<String> functionParameters = new ArrayList<String>();
                        functionParameters.add("red");
                        try {
                            mDevice.callFunction("showAnswerResult", functionParameters);

                        } catch (ParticleDevice.FunctionDoesNotExistException e1) {
                            e1.printStackTrace();
                        }


                        return -1;
                    }

                    @Override
                    public void onSuccess(Object o) {
                        // put your success message here
                        Log.d(TAG, "Success: Turned lights red!!");
                    }

                    @Override
                    public void onFailure(ParticleCloudException exception) {
                        // put your error handling code here
                        Log.d(TAG, exception.getBestMessage());
                    }
                });

            }

            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "Success: Subscribed to events!");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                Log.d(TAG, exception.getBestMessage());
            }
        });
    }

    public void startActivity2(View view){
        Intent myIntent = new Intent(getBaseContext(),   Main2Activity.class);
        startActivity(myIntent);
    }

    public void gotoStartingActivity(View view){
        Intent myIntent = new Intent(getBaseContext(),   ActivityChooseShapes.class);
        startActivity(myIntent);
    }

}
