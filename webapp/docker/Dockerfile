FROM centos:centos6

RUN yum install -y java-1.7.0-openjdk
RUN yum install -y tar

ENV TOMCAT_MIRROR  http://mirror.lagoon.nc/pub/apache/tomcat
ENV TOMCAT_VERSION 7.0.57

WORKDIR /opt
RUN curl -s $TOMCAT_MIRROR/tomcat-7/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz |tar zxv
RUN ln -s apache-tomcat-$TOMCAT_VERSION tomcat

RUN rpm -Uvh http://yum.postgresql.org/9.3/redhat/rhel-6-x86_64/pgdg-redhat93-9.3-1.noarch.rpm
RUN yum install -y postgresql93-server
RUN /etc/init.d/postgresql-9.3 initdb
RUN sed -i 's,ident\|peer$,trust,' /var/lib/pgsql/9.3/data/pg_hba.conf

RUN yum install -y xz

USER postgres
COPY gasel.sql.xz /tmp/
RUN /usr/pgsql-9.3/bin/postmaster -p 5432 -D /var/lib/pgsql/9.3/data ${PGOPTS} & \
    sleep 1 && \
    createuser -R -S gasel_adm && \
    createdb -E UTF-8 -O gasel_adm gasel && \
    xzcat /tmp/gasel.sql.xz |psql -U gasel_adm gasel && \
    psql -U gasel_adm gasel -c "vacuum full analyze" && \
    kill $! && sleep 1

USER root

RUN ln -s /opt/tomcat/webapps /

ADD lib/postgresql-9.3-1100-jdbc41.jar /opt/tomcat/lib/
ADD lib/jt400.jar                      /opt/tomcat/lib/
ADD conf/context.xml      /opt/tomcat/conf/
ADD conf/tomcat-users.xml /opt/tomcat/conf/

WORKDIR /opt/tomcat
RUN rm -fr webapps/*
ADD gasel.war /opt/tomcat/webapps/ROOT.war

#RUN yum install -y openssh-server
#RUN /etc/init.d/sshd start
#RUN mkdir /root/.ssh
#ADD authorized_keys /root/.ssh/

#CMD /etc/init.d/sshd start && /etc/init.d/postgresql-9.3 start && bin/catalina.sh run
CMD /etc/init.d/postgresql-9.3 start && bin/catalina.sh run

VOLUME /var/lib/pgsql

ENV CATALINA_OPTS -Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.port=9090 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.awt.headless=true -XX:+UseConcMarkSweepGC -Xmx256m
