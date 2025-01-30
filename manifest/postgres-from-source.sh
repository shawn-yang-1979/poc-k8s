oc new-app https://github.com/shawn-yang-1979/examples \
--context-dir=postgres \
--source-secret=github \
--name=postgres-from-source \
--as-deployment-config \
--env=POSTGRES_PASSWORD=postgrespw \
--env=PGDATA=/temp/data \
--dry-run \
-o yaml > postgres-from-source.yaml