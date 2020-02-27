FROM mysql
COPY ./schema-with-test-data.sql /docker-entrypoint-initdb.d
EXPOSE 3306
ENV MYSQL_ROOT_PASSWORD=admin
# ENV MYSQL_DATABASE=U04ioP