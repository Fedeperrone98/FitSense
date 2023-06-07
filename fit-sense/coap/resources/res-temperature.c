#include "contiki.h"
#include "resource.h"

#include <string.h>
#include <stdio.h>
#include "coap-engine.h"

static void res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);
static void res_put_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);
static void res_event_handler(void);

static struct temperature_str{
    int value;
    int state;
}temperature_mem;

EVENT_RESOURCE(
    res_temperature,
    "title=\"Temperature\";rt=\"Temperature\"",
    res_get_handler,
    NULL,
    res_put_handler,
    NULL,
    res_event_handler
);

void set_temperature(char msg[]){
    int temperature = (5 + random_rand()%35);

    temperature_mem.value = temperature;

    LOG_INFO("[+] temperature detected: %d\n", temperature);

    sprintf(msg,"{\"cmd\":\"%s\",\"value\":%d}",
        "temperature",
        temperature_mem.value
        );

    LOG_INFO(" >  %s\n", msg);
}

static void
res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset)
{
  if(temperature_mem.state == STATE_ERROR)
        return;

    char reply[MSG_SIZE];

    LOG_INFO(" <  GET actuator/temperature\n")
    set_temperature(reply);
    
    coap_set_header_content_format(response, TEXT_PLAIN);
    coap_set_payload(response, buffer, snprintf((char *)buffer, preferred_size, "%s", reply));
}

static void
res_put_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset)
{
  if(humidity_mem.state == STATE_ERROR)
    	return;  
}

static void
res_event_handler(void)
{
	// Notify all the observers
	coap_notify_observers(&res_temperature);
}