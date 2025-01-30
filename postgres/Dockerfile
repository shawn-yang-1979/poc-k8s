FROM postgres:12

# Reference: https://suedbroecker.net/2020/06/23/run-a-postgressql-container-as-a-non-root-user-in-openshift/
# Create the needed temp file before the first postgreSQL execution
RUN mkdir temp

# Set user rights to allow the on-root-postgres-user 
# to access the temp folder
RUN chgrp -R 0 /temp
RUN chmod -R g=u /temp
RUN chgrp -R 0 /var/run/postgresql
RUN chmod -R g=u /var/run/postgresql

USER postgres