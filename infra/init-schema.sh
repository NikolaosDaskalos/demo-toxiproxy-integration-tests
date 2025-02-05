#!/bin/sh

#source ./schemas.sh
echo "Waiting for Schema Registry to be ready..."
/etc/confluent/docker/run &

while true; do
    response=$(curl -o /dev/null -s -w "%{http_code}" "http://schema-registry:8081/subjects")
    if [ "$response" -eq 200 ]; then
      echo "Schema registry is ready"
      break
    fi
    echo "Still waiting for schema registry"
    sleep 2
done

echo "Registering schema..."
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
     --data '{"schema": "{  \"type\": \"record\",  \"name\": \"OrderEventMessage\",  \"namespace\": \"org.demo.toxiproxy.avro\",  \"fields\": [    {      \"name\": \"messageId\",      \"type\": {        \"type\": \"string\",        \"logicalType\": \"uuid\"      }    },    {      \"name\": \"orderId\",      \"type\": \"long\"    },    {      \"name\": \"item\",      \"type\": \"string\"    },    {      \"name\": \"quantity\",      \"type\": \"int\"    },    {      \"name\": \"userInfo\",      \"type\": \"string\"    },    {      \"name\": \"status\",      \"type\": [        \"null\",        \"string\"      ],      \"default\": null    }  ]}"}' \
     http://schema-registry:8081/subjects/order-events-value/versions

echo "Schema registration complete!"

# Keep the container running (in the background)
tail -f /dev/null
