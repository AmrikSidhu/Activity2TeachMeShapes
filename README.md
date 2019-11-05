## TeachMeShapes Platform: Android : Assignment 2
## Platform : Android.

Student Name  | Student ID
------------- | -------------
Amrik Singh Sidhu  | C0742318


## Particle code

#include <InternetButton.h>

#include "math.h"

InternetButton button = InternetButton();
// creating varables
int score = 0;
int activityNumber = 0;

void setup() {
  button.begin();

//ACTIVITY 1 exposed function/variables
  Particle.function("score", showScore);
  Particle.function("setActivityNumber", setActivityNumber);
  Particle.function("showAnswerResult", showAnswerResult);
  Particle.variable("scoreCount", score);
  
  Particle.variable("activityNumber", activityNumber);

// welcome tone..
   button.playSong("D6,8,G6,8");
    button.playSong("D6,8,B9,8,5,G11,8,6,D6,8");
    button.playSong("E12,10,9,7");
    button.playSong("D6,8,G6,8");
    button.playSong("E6,8,C6,8");
    button.playSong("E5,8,G5,8");
    button.playSong("D6,8,G11,8,6,D6,8");
    button.playSong("D6,8,G6,8");
    button.allLedsOn(200,10,10);
    delay(2000);
    button.allLedsOff();
    delay(200);
  
}



int DELAY = 100;

void loop() {

//Start mobile screen Activity (not implemented yet!)
  if (activityNumber == 0){
        if (button.buttonOn(2)){
       Particle.publish("selectedOption","activity1", 60, PUBLIC);
       delay(DELAY);
   }
        if (button.buttonOn(3)){
       Particle.publish("selectedOption","activity2", 60, PUBLIC);
       delay(DELAY);
   }
        if (button.buttonOn(4)){
       Particle.publish("selectedOption","randomActivity", 60, PUBLIC);
       delay(DELAY);
   }
  }
  
  

// implemented : Activity 1 Teaching Shapes!
  if (activityNumber == 1){
    
    
  if(button.buttonOn(2))
  {
    
        Particle.publish("selectedOption","2", 60, PUBLIC);
  }
   
    else if (button.buttonOn(1)){
        delay(500);
       
        Particle.publish("selectedOption","1", 60, PUBLIC);
    
    }
  
    

   if (button.buttonOn(4)) {
       delay(500);
       if (button.buttonOn(4)){
        Particle.publish("selectedOption","next", 60, PUBLIC);
        delay(DELAY);
       }
  }
  if (button.buttonOn(2)){
      Particle.publish("selectedOption","showScore", 60, PUBLIC);
      showScore("");
      delay(DELAY);
    }
    
  if (button.buttonOn(3)) {
      delay(500);
      if (button.buttonOn(3)){
        Particle.publish("selectedOption","prev", 60, PUBLIC);
        delay(DELAY);
      }
    
  }
  }
  
 
 
}

// exposing functions

int showAnswerResult(String cmd) {
  if (cmd == "green") {
    button.allLedsOn(0,20,0);
    score = score + 1;
    delay(2000);
    button.allLedsOff();
  }
  else if (cmd == "red") {
    button.allLedsOn(20,0,0);
    delay(2000);
    button.allLedsOff();
  }
  else {
    return -1;
  }
  return 1;
}

int showScore(String cmd) {

  // scores on particle
  button.allLedsOff();

  if (score < 0 || score > 11) {

    return -1;
  }
// Leds loop
  for (int i = 1; i <= score; i++) {
      button.ledOn(i, 100, 50, 0);
      delay(DELAY);
  }
  delay(500);
  button.allLedsOff();

  return 1;
}

// Activity Number
int setActivityNumber(String cmd){
    activityNumber = cmd.toInt();
    return 1;
}
