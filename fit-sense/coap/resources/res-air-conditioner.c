#include "contiki.h"
#include "resource.h"

#include <string.h>
#include <stdio.h>
#include "coap-engine.h"
#include "sys/log.h"
#include "os/dev/leds.h"

static void res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);
static void res_put_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);

static char mode[] = "off";

RESOURCE(
    res_air_conditioner,
    "title=\"AirConditioner\";rt=\"AirConditioner\"",
    res_get_handler,
    NULL,
    res_put_handler,
    NULL
);

// Function to clean the message array
void cleanArray(char* array, size_t size) {
    for (size_t i = 0; i < size; i++) {
        array[i] = '\0';
    }
}

void send_air_conditioner_status(char msg[]){

    sprintf(msg,"{\"cmd\":\"%s\",\"value\":\"%s\"}",
        "air_conditioner_status",
        mode
        );

    LOG_INFO(" >  %s\n", msg);
}

static void
res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset)
{
    char reply[MSG_SIZE];

    LOG_INFO(" <  GET actuator/air-conditioner\n");
    send_air_conditioner_status(reply);
    
    coap_set_header_content_format(response, TEXT_PLAIN);
    coap_set_payload(response, buffer, snprintf((char *)buffer, preferred_size, "%s", reply));
}

static void
res_put_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset)
{
    LOG_INFO(" <  put air conditioner\n");
    const uint8_t* arg;
    char msg[MSG_SIZE];
    char reply[MSG_SIZE];
    int len = coap_get_payload(request, &arg);
    if (len <= 0){
        LOG_INFO("[-] no argument obteined from put request of config_rsc\n");
        return;
    }
    sprintf(msg, "%s", (char*)arg);
    
    int n_arguments = 1; 
    char arguments[n_arguments][100];
    parse_json(msg, n_arguments, arguments );

    cleanArray(mode, sizeof(mode));
    strcpy(mode, arguments[0]);
    send_air_conditioner_status(reply);

    coap_set_header_content_format(response, TEXT_PLAIN);
    coap_set_payload(response, buffer, snprintf((char *)buffer, preferred_size, "%s", reply));

    if(!strcmp(mode, "on")){
        set_air_conditioner_status(true);
        LOG_INFO("[!] Turn on GREEN LED\n");
        leds_on(LEDS_GREEN);
    } else{
        set_air_conditioner_status(false);
        LOG_INFO("[!] Turn off GREEN LED\n");
        leds_off(LEDS_GREEN);
    }
}
