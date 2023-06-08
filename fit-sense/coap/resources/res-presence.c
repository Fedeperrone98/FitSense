#include "contiki.h"
#include "resource.h"

#include <string.h>
#include <stdio.h>
#include "coap-engine.h"

#define MAX_CAPACITY 50

static void res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);
static void res_put_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);
static void res_event_handler(void);

static struct presence_str{
    int value = 0;
}presence_mem;

EVENT_RESOURCE(
    res_presence,
    "title=\"Presence\";rt=\"Presence\"",
    res_get_handler,
    NULL,
    res_put_handler,
    NULL,
    res_event_handler
);

void set_presence(char msg[]){
    int presence = random_rand() % MAX_CAPACITY;

    presence_mem.value = presence;

    LOG_INFO("[+] presence detected: %d\n", presence);

    sprintf(msg,"{\"cmd\":\"%s\",\"value\":%d}",
        "presence",
        presence_mem.value
        );

    LOG_INFO(" >  %s\n", msg);
}

static void
res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset)
{

    char reply[MSG_SIZE];

    LOG_INFO(" <  GET actuator/presence\n")
    set_presence(reply);
    
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
	coap_notify_observers(&res_presence);
}