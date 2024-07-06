package lfrc;

import lfrc.api.web.JsonResponseConverter;
import lfrc.api.web.WebServer;
import lfrc.model.Temperatures;

import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    private static final int NUM_LINE_PROCESSORS = 2; // could be provided as a command line parameter

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: Main <file>");
            System.exit(1);
        }

        // prepare dependencies
        var fileName = args[0];
        var temps = new Temperatures();
        var parser = new LineParser();
        var linesQueue = new LinkedBlockingQueue<String>();
        var responseConverter = new JsonResponseConverter();

        // start reader
        Thread.ofVirtual().start(new LargeFileReader(fileName, linesQueue));
        // this could have been done smarter:
        // reschedule the reader thread when it fails and start from the last line read

        // start web server
        new WebServer(temps, responseConverter).start();

        // start processor
        new LineProcessor(linesQueue, parser, NUM_LINE_PROCESSORS, temps).start();
    }
}
