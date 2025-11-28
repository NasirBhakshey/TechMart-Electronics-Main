#!/bin/bash
set -e

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

if [ -n "$POSTGRES_HOSTPORT" ]; then
  POSTGRES_HOST=$(echo "$POSTGRES_HOSTPORT" | cut -d: -f1)
  POSTGRES_PORT=$(echo "$POSTGRES_HOSTPORT" | cut -d: -f2)
fi

if [ -n "$EUREKA_HOSTPORT" ]; then
  EUREKA_HOST=$(echo "$EUREKA_HOSTPORT" | cut -d: -f1)
  EUREKA_PORT=$(echo "$EUREKA_HOSTPORT" | cut -d: -f2)
fi

if [ -n "$POSTGRES_HOSTPORT" ]; then
  echo "⏳ Waiting for Postgres at $POSTGRES_HOST:$POSTGRES_PORT..."
  until nc -z "$POSTGRES_HOST" "$POSTGRES_PORT"; do
    echo "   Postgres not ready yet..."
    sleep 2
  done
  echo "✅ Postgres is up!"
fi

if [ -n "$EUREKA_HOSTPORT" ]; then
  echo "⏳ Waiting for Eureka at $EUREKA_HOST:$EUREKA_PORT..."
  until curl -s "http://$EUREKA_HOST:$EUREKA_PORT/actuator/health" | grep -q '"status":"UP"'; do
    echo "   Eureka not ready yet..."
    sleep 3
  done
  echo "✅ Eureka is up!"
fi

echo "🚀 All dependencies ready, starting application..."
exec $CMD
