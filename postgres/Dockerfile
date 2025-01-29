FROM postgres:12

RUN chown -R 1001:0 /var/lib/postgresql/data /var/run/postgresql

# Set default user to 1001 (arbitrary non-root user)
USER 1001