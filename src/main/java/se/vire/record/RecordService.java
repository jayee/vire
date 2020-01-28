package se.vire.record;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public class RecordService {

    Logger log = Logger.getLogger(RecordService.class.getName());

    List<StreamProcess> processList = new ArrayList<>();

    @ConfigProperty(name = "vire.streams")
    List<String> streams;

    @ConfigProperty(name = "vire.data.dir")
    String dataDirectory;

    void startup(@Observes StartupEvent event) {
        log.info("Starting up");
    }

    String startRecord() {
        if (streams.isEmpty() || !streams.get(0).contains("rtsp")) {
            throw new RuntimeException("No streams to record");
        }

        if (processList.stream().anyMatch(p -> p.isAlive())) {
            return "There are still recording(s) going on";
        }

        processList = new ArrayList<>();

        for (int i = 0; i < streams.size(); i++) {
            log.info("Processing stream: " + streams.get(i));

            StreamProcessConfiguration config = new StreamProcessConfiguration(
                    String.valueOf(i + 1),
                    dataDirectory,
                    streams.get(i),
                    System.out::println
            );

            try {
                StreamProcess streamProcess = StreamProcess.create(config);
                processList.add(streamProcess);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return String.format("Recording started for %d streams", processList.size());
    }

    String stopRecord() {
        processList.stream().filter(p -> p.isAlive()).forEach(p -> {
            try {
                p.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        return "Recording(s) stopped";
    }

    @Scheduled(every = "60s")
    void stopLongRunningStreams() {
        processList.stream().filter(p -> p.isAlive()).forEach(p -> {
            if (p.secondsSinceStart() > 60 * 30) {
                try {
                    p.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
