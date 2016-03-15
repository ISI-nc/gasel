FROM centos:centos7

RUN yum install -y java-1.8.0-openjdk
RUN yum install -y tar

ENV TOMCAT_MIRROR  http://mirror.lagoon.nc/pub/apache/tomcat
ENV TOMCAT_VERSION 8.0.32

WORKDIR /opt
RUN curl -s $TOMCAT_MIRROR/tomcat-${TOMCAT_VERSION%%.*}/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz |tar zxv
RUN ln -s apache-tomcat-$TOMCAT_VERSION tomcat

USER root

RUN ln -s /opt/tomcat/webapps /

ADD lib/postgresql-9.3-1100-jdbc41.jar /opt/tomcat/lib/
ADD lib/jt400.jar                      /opt/tomcat/lib/
ADD conf/context.xml      /opt/tomcat/conf/
ADD conf/tomcat-users.xml /opt/tomcat/conf/

WORKDIR /opt/tomcat
RUN rm -fr webapps/*
ADD gasel.war /opt/tomcat/webapps/ROOT.war

CMD bin/catalina.sh run

VOLUME /var/lib/pgsql

ENV CATALINA_OPTS -Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.port=9090 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.awt.headless=true -XX:+UseConcMarkSweepGC -Xmx256m
