package config;

import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceConfig {
    @Getter
    private static ExecutorService executorService =
            Executors.newFixedThreadPool(4);

}
