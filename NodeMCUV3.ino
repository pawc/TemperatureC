#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>
#include <OneWire.h> 
#include <DallasTemperature.h>

#define ONE_WIRE_BUS D1

OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature sensors(&oneWire);
 
void setup() {
 
  Serial.begin(115200);    
  WiFi.begin("", "");  
 
  while (WiFi.status() != WL_CONNECTED) {
 
    delay(500);
    Serial.println("Waiting for connection");
 
  }

  sensors.begin(); 
 
}
 
void loop() {
 
 if(WiFi.status()== WL_CONNECTED){
 
   HTTPClient http;

   sensors.requestTemperatures();

   String temperature = String(sensors.getTempCByIndex(0));
 
   http.begin("http://cloud.paddatrapper.me:8080/Temperature/post?owner=osk1&temperature="+temperature);
   http.addHeader("Content-Type", "text/plain");
 
   int httpCode = http.POST("Message from ESP8266");
   String payload = http.getString();
 
   Serial.println(httpCode);
   Serial.println(payload);
 
   http.end();
 
 }else{
 
    Serial.println("Error in WiFi connection");   
 
 }
 
  delay(60000); 
 
}