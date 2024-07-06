package lfrc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class LargeFileReader implements Runnable {

    private static final Logger LOG = Logger.getLogger(LargeFileReader.class.getName());

    private final BlockingQueue<String> linesQueue;
    private final String fileName;

    public LargeFileReader(String fileName, BlockingQueue<String> linesQueue) {
        this.fileName = fileName;
        this.linesQueue = linesQueue;
    }

    @Override
    public void run() {
        LOG.info(() -> "Reading file: " + fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while (true) {
                if ((line = reader.readLine()) != null) {
                    linesQueue.put(line);
                } else {
                    Thread.sleep(1000);
                }
            }
        } catch (IOException | InterruptedException ioe) {
            LOG.severe(() -> "Failed to read file: " + fileName + " " + ioe.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
