oc set volumes dc/postgres-from-source \
--add \
--name=initdb \
--type=configmap \
--configmap-name=postgres-initdb \
--overwrite \
--mount-path=/docker-entrypoint-initdb.d