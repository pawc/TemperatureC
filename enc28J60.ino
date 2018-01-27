#include <EtherCard.h>
#include <OneWire.h> 
#include <DallasTemperature.h>

#define ONE_WIRE_BUS 2
OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature sensors(&oneWire);

static byte mymac[] = { 0x74,0x69,0x69,0x2D,0x30,0x31 };

byte Ethernet::buffer[700];
static uint32_t timer;

const char website[] PROGMEM = "51.15.78.61";

static void my_callback (byte status, word off, word len) {
  Serial.println(">>>");
  Ethernet::buffer[off+300] = 0;
  Serial.print((const char*) Ethernet::buffer + off);
  Serial.println("...");
}

void setup () {
  Serial.begin(57600);
  Serial.println("\n[webClient]");

  if (ether.begin(sizeof Ethernet::buffer, mymac) == 0) 
    Serial.println( "Failed to access Ethernet controller");
  if (!ether.dhcpSetup())
    Serial.println("DHCP failed");

  ether.printIp("IP:  ", ether.myip);
  ether.printIp("GW:  ", ether.gwip);  
  ether.printIp("DNS: ", ether.dnsip);

  ether.parseIp(ether.hisip, "51.15.78.61"); 
  ether.printIp("SRV: ", ether.hisip); 

  ether.hisport = 8080;

  sensors.begin();
}

void loop () {
  ether.packetLoop(ether.packetReceive());
  
  if (millis() > timer) {
  timer = millis() + 60000;
  Serial.println();

  sensors.requestTemperatures();

  double temperature = sensors.getTempCByIndex(0);

  char result[20] = "";
  dtostrf(temperature, 3, 4, result);

  Serial.println("Odczytana temperatura: "+String(temperature));
 
  ether.browseUrl(PSTR("/Temperature/oskPost?value="), result, website, my_callback);
  }
}
