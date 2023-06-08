#include "contiki.h"
#include "resource.h"

#include <string.h>
#include <stdio.h>
#include "coap-engine.h"

static void res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);
static void res_put_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);

static char[4] mode = "off";

RESOURCE(
    res_semaphore,
    "title=\"Semaphore\";rt=\"Semaphore\"",
    res_get_handler,
    NULL,
    res_put_handler,
    NULL
);

void send_semaphore_status(char msg[]){

    sprintf(msg,"{\"cmd\":\"%s\",\"value\":%s}",
        "semaphorer_status",
        mode
        );

    LOG_INFO(" >  %s\n", msg);
}

static void
res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset)
{
    char reply[MSG_SIZE];

    LOG_INFO(" <  GET actuator/semaphore\n")
    send_semaphore_status(reply);
    
    coap_set_header_content_format(response, TEXT_PLAIN);
    coap_set_payload(response, buffer, snprintf((char *)buffer, preferred_size, "%s", reply));
}

static void
res_put_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset)
{
    LOG_INFO(" <  put semaphore\n");
    const uint8_t* arg;
    char msg[MSG_SIZE];
    char reply[MSG_SIZE];
    int len = coap_get_payload(request, &arg);
    if (len <= 0){
        LOG_INFO("[-] no argument obteined from put request of config_rsc");
        return;
    }
    sprintf(msg, "%s", (char*)arg);
    
    int n_arguments = 1; 
    char arguments[n_arguments][100];
    parse_json(msg, n_arguments, arguments );

    strcpy(mode, arguments[0]);
    send_semaphore_status(reply);

    coap_set_header_content_format(response, TEXT_PLAIN);
    coap_set_payload(response, buffer, snprintf((char *)buffer, preferred_size, "%s", reply));

    if(strcmp(mode, "on")){
        leds_single_on(LEDS_RED);
    } else{
        leds_single_off(LEDS_RED);
    }

}