#!/bin/bash

# Имя топика
TOPIC_NAME="my_topic"

# Количество разделов (partitions)
PARTITIONS=3

# Коэффициент репликации (replicas)
REPLICATION_FACTOR=2

# Проверяем, существует ли топик
docker exec kafka-low-level-kafka1-1 kafka-topics --bootstrap-server localhost:9092 --topic $TOPIC_NAME --describe > /dev/null 2>&1
if [ $? -eq 0 ]; then
  echo "Топик '$TOPIC_NAME' уже существует."
else
  # Создаем топик
  docker exec kafka-low-level-kafka1-1 kafka-topics --create --bootstrap-server localhost:9092 \
                                --replication-factor $REPLICATION_FACTOR \
                                --partitions $PARTITIONS \
                                --topic $TOPIC_NAME
fi