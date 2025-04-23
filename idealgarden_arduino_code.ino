// Load libraries
#include <WiFi.h>

#include <WiFiClient.h>
#include <WebServer.h>
#include <ESPmDNS.h>

#include <Ultrasonic.h>
#include <DHT.h>

#include <ArduinoJson.h>
#include <ESP32Servo.h>

//***************************** FIREBASE *****************
#include <Firebase_ESP_Client.h>



//Provide the token generation process info.
#include "addons/TokenHelper.h"
//Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"




// Insert your network credentials
#define WIFI_SSID "CF1013"
#define WIFI_PASSWORD "c20211013"

// Insert Firebase project API Key
#define API_KEY "AIzaSyByqJZuUHjxs8gqYUKWjHix-5vE0LTD5oQ"

// Insert RTDB URLefine the RTDB URL */
#define DATABASE_URL "https://ideal-garden-5b5d0-default-rtdb.europe-west1.firebasedatabase.app/" 

//Define Firebase Data object
FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;

unsigned long sendDataPrevMillis = 0;
int intValue;
float floatValue;
bool signupOK = false;



//*****************************************************************

bool isDoorOpen=false;
bool isManuallyControlled = false;

int doorCloseDegree = 60;
int doorOpenDegree = 0;


Servo myservo;  // create servo object to control a servo


// Set web server port number to 80
WebServer server(80);


#define tempratureSensor 21

Ultrasonic ultrasonicOne(12, 14); //(Trig -> 12,Echo -> 14)    //For Trash

DHT dht_sensor(tempratureSensor, DHT11);



//LDR Sensor
#define ldrSensor A0 //SP


//Soil Sensors
#define soilSensorOne 33

//Movment Sensors
#define movmentSensorOne 35
#define movmentSensorTwo 34

//LEDs
#define ledOne 17 //For the auto lighting
#define ledTwo 23 //For the chairs
#define ledThree 18 //For the Soil
#define ledFour 4 //For the Trash


//Buzzer 
#define buzzer 16

//Temprature 
#define servo 19


void handleRoot() {
      Serial.println("MAIN PAGE");
    }

void handleNotFound() {
      Serial.println("ROOT NOT FOUND");

    }


void handleAutoControlledOn(){
  Serial.println("AUTO CONTROLLED ON");
  isManuallyControlled = false;
      }

void handleAutoControlledOff(){
    Serial.println("AUTO CONTROLLED OFF");
  isManuallyControlled = true;
    }

void handleWaterOn(){
  Serial.println("WATER ON");
  pinON(ledThree);
  pinON(buzzer);
    }

void handleWaterOff(){
  Serial.println("WATER OFF");
  pinOFF(ledThree);
  pinOFF(buzzer);
    }

void handleLamp1_On(){
 Serial.println("LAMPS1 ON");
 pinON(ledOne);
    }

void handleLamp1_Off(){
 Serial.println("LAMPS1 OFF");
 pinOFF(ledOne);
    }

void handleLamp2_On(){
   Serial.println("LAMPS2 ON");
 pinON(ledTwo);
    }

void handleLamp2_Off(){
   Serial.println("LAMPS2 OFF");
 pinOFF(ledTwo);
    }

void handleAllLampsOn(){
    Serial.println("ALL LAMPS ON");
   pinON(ledOne);
   pinON(ledTwo);
    }

void handleAllLampsOff(){
  Serial.println("ALL LAMPS OFF");
   pinOFF(ledOne);
   pinOFF(ledTwo);
    }

void handleDoorOn(){
  Serial.println("DOOR HAS BEEN OPENED");
  isDoorOpen = true;
  myservo.write(doorOpenDegree);
    }

