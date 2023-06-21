#include "contiki.h"
#include "resource.h"

#include <string.h>
#include <stdio.h>
#include "coap-engine.h"
#include "sys/log.h"

static void res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);

static struct configuration_str{
    unsigned int area_id;
    unsigned int node_id;
    int state;
}configuration;

RESOURCE(
    res_configuration,
    "title=\"Configuration\";rt=\"Configuration\"",
    res_get_handler,
    NULL,
    NULL,
    NULL
);

void save_config(int area_id, int node_id){
    configuration.area_id = area_id;
    configuration.node_id = node_id;
    configuration.state = STATE_INITIALIZED;
}

void config_error(){
  configuration.state = STATE_ERROR;
}

void send_config_status(char msg[]){

    sprintf(msg, "{\"cmd\":\"%s\",\"body\":{\"area_id\":%d,\"node_id\":%d}}",
        "config-status",
        configuration.area_id,
        configuration.node_id
        );
    
    LOG_INFO(" >  %s \n", msg);
}

static void
res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset)
{
    if(configuration.state == STATE_ERROR)
        return;

    LOG_INFO(" <  get config\n");
    char msg[MSG_SIZE];
    send_config_status(msg); 
    coap_set_header_content_format(response, TEXT_PLAIN);
    coap_set_payload(response, buffer, snprintf((char *)buffer, preferred_size, "%s", msg));
}

