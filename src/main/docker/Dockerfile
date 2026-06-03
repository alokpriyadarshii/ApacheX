FROM eclipse-temurin:25.0.3_9-jdk

ADD ApacheX.sh /
ADD maven/ /ApacheX/

RUN chmod +x /ApacheX.sh

EXPOSE 9000

ENTRYPOINT ["/ApacheX.sh"]
