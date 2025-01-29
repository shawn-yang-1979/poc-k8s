FROM postgres:12

RUN chown -R 1004780000:0 /var/lib/postgresql/data && \
    chown -R 1004780000:0 /var/run/postgresql && \
    chmod -R g=u /var/lib/postgresql/data && \ 
    chmod -R g=u /var/run/postgresql