FROM postgres:12

RUN chmod -R g=u /var/lib/postgresql/data /var/run/postgresql && \
    chown -R 1004780000:0 /var/lib/postgresql/data /var/run/postgresql

# Set default user to 1001 (arbitrary non-root user)
USER 1004780000