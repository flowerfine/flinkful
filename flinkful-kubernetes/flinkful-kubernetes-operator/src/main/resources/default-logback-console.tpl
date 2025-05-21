<configuration>
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{60} %X{sourceThread} - %msg%n</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>${console.log.level:-ALL}</level>
        </filter>
    </appender>

    <appender name="rolling" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${log.file}</file>
        <append>false</append>

        <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
            <fileNamePattern>${log.file}.%i</fileNamePattern>
            <minIndex>1</minIndex>
            <maxIndex>10</maxIndex>
        </rollingPolicy>

        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <maxFileSize>100MB</maxFileSize>
        </triggeringPolicy>

        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{60} %X{sourceThread} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- This affects logging for both user code and Flink -->
    <root level="{{ rootLoggerLevel }}">
        <appender-ref ref="console"/>
        <appender-ref ref="rolling"/>
    </root>

    <!-- Uncomment this if you want to only change Flink's logging -->
    <!--<logger name="org.apache.flink" level="INFO"/>-->

    <!-- The following lines keep the log level of common libraries/connectors on
         log level INFO. The root logger does not override this. You have to manually
         change the log levels here. -->
    <logger name="org.apache.pekko" level="INFO"/>
    <logger name="org.apache.kafka" level="INFO"/>
    <logger name="org.apache.hadoop" level="INFO"/>
    <logger name="org.apache.zookeeper" level="INFO"/>

    <!-- Suppress the irrelevant (wrong) warnings from the Netty channel handler -->
    <logger name="org.jboss.netty.channel.DefaultChannelPipeline" level="ERROR"/>

    <!-- User logger here -->
    {%- if loggers %}
    {%- for logger in loggers %}
    <logger name="{{ logger.logger }}" level="{{ logger.level }}"/>
    {%- endfor %}
    {%- endif %}
</configuration>