package com.esenyun.flink.streaming.springboot;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeutils.base.IntSerializer;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.FromElementsFunction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
    FlinkStreamingSpringBootTest.class,
    FlinkStreamingSpringBootTest.FlinkStreamingSpringBootTestPropertiesConfiguration.class,
    FlinkStreamingSpringBootTest.FlinkStreamingSpringBootTestConfiguration.class
})
@ComponentScan("com.esenyun.flink.streaming.springboot")
public class FlinkStreamingSpringBootTest {

    @TestConfiguration
    static class FlinkStreamingSpringBootTestPropertiesConfiguration {

        @Bean
        String outputFileName() {
            return "target/FlinkStreamingSpringBootTest.txt";
        }
    }

    @TestConfiguration
    static class FlinkStreamingSpringBootTestConfiguration {

        @Bean("streamEnvironment")
        @Primary
        StreamExecutionEnvironment getStreamEnvironment(FlinkProperties flinkProperties) {
            return StreamExecutionEnvironment.createLocalEnvironment();
        }

        @Bean("executionEnvironment")
        @Primary
        ExecutionEnvironment getExecutionEnvironment(FlinkProperties flinkProperties) {
            return ExecutionEnvironment.createLocalEnvironment();
        }

        @Autowired
        void populateEnv(StreamExecutionEnvironment streamEnvironment, String outputFileName)
            throws IOException {
            streamEnvironment
                .addSource(
                    new FromElementsFunction<>(new IntSerializer(), 1, 2, 3),
                    TypeInformation.of(Integer.class))
                .filter((Integer i) -> i % 2 == 0)
                .writeAsText(outputFileName, FileSystem.WriteMode.OVERWRITE)
                .setParallelism(1);
        }
    }

    @Autowired
    FlinkProperties flinkProperties;

    @Autowired
    String outputFileName;

    @Test
    public void localStreamExecution() throws InterruptedException, IOException {
        Thread.sleep(flinkProperties.getTerminationGracePeriodMs() / 2); // fixme

        String outputFileText = new String(Files.readAllBytes(Paths.get(outputFileName))).trim();
        assertThat(outputFileText).isEqualTo("2");
    }

}
