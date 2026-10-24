#!/bin/bash
set -e

psql \
  --username "$POSTGRES_USER" \
  --dbname "$POSTGRES_DB" \
  --set=test_db="$POSTGRES_TEST_DB" \
  --set=db_user="$POSTGRES_USER" \
  --set=db_password="$POSTGRES_PASSWORD" \
  --command="CREATE DATABASE \"$POSTGRES_TEST_DB\" OWNER \"$POSTGRES_USER\";"