void handleDoorOff(){
  Serial.println("DOOR HAS BEEN CLOSED");
isDoorOpen=false;
myservo.write(doorCloseDegree);
    }

  String IpAddress2String(const IPAddress& ipAddress)
{
    return String(ipAddress[0]) + String(".") +
           String(ipAddress[1]) + String(".") +
           String(ipAddress[2]) + String(".") +
           String(ipAddress[3]);
}
  
  void handleAnalyticsMobile(){
  String response; 
  
        //JSON OBJECT (lamit les données ta3na kaml w 7tinahm f object JSON w b3tnahm l tele)
      DynamicJsonDocument doc(1024);
      doc["isManually"] = isManuallyControlled;
      doc["isNight"] = isNight();
      doc["isWaterON"] = intToBool(readPinState(ledThree));
      doc["isDrySoil"] = isDrySoil();
      doc["isChairsNotEmpty"] = isChairsNotEmpty();
      doc["isTrashFull"] = isTrashFull();
      doc["isDoorOpen"]   = isDoorOpen;
      doc["isLedOneLighting"] = intToBool(readPinState(ledOne));
      doc["isLedTwoLighting"]   = intToBool(readPinState(ledTwo));
      doc["Temperature"] = (int) goReadTemperature();
      doc["Humidity"]   = goReadHumidity();
      doc["IP" ] = IpAddress2String(WiFi.localIP());
      serializeJson(doc, response);
      server.send(200, "application/json", response);
  }

    void saveToFirebase(){
      FirebaseJson json;
      
      json.add("IP", IpAddress2String(WiFi.localIP()));
      json.add("isManually", isManuallyControlled);
      json.add("isNight", isNight());
      json.add("isWaterON", intToBool(readPinState(ledThree)));
      json.add("isDrySoil", isDrySoil());
      json.add("isChairsNotEmpty", isChairsNotEmpty());
      json.add("isTrashFull", isTrashFull());
      json.add("isDoorOpen", isDoorOpen);
      json.add("isLedOneLighting", intToBool(readPinState(ledOne)));
      json.add("isLedTwoLighting", intToBool(readPinState(ledTwo)));
      json.add("Temperature", (int) goReadTemperature());
      json.add("Humidity", goReadHumidity());

      Firebase.RTDB.setJSONAsync(&fbdo, "data/", &json);

      

  }

void setup() {
  Serial.begin(9600);
  pinMode(ldrSensor, INPUT);
  pinMode(soilSensorOne, INPUT);
  pinMode(movmentSensorOne, INPUT);
  pinMode(movmentSensorTwo, INPUT);
  
  pinMode(ledOne, OUTPUT);
  pinMode(ledTwo, OUTPUT);
  pinMode(ledThree, OUTPUT);
  pinMode(ledFour, OUTPUT);
  pinMode(buzzer, OUTPUT);
  pinMode(servo, OUTPUT);
  dht_sensor.begin();
  
  // Allow allocation of all timers
  ESP32PWM::allocateTimer(0);
  ESP32PWM::allocateTimer(1);
  ESP32PWM::allocateTimer(2);
  ESP32PWM::allocateTimer(3);
  myservo.setPeriodHertz(50);    // standard 50 hz servo
  myservo.attach(servo, 1000, 2000); // attaches the servo on pin 18 to the servo object
  // using default min/max of 1000us and 2000us
  // different servos may require different min/max settings
  // for an accurate 0 to 180 sweep

  myservo.write(doorCloseDegree);


  //****************** FIREBASE **********************



  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  /* Assign the api key (required) */
  config.api_key = API_KEY;

  /* Assign the RTDB URL (required) */
  config.database_url = DATABASE_URL;

  /* Sign up */
  if (Firebase.signUp(&config, &auth, "", "")) {
    Serial.println("ok");
    signupOK = true;
  }
  else {
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }

  /* Assign the callback function for the long running token generation task */
  config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h

  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
  


  //******************* END FIREBASE **************************

  server.on("/", handleRoot);
  server.on("/auto/on", handleAutoControlledOn);
  server.on("/auto/off", handleAutoControlledOff);
  server.on("/water/on", handleWaterOn);
  server.on("/water/off", handleWaterOff);
  server.on("/lamps1/on", handleLamp1_On);
  server.on("/lamps1/off", handleLamp1_Off);
  server.on("/lamps2/on", handleLamp2_On);
  server.on("/lamps2/off", handleLamp2_Off);
  server.on("/all_lamps/on", handleAllLampsOn);
  server.on("/all_lamps/off", handleAllLampsOff);
  server.on("/door/on", handleDoorOn);
  server.on("/door/off", handleDoorOff);
  server.on("/analyticsMobile", handleAnalyticsMobile);
  server.onNotFound(handleNotFound);

  server.begin();
  Serial.println("server started");
  //SEND IP TO FIREBASE
  Firebase.RTDB.setStringAsync(&fbdo, "data/IP", IpAddress2String(WiFi.localIP()));
}






float goReadTemperature(){
    // read humidity
  float tempC  = dht_sensor.readTemperature();


  // check whether the reading is successful or not
  if (isnan(tempC)) {
    return 0;
    //Serial.println("Failed to read from DHT sensor!");
  } else {
    return tempC;
  }
}

