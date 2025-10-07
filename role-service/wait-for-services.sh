#!/bin/bash
# -------------------------------------------------------------
# wait-for-services.sh
# Generic wait script for microservices: Postgres, Eureka, etc.
#
# Usage:
#   ./wait-for-services.sh --postgres host:port --eureka host:port -- <command>
# Example:
#   ./wait-for-services.sh --postgres postgres:5432 --eureka eureka-server:8761 -- java -jar app.jar
# -------------------------------------------------------------

set -e

# ------------------------
# Parse arguments
# ------------------------
while [ $# -gt 0 ]; do
  case "$1" in
    --postgres)
      POSTGRES_HOSTPORT="$2"
      shift 2
      ;;
    --eureka)
      EUREKA_HOSTPORT="$2"
      shift 2
      ;;
    --)
      shift
      CMD="$@"
      break
      ;;
    *)
      echo "Unknown option: $1"
      exit 1
      ;;
  esac
done

# ------------------------
# Split host:port into separate variables
# ------------------------
if [ -n "$POSTGRES_HOSTPORT" ]; then
  POSTGRES_HOST=$(echo "$POSTGRES_HOSTPORT" | cut -d: -f1)
  POSTGRES_PORT=$(echo "$POSTGRES_HOSTPORT" | cut -d: -f2)
fi

if [ -n "$EUREKA_HOSTPORT" ]; then
  EUREKA_HOST=$(echo "$EUREKA_HOSTPORT" | cut -d: -f1)
  EUREKA_PORT=$(echo "$EUREKA_HOSTPORT" | cut -d: -f2)
fi

# ------------------------
# Wait for Postgres
# ------------------------
if [ -n "$POSTGRES_HOSTPORT" ]; then
  echo "⏳ Waiting for Postgres at $POSTGRES_HOST:$POSTGRES_PORT..."
  until nc -z "$POSTGRES_HOST" "$POSTGRES_PORT"; do
    echo "   Postgres not ready yet..."
    sleep 2
  done
  echo "✅ Postgres is up!"
fi

# ------------------------
# Wait for Eureka
# ------------------------
if [ -n "$EUREKA_HOSTPORT" ]; then
  echo "⏳ Waiting for Eureka at $EUREKA_HOST:$EUREKA_PORT..."
  while true; do
    STATUS=$(curl -s "http://$EUREKA_HOST:$EUREKA_PORT/actuator/health" || echo "")
    if echo "$STATUS" | grep -q '"status":"UP"'; then
      echo "✅ Eureka is up!"
      break
    else
      echo "   Eureka not ready yet..."
      sleep 3
    fi
  done
fi

# ------------------------
# Start the main service
# ------------------------
echo "🚀 All dependencies are ready. Starting the application..."
exec $CMD
