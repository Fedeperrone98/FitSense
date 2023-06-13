#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "contiki.h"
#include "coap-engine.h"
#include "os/dev/leds.h"
#include "sys/etimer.h"
#include "os/dev/button-hal.h"
#include "coap-blocking-api.h"

/* Log configuration */
#include "sys/log.h"
#define LOG_MODULE "App"
#define LOG_LEVEL LOG_LEVEL_APP

#define SERVER_EP           "coap://[fd00::1]:5683"
#define STATE_INITIALIZED   0
#define STATE_REGISTERED    1
#define STATE_ERROR         2
#define CLOCK_MINUTE        CLOCK_SECOND * 60
#define MSG_SIZE            200

void save_config(int area_id, int node_id);
void initialize_humidity_str();
void initialize_temperature_str();
void initialize_presence_str();
void set_humidity_etimer();
void set_temperature_etimer();
void set_presence_etimer();
bool check_temperature_timer_expired();
bool check_humidity_timer_expired();
bool check_presence_timer_expired();
void restart_presence_timer();
void restart_temperature_timer();
void restart_humidity_timer();

static struct etimer btn_etimer;
static struct etimer led_etimer;

// struct to send coap registration request
static struct coap_module_str{
    coap_endpoint_t server_ep;
    coap_message_t request[1]; 
}coap_module;

static unsigned int STATE;

// resources to expose
extern coap_resource_t
    res_configuration,
    res_humidity,
    res_presence,
    res_temperature,
    res_dehumidifier,
    res_air_conditioner,
    res_semaphore;


PROCESS(coap_node, "Coap node");
AUTOSTART_PROCESSES(&coap_node);

void parse_json(char json[], int n_arguments, char arguments[][100]){

    int value_parsed = 0;
    int len = 0;
    bool value_detected = false;
    int i;
    for(i = 0; json[i] != '\0' && value_parsed < n_arguments; i++){
        
        if(json[i] == ':'){
            i++; //there is the space after ':'
            value_detected = true;
            len = 0;
        }
        else if(value_detected && (json[i] == ',' || json[i] == ' ' || json[i] == '}')){
            value_detected = false;
            arguments[value_parsed][len] = '\0';
            value_parsed++;
        }
        else if(value_detected && json[i] == '{'){
            value_detected = false;
        }
        else if(value_detected){
            if(json[i] =='\'' || json[i] == '\"')
                continue;
            arguments[value_parsed][len] = json[i];
            len++;
        }

    }
}

// Handle the response from the server
void client_chunk_handler(coap_message_t *response){
    const uint8_t *chunk;

    if(response == NULL) {
        puts("Request timed out");
        return;
    }

    coap_get_payload(response, &chunk);
    char msg[MSG_SIZE];
    sprintf(msg,"%s",(char*)chunk);

    int n_arguments = 1; 
    char arguments[n_arguments][100];
    parse_json(msg, n_arguments, arguments );
    
    LOG_INFO("[!] ASSIGN_CONFIG command elaboration ...\n");

    LOG_INFO(" <  %s \n", msg);


    if(strcmp(arguments[0], "server_ok") == 0){
        STATE = STATE_REGISTERED;
        LOG_INFO("[+] ASSIGN_CONFIG command elaborated with success\n");
        return;
    }
    else if(strcmp(arguments[0], "error_area") == 0){
        LOG_INFO("[-] area selected doesn't exist\n");
        STATE = STATE_ERROR;
        return;
    }
    else if(strcmp(arguments[0], "error_id") == 0){
        LOG_INFO("[-] node with the same id already exists\n");
        STATE = STATE_ERROR;
        return;
    } else {
        LOG_INFO("[-] server returns an unknown message\n");
        STATE = STATE_ERROR;
    }

}