float goReadHumidity(){
    // read humidity
  float humi  = dht_sensor.readHumidity();


  // check whether the reading is successful or not
  if (isnan(humi)) {
    return 0;
    //Serial.println("Failed to read from DHT sensor!");
  } else {
    return humi;
  }
}


//CONVERTE FERQUNCY TO PERCENTRAGE 0..100%
int frequancyToPercentage(int value){
  return map(value, 0, 4095, 0, 100);
}


// UTILS
bool intToBool(int state){
  if(state==1){
    return true;
  }else{
    return false;
  }
}

int goReadSoilSensor(int sensorPin){
   int readsoilSensorvalue = analogRead(sensorPin);
  return frequancyToPercentage(readsoilSensorvalue);
}

int goReadMovmentSensor(int sensorPin){
   int readMovmentSensorvalue = analogRead(sensorPin);
  return frequancyToPercentage(readMovmentSensorvalue);
}

int goReadUltrasonicSensor(Ultrasonic sensor){
   int UltrasonicValueCM = sensor.read();
  return UltrasonicValueCM;
}

int goReadLDRSensor(){
   int LDRValue = analogRead(ldrSensor);
  return frequancyToPercentage(LDRValue);
}

bool isNight(){
  if(goReadLDRSensor()<15){
    return true;
  }else{
    return false;
  }
}


bool isDrySoil(){
  if(goReadSoilSensor(soilSensorOne)>50){
    return true;
  }else{
    return false;
  }
}


bool isChairsNotEmpty(){
  if(goReadMovmentSensor(movmentSensorOne)<50 || goReadMovmentSensor(movmentSensorTwo)<50){
    return true;
  }else{
    return false;
  }
}

bool isTrashFull(){
  if(goReadUltrasonicSensor(ultrasonicOne)<=7){
    return true;
  }else{
    return false;
  }
}



void pinON(int mPin){
  digitalWrite(mPin,HIGH);
}

void pinOFF(int mPin){
  digitalWrite(mPin,LOW);
}

int readPinState(int mPin){
  return digitalRead(mPin);
}

//*** AUTO CONTROL ************
void autoControl(){
   //IF NIGHT TURN ON THE LIGHT
  if(isNight()){
    Serial.println("LDR : NIGHT ");
    pinON(ledOne);
  }else{
    Serial.println("LDR : MORNING ");
    pinOFF(ledOne);
  }

    if(isDrySoil()){
    Serial.println("SOIL : DRY ");
    pinON(ledThree);
    pinON(buzzer);
  }else{
    Serial.println("SOIL : WET ");
    pinOFF(ledThree);
    pinOFF(buzzer);
  }

   if(isChairsNotEmpty() && isNight()){
    Serial.println("PPL ON THE CHAIR : YES ");
    pinON(ledTwo);
  }else{
    Serial.println("PPL ON THE CHAIR : NO ");
    pinOFF(ledTwo);
  }

    if(isTrashFull()){
    Serial.println("TRASH : FULL");
    pinON(ledFour);
  }else{
    Serial.println("TRASH : Empety");
    pinOFF(ledFour);
  }
  

  
  Serial.print("LDR_VALUE : ");
  Serial.println(goReadLDRSensor());

  Serial.print("Temperature : ");
  Serial.print(goReadTemperature());
  Serial.println("°C");

  
  Serial.print("Humidity : ");
  Serial.print(goReadHumidity());
  Serial.println("%");
  


  Serial.print("Soil Sensor 1 : ");
  Serial.println(goReadSoilSensor(soilSensorOne));
 
  

 
  

  Serial.print("Movment Sensor 1 : ");
  Serial.println(goReadMovmentSensor(movmentSensorOne));

  Serial.print("Movment Sensor 2 : ");
  Serial.println(goReadMovmentSensor(movmentSensorTwo));

  
  Serial.print("Sensor 1 Distance in CM: ");
  Serial.println(goReadUltrasonicSensor(ultrasonicOne));




  Serial.println("Pins State One : ");
  Serial.println(readPinState(buzzer));
  Serial.println(readPinState(ledOne));

}

void loop() {
    server.handleClient();
    delay(2);//allow the cpu to switch to other tasks

// SAVE NEW DATA TO FIREBASE EVERY 2s
    if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 2000 || sendDataPrevMillis == 0)) {
    sendDataPrevMillis = millis();
    saveToFirebase();
 }
  

 if(!isManuallyControlled){
   autoControl();
 }

  delay(500);
}
