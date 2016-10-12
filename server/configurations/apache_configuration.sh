#!/bin/bash
# Server configuration for Apache2. Execute with sudo

APACHE_PATH="/etc/apache2"
SERVER_PATH="/home/wouter/server"

# Define port redirection
if [ -f $APACHE_PATH/conf.d/play.conf ]; then
	mv $APACHE_PATH/conf.d/play.conf $SERVER_PATH/config_backup/play.conf
fi

cd $SERVER_PATH/configurations

cp ./play.conf $APACHE_PATH/conf.d/
mkdir -p $APACHE_PATH/ssl
cp ./ssl/* $APACHE_PATH/ssl/

# Load proxy modules
a2enmod proxy
a2enmod proxy_http
a2enmod ssl

# Restart apache server
service apache2 reload
service apache2 restart