PROCESS_THREAD(coap_node, ev, data)
{
    static unsigned int btn_count;
    static bool area_id_setted;
    static int area_id;
    static int node_id;

    PROCESS_BEGIN();

    LOG_INFO("[!] initialization COAP node...\n");

    // set node_id and area_id

    LOG_INFO("[!] manual area_id setting\n");

    // Red LED flashes while waiting for user interaction
    etimer_set(&led_etimer,0.5 * CLOCK_SECOND);
    leds_single_on(LEDS_RED);

    STATE=STATE_ERROR;

    // Ask more times the Ids until they are both ok
    while(STATE==STATE_ERROR)
    {
        btn_count = 0;    
        area_id_setted = false;
        area_id = 0;
        node_id = 0;

        STATE=STATE_ERROR;
        
        while(1) {
            PROCESS_YIELD();

            // check for the event generated by the etimer expiration 
            if( ev == PROCESS_EVENT_TIMER){
                if(etimer_expired(&led_etimer)){
                    leds_single_toggle(LEDS_RED);
                    etimer_restart(&led_etimer);
                }

                if(btn_count > 0 && etimer_expired(&btn_etimer)){
                    if(!area_id_setted){
                        area_id = btn_count;
                        area_id_setted = true;

                        // Green LED flashes for correct area_id setting
                        leds_single_off(LEDS_RED);
                        leds_single_on(LEDS_GREEN);
                        etimer_reset_with_new_interval(&led_etimer, 2 * CLOCK_SECOND);
                        PROCESS_WAIT_EVENT_UNTIL(etimer_expired(&led_etimer));
                        leds_single_off(LEDS_GREEN);

                        // Red LED flashes while waiting for user interaction for node_id setting
                        etimer_set(&led_etimer,0.5 * CLOCK_SECOND);
                        leds_single_on(LEDS_RED);

                        LOG_INFO("[!] manual node_id setting\n");
                        btn_count = 0;
                    }else{
                        node_id = btn_count;

                        // Green LED flashes for correct node_id setting
                        leds_single_off(LEDS_RED);
                        leds_single_on(LEDS_GREEN);
                        etimer_reset_with_new_interval(&led_etimer, 2 * CLOCK_SECOND);
                        PROCESS_WAIT_EVENT_UNTIL(etimer_expired(&led_etimer));
                        leds_single_off(LEDS_GREEN);

                        break;
                    }
                }
            }

            // check for the event generated by the button
            if(ev == button_hal_press_event){
                LOG_INFO("[!] button pressed\n");

                // Stop Red LED flashes
                leds_single_off(LEDS_RED);
                etimer_stop(&led_etimer);

                btn_count++;
                if(btn_count == 1 && !area_id_setted)
                    etimer_set(&btn_etimer, 3 * CLOCK_SECOND);
                else    
                    etimer_restart(&btn_etimer);
            }

        }   

        LOG_INFO("[+] area %d selected \n", area_id);
        LOG_INFO("[+] id %d selected \n", node_id);

        save_config(area_id, node_id); 

        LOG_INFO("[!] intialization ended\n");
        STATE = STATE_INITIALIZED;

        LOG_INFO("[!] registration ... \n");

        // prepare the message to send at the application
        char msg[MSG_SIZE];
        sprintf(msg, "{\"area_id\":%d,\"node_id\":%d}", area_id, node_id);

        // Populate the coap_endpoint_t data structure
        coap_endpoint_parse(SERVER_EP, strlen(SERVER_EP), &coap_module.server_ep);
        // Prepare the message
        coap_init_message(coap_module.request, COAP_TYPE_CON, COAP_POST, 0);
        coap_set_header_uri_path(coap_module.request, "/registration");
        // Set the payload
        coap_set_payload(coap_module.request, (uint8_t *)msg, strlen(msg));

        while(1){
            //Wait until the server replay
            COAP_BLOCKING_REQUEST(&coap_module.server_ep, coap_module.request, client_chunk_handler);

            if(STATE==STATE_REGISTERED)
                break;
            else if(STATE==STATE_ERROR){
                LOG_INFO("[-] configuration failed\n");

                leds_single_toggle(LEDS_RED);
                etimer_restart(&led_etimer);
                break;
            }
            
        }
    }

    LOG_INFO("[!] node online\n");

    coap_activate_resource(&res_configuration, "actuator/configuration");
    coap_activate_resource(&res_humidity, "actuator/humidity");
    coap_activate_resource(&res_presence, "actuator/presence");
    coap_activate_resource(&res_temperature, "actuator/temperature");
    coap_activate_resource(&res_dehumidifier, "actuator/dehumidifier");
    coap_activate_resource(&res_air_conditioner, "actuator/air_conditioner");
    coap_activate_resource(&res_semaphore, "actuator/semaphore");

    initialize_humidity_str();
    initialize_temperature_str();
    initialize_presence_str();

    // Set etimer for observing behavior
    set_humidity_etimer();
    set_temperature_etimer();
    set_presence_etimer();

    while(true){

        PROCESS_YIELD();

        if(check_humidity_timer_expired()){
            res_humidity.trigger();
            restart_humidity_timer();
        }

        if(check_temperature_timer_expired()){
            res_temperature.trigger();
            restart_temperature_timer();
        }

        if(check_presence_timer_expired()){
            res_presence.trigger();
            restart_presence_timer();
        }

    }

    PROCESS_END();
}
