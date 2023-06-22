#ifndef RESOURCE_H_
#define RESOURCE_H_

#include <stddef.h>

#define LOG_MODULE "App"
#define LOG_LEVEL LOG_LEVEL_APP

#define STATE_INITIALIZED    0
#define STATE_ERROR         1

#define MSG_SIZE    200

#define NOTIFICATION_TIME_TEMPERATURE 600
#define NOTIFICATION_TIME_HUMIDITY 601
#define NOTIFICATION_TIME_PRESENCE 602

void parse_json(char json[], int n_arguments, char arguments[][100]);
void set_air_conditioner_status(bool on);
void set_dehumidifier_status(bool on);
void set_air_conditioner_status(bool on);
void cleanArray(char* array, size_t size);

#endif
