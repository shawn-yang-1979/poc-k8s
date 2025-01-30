oc set volumes dc/postgres-from-source \
--add \
--name data \
--type pvc \
--claim-name postgres-data
--claim-mode rwo \
--claim-size 1Gi \
--mount-path /temp