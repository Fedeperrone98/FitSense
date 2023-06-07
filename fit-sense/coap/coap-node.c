#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "contiki.h"
#include "coap-engine.h"

/* Log configuration */
#include "sys/log.h"
#define LOG_MODULE "App"
#define LOG_LEVEL LOG_LEVEL_APP
/*
 * Resources to be activated need to be imported through the extern keyword.
 * The build system automatically compiles the resources in the corresponding sub-directory.
 */
extern coap_resource_t
    res_humidity,
    res_presence,
    res_temperature;


PROCESS(er_example_server, "Erbium Example Server");
AUTOSTART_PROCESSES(&er_example_server);

PROCESS_THREAD(er_example_server, ev, data)
{
    PROCESS_BEGIN();

    PROCESS_PAUSE();

    LOG_INFO("Starting Erbium Example Server\n");

    /*
    * Bind the resources to their Uri-Path.
    * WARNING: Activating twice only means alternate path, not two instances!
    * All static variables are the same for each URI path.
    */
    coap_activate_resource(&res_humidity, "actuator/humidity");
    coap_activate_resource(&res_presence, "actuator/presence");
    coap_activate_resource(&res_temperature, "actuator/temperature");

    /* Define application-specific events here. */
    while(1) {
        PROCESS_WAIT_EVENT();

    }

    PROCESS_END();
}
