FROM eclipse-temurin:21-jdk

ENV SBT_HOME="/opt/sbt"
ENV SBT_VERSION="1.12.1"
ENV PATH="${SBT_HOME}/bin:${PATH}"

ENV SPARK_HOME="/opt/spark"
ENV SPARK_VERSION="4.1.1"
ENV HADOOP_VERSION="3"
ENV PATH="${SPARK_HOME}/bin:${PATH}"

WORKDIR /opt

# SBT (GitHub Releases -- the official, permanent source)
ADD "https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz" .
RUN tar -xzf sbt*.tgz && rm -f sbt*.tgz

# Spark (archive.apache.org, not downloads.apache.org -- the latter only
# mirrors current releases and will 404 once this version is superseded)
ADD "https://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz" .
RUN tar -xzf spark*.tgz && rm -f spark*.tgz && mv spark* spark

CMD tail -f /dev/null
