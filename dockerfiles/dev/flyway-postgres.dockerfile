FROM flyway/flyway:latest-alpine

VOLUME [ "/flyway/conf", "/flyway/drivers", "/flyway/sql", "/flyway/jars" ]

COPY ./db-postgres/flyway.conf "/flyway/conf"
COPY ./db-postgres/flyway-psql "/flyway/sql"

ENV FLYWAY_EDITION=community
