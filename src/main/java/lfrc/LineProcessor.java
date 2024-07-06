package lfrc;

import lfrc.model.Temperatures;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class LineProcessor {

    private static final Logger LOG = Logger.getLogger(LineProcessor.class.getName());

    private final BlockingQueue<String> linesQueue;
    private final LineParser parser;
    private final int numLineProcessors;
    private final Temperatures temps;

    public LineProcessor(BlockingQueue<String> linesQueue, LineParser parser, int numLineProcessors, Temperatures temps) {
        this.linesQueue = linesQueue;
        this.parser = parser;
        this.numLineProcessors = numLineProcessors;
        this.temps = temps;
    }

    public void start() {
        try (ExecutorService executor = Executors.newFixedThreadPool(numLineProcessors)) {
            for (int i = 0; i < numLineProcessors; i++) {
                executor.submit(() -> {
                    while (true) {
                        try {
                            String line = linesQueue.take();
                            LOG.fine(() -> "Processing line: " + line);
                            parser.parse(line).ifPresent(temps::addReading);
                        } catch (InterruptedException iex) {
                            LOG.warning(() -> "Line processor interrupted: " + iex.getMessage());
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                });
            }
        }
    }
}
