#ifndef RESOURCE_H_
#define RESOURCE_H_

#define LOG_LEVEL LOG_LEVEL_APP

#define STATE_INITIALIZED    0
#define STATE_ERROR         1

#define MSG_SIZE    200

#define NOTIFICATION_TIME_TEMPERATURE 600
#define NOTIFICATION_TIME_HUMIDITY 601
#define NOTIFICATION_TIME_PRESENCE 602

static struct etimer led_etimer;

void parse_json(char json[], int n_arguments, char arguments[][100]);

#endif