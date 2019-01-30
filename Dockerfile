FROM tomcat:7.0-jre8

# add TEAM engine webapp
ADD target/dependency/teamengine-web-*.war /root/
RUN cd /root/ && unzip -q teamengine-web-*.war -d /usr/local/tomcat/webapps/teamengine

# add common libs
ADD target/dependency/teamengine-web-*-common-libs.zip /root/
RUN cd /root/ && unzip -q teamengine-web-*-common-libs.zip -d /usr/local/tomcat/lib

# add TEAM engine console
ADD target/dependency/teamengine-console-*-base.zip /root/
RUN cd /root/ && unzip -q teamengine-console-*-base.zip -d /root/te_base

# set TE_BASE
ENV JAVA_OPTS="-Xms1024m -Xmx2048m -DTE_BASE=/root/te_base"

# add ETS for STA 1.0
ADD target/ets-sta10-*-ctl.zip /root/
RUN cd /root/ && unzip -q ets-sta10-*-ctl.zip -d /root/te_base/scripts
ADD target/ets-sta10-*-deps.zip /root/
RUN cd /root/ && unzip -q -o ets-sta10-*-deps.zip -d /usr/local/tomcat/webapps/teamengine/WEB-INF/lib

# run tomcat
CMD ["catalina.sh", "run"]