package se.vire.record;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StreamProcess {
    private Process process;

    private StreamProcess(Process process) {
        this.process = process;
    }

    static StreamProcess create(StreamProcessConfiguration streamProcessConfiguration) throws IOException {
        ProcessBuilder builder = new ProcessBuilder();

        Path filenameTemplate = Paths.get(streamProcessConfiguration.getDataDir(),
                "stream_" + streamProcessConfiguration.getId() + "_%Y-%m-%d_%H-%M-%S.mkv");

        builder.command("bash", "-c", "ffmpeg -rtsp_transport tcp -y -stimeout 10 -i " + streamProcessConfiguration.getSourceRtsp() +
                " -c copy -f segment -segment_time 360 -strftime 1 " +
                filenameTemplate.toString());

        builder.directory(new File(streamProcessConfiguration.getDataDir()));
        Process p = builder.start();

        StreamGobbler streamGobblerInput = new StreamGobbler(p.getInputStream(), streamProcessConfiguration.getStringConsumer());
        StreamGobbler streamGobblerError = new StreamGobbler(p.getErrorStream(), streamProcessConfiguration.getStringConsumer());
        Executors.newSingleThreadExecutor().submit(streamGobblerInput);
        Executors.newSingleThreadExecutor().submit(streamGobblerError);
        return new StreamProcess(p);
    }

    void stop() throws InterruptedException {
        if (process != null) {
            process.destroy();
            if (!process.waitFor(5, TimeUnit.SECONDS)) {
                process.destroyForcibly();
            }

        }
    }

    boolean isAlive() {
        return process.isAlive();
    }

    long secondsSinceStart() {
        return Instant.now().getEpochSecond() - process.info().startInstant().get().getEpochSecond();
    }

}
