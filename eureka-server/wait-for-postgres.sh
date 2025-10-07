#!/bin/sh

host="$1"
port="$2"
shift 2
cmd="$@"

echo "Waiting for Postgres at $host:$port..."

# Loop until PostgreSQL is up
until nc -z "$host" "$port"; do
  echo "Postgres is not ready. Sleeping..."
  sleep 2
done

echo "Postgres is up - executing command"
exec $cmd