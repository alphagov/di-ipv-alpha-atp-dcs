#!/usr/bin/env bash
KEY_PATH="src/main/resources/keys"
CERT_PATH="src/main/resources/certs"
[[ -d $KEY_PATH ]] || mkdir $KEY_PATH
[[ -d $CERT_PATH ]] || mkdir $CERT_PATH

openssl req -newkey rsa:2048 -subj "/O=Digital Identity/CN=client-signing" -new -nodes -x509 -days 3650 -keyout $KEY_PATH/client-signing-key.pem -out $CERT_PATH/client-signing-cert.pem
openssl req -newkey rsa:2048 -subj "/O=Digital Identity/CN=client-encryption" -new -nodes -x509 -days 3650 -keyout $KEY_PATH/client-encryption-key.pem -out $CERT_PATH/client-encryption-cert.pem
openssl req -newkey rsa:2048 -subj "/O=Digital Identity/CN=server-signing" -new -nodes -x509 -days 3650 -keyout $KEY_PATH/server-signing-key.pem -out $CERT_PATH/server-signing-cert.pem
openssl req -newkey rsa:2048 -subj "/O=Digital Identity/CN=server-encryption" -new -nodes -x509 -days 3650 -keyout $KEY_PATH/server-encryption-key.pem -out $CERT_PATH/server-encryption-cert.pem
openssl req -newkey rsa:2048 -subj "/O=Digital Identity/CN=ipv-signing" -new -nodes -x509 -days 3650 -keyout $KEY_PATH/ipv-signing-key.pem -out $CERT_PATH/ipv-signing-cert.pem
