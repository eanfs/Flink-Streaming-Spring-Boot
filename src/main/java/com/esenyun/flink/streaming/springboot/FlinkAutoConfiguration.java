package com.esenyun.flink.streaming.springboot;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableConfigurationProperties(FlinkProperties.class)
public class FlinkAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(FlinkAutoConfiguration.class);

    @Bean
    FlinkExecutor flinkExecutor(ApplicationContext appCtx, TaskExecutor taskExecutor,
        FlinkProperties flinkProperties, StreamExecutionEnvironment streamEnvironment) {

        return new FlinkExecutor(appCtx, taskExecutor, flinkProperties, streamEnvironment);
    }

    @Bean("taskExecutor")
    TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean("streamEnvironment")
    StreamExecutionEnvironment getStreamEnvironment(FlinkProperties flinkProperties) {
        long maxBytes = flinkProperties.getMaxClientRestRequestSizeBytes();
        org.apache.flink.configuration.Configuration config = new org.apache.flink.configuration.Configuration();
        log.info("Set streamEnvironment with address:{} port:{}", flinkProperties.getJobManagerUrl(), flinkProperties.getJobManagerPort());
        config.setString("rest.address", flinkProperties.getJobManagerUrl());
        config.setInteger("rest.port", flinkProperties.getJobManagerPort());
        config.setLong("rest.client.max-content-length", maxBytes);
        config.setLong("rest.server.max-content-length", maxBytes);
        config.setString("akka.framesize", maxBytes + "b");

        return StreamExecutionEnvironment.createRemoteEnvironment(
            flinkProperties.getJobManagerUrl(),
            flinkProperties.getJobManagerPort(),
            config,
            flinkProperties.getRemoteEnvJarFiles().stream().toArray(String[]::new));
    }


    @Bean("executionEnvironment")
    ExecutionEnvironment getExecutionEnvironment(FlinkProperties flinkProperties) {
        long maxBytes = flinkProperties.getMaxClientRestRequestSizeBytes();
        org.apache.flink.configuration.Configuration config = new org.apache.flink.configuration.Configuration();
        log.info("Set streamEnvironment with address:{} port:{}", flinkProperties.getJobManagerUrl(), flinkProperties.getJobManagerPort());
        config.setString("rest.address", flinkProperties.getJobManagerUrl());
        config.setInteger("rest.port", flinkProperties.getJobManagerPort());
        config.setLong("rest.client.max-content-length", maxBytes);
        config.setLong("rest.server.max-content-length", maxBytes);
        config.setString("akka.framesize", maxBytes + "b");

        return ExecutionEnvironment.createRemoteEnvironment(
                flinkProperties.getJobManagerUrl(),
                flinkProperties.getJobManagerPort(),
                config,
                flinkProperties.getRemoteEnvJarFiles().stream().toArray(String[]::new));
    }
}
