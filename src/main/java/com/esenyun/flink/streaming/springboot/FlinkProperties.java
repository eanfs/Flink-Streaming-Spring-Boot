package com.esenyun.flink.streaming.springboot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "esenyun.flink")
public class FlinkProperties {

    @NotNull
    private String jobName;

    @NotNull
    private String jobManagerUrl;

    private int jobManagerPort;

    @NotNull
    private List<String> remoteEnvJarFiles;

    private long maxClientRestRequestSizeBytes;

    private boolean terminate;

    private long terminationGracePeriodMs;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobManagerUrl() {
        return jobManagerUrl;
    }

    public void setJobManagerUrl(String jobManagerUrl) {
        this.jobManagerUrl = jobManagerUrl;
    }

    public int getJobManagerPort() {
        return jobManagerPort;
    }

    public void setJobManagerPort(int jobManagerPort) {
        this.jobManagerPort = jobManagerPort;
    }

    public List<String> getRemoteEnvJarFiles() {
        return remoteEnvJarFiles;
    }

    public void setRemoteEnvJarFiles(List<String> remoteEnvJarFiles) {
        this.remoteEnvJarFiles = remoteEnvJarFiles;
    }

    public long getMaxClientRestRequestSizeBytes() {
        return maxClientRestRequestSizeBytes;
    }

    public void setMaxClientRestRequestSizeBytes(long maxClientRestRequestSizeBytes) {
        this.maxClientRestRequestSizeBytes = maxClientRestRequestSizeBytes;
    }

    public boolean isTerminate() {
        return terminate;
    }

    public void setTerminate(boolean terminate) {
        this.terminate = terminate;
    }

    public long getTerminationGracePeriodMs() {
        return terminationGracePeriodMs;
    }

    public void setTerminationGracePeriodMs(long terminationGracePeriodMs) {
        this.terminationGracePeriodMs = terminationGracePeriodMs;
    }
}
