package com.dkramer.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.IllegalFormatConversionException;
import java.util.MissingFormatArgumentException;

/**
 * Logger class for logging various events to a log file
 * Created by David Kramer on 1/7/2016.
 */
public class Logger {
    private static boolean initialized = false; // has logger been initialized yet?
    private static String filename = null;  // name of file to write to
    private static PrintWriter writer = null;   // log file writer



    /**
     * Initializes the logger with a specialized filename so that
     * it is ready to log events and write to a log file.
     */
    public static boolean init(String name) {
        if (!initialized) {
            filename = checkForExtension(name, ".log");    // make sure we have .log at the end
            log(MessageLevel.INFO, "Logger initialized with filename [%s]", filename);
            try {
                writer = new PrintWriter(filename);
            } catch (FileNotFoundException e) {
                Logger.log(MessageLevel.ERROR, e.getMessage());
                return false;
            }
            initialized = true;
            log(MessageLevel.HEADER, MessageLevel.getEnabled());
        }
        return initialized;
    }

    /**
     * Returns a timestamp containing the current date and time.
     * The format of the timestamp is:
     * 2016-08-14 02:22:15:242
     * @return  a timestamp as a String
     */
    public static String getTimestamp() {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return df.format(date.getTime());
    }

    /**
     * Logs with a specified message level and message.
     * @param msgLevel - MessageLevel type
     * @param msg - message contents
     */
    public static void log(MessageLevel msgLevel, String msg) {
        writeLog(msgLevel, msg);
    }

    /**
     * Generic method that logs a specified MessageLevel and message. The method can contain
     * format strings such as %s, %d, %f, etc... and the type will be inferred at compile time.
     * If an argument that is passed doesn't match the format string, the resulting errors
     * will be logged, indicating an illegal use of this method!
     * @param msgLevel - MessageLevel type
     * @param msg - message contents
     * @param args - arguments for the message contents, if it contains String formatters
     * @param <T>
     */
    public static <T> void log(MessageLevel msgLevel, String msg,  T... args) {
        try {
            String logMsg = String.format(msg, args);
            log(msgLevel, logMsg);
        } catch (IllegalFormatConversionException e) {
            log(MessageLevel.ERROR, "Usage error of log() Invalid symbol -> " + e.getMessage());
        } catch (MissingFormatArgumentException e) {
            log(MessageLevel.ERROR, "Usage error of log() Missing an argument -> " + e.getMessage());
        }
    }

    /**
     * Appends the specified log message to the existing log file that has
     * been written to by the PrintWriter.
     */
    public static void writeToFile(String logMsg) {
        if (initialized) {
            writer.append(logMsg + "\n");
            writer.flush();
        }
    }

    /**
     * Shuts down logger and uninitializes it.
     */
    public static void shutdown() {
        log(MessageLevel.INFO, "Logger has shutdown successfully!");
        writer.close();
        initialized = false;
        filename = null;
    }

    /**
     * Writes to a log and formats it nicely, by adding some padding around
     * important information such as the date, and type of message. It also
     * stores the logMsg in the logMsg's ArrayList.
     * @param msgLevel - MessageLevel of the msg to be logged
     * @param msg - Detailed log msg
     */
    private static void writeLog(MessageLevel msgLevel, String msg) {
        if (!initialized && filename == null) {
            System.err.println("Logger not initialized! Call init() before using!");
            return;
        }

        if (msgLevel.isEnabled()) {
            String logMsg = String.format("%-10s \t %-10s \t %-20s", getTimestamp(), msgLevel, msg);
            System.out.println(logMsg);
            writeToFile(logMsg);
        }
    }


    /**
     * Checks to make sure that the filename contains the specified extension
     * at the end. If it doesn't, this method will append it.
     * @param filename - name of file
     * @param ext - the extension of the file to check if exists
     * @return String containing extension, if it didn't exist already
     */
    private static String checkForExtension(String filename, String ext) {
        if (!filename.endsWith(ext)) {
            Logger.log(MessageLevel.INFO, "[%s] didn't end with [%s]. It has now been appended!", filename, ext);
            filename += ext;
        }
        return filename;
    }
}