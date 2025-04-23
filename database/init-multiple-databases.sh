#!/bin/bash

set -e
set -u

function create_user_and_db() {
	local database=$1
	echo "  Creating database '$database'"
	psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	    CREATE DATABASE $database;
	    GRANT ALL PRIVILEGES ON DATABASE $database TO $POSTGRES_USER;
EOSQL
}

if [ -n "$POSTGRES_USER" ] && [ -n "$POSTGRES_PASSWORD" ]; then
	echo "Creating databases: authentication, userprofile, workout"
	create_user_and_db 'authentication'
	create_user_and_db 'userprofile'
	create_user_and_db 'workout'
	echo "Multiple databases created"
fi
