package se.vire.record;

import java.util.function.Consumer;

public class StreamProcessConfiguration {
    private final String id;
    private final String dataDir;
    private final String sourceRtsp;
    private final Consumer<String> stringConsumer;

    public StreamProcessConfiguration(String id, String dataDir, String sourceRtsp, Consumer<String> stringConsumer) {
        this.id = id;
        this.dataDir = dataDir;
        this.sourceRtsp = sourceRtsp;
        this.stringConsumer = stringConsumer;
    }

    public String getId() {
        return id;
    }

    public String getDataDir() {
        return dataDir;
    }

    public String getSourceRtsp() {
        return sourceRtsp;
    }

    public Consumer<String> getStringConsumer() {
        return stringConsumer;
    }
}
