#include "contiki.h"
#include "resource.h"

#include <string.h>
#include <stdio.h>
#include "coap-engine.h"

static void res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);
static void res_put_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);
static void res_event_handler(void);

static struct humidity_str{
    int value = 0;
    bool dehumidifier_on = false;
}humidity_mem;

EVENT_RESOURCE(
    res_humidity,
    "title=\"Humidity\";rt=\"Humidity\"",
    res_get_handler,
    NULL,
    res_put_handler,
    NULL,
    res_event_handler
);

void set_dehumidifier_status(bool on){
    humidity_mem.dehumidifier_on = on;
}

void set_humidity(char msg[]){
    int random;
    int humidity;
    if(humidity_mem.dehumidifier_on == 0){
        // Generazione di un numero casuale compreso tra 0 e 100
        random = random_rand() % 101;
        // Mapping del numero casuale nell'intervallo 20-80
        humidity = (random * 60 / 100) + 20;
    }else{
        // Generazione di un numero casuale compreso tra 0 e 100
        random = random_rand() % 101;
        // Mapping del numero casuale nell'intervallo 20-25
        humidity = (random * 5 / 100) + 20;
    }

    humidity_mem.value = humidity;

    LOG_INFO("[+] humidity detected: %d\n", humidity);

    sprintf(msg,"{\"cmd\":\"%s\",\"value\":%d}",
        "humidity",
        humidity_mem.value
        );

    LOG_INFO(" >  %s\n", msg);
}

static void
res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset)
{
    char reply[MSG_SIZE];

    LOG_INFO(" <  GET actuator/humidity\n")
    set_humidity(reply);
    
    coap_set_header_content_format(response, TEXT_PLAIN);
    coap_set_payload(response, buffer, snprintf((char *)buffer, preferred_size, "%s", reply));
}

static void
res_put_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset)
{
    
}

static void
res_event_handler(void)
{
    // Notify all the observers
    coap_notify_observers(&res_humidity);
}