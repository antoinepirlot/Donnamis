package be.vinci.pae.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerHandler {

  private static final String LOG_FILE_PATH = "src/main/java/be/vinci/pae/data/";
  private static FileHandler fileHandler;
  private static Logger logger;

  public static void init() {
    logger = Logger.getLogger("LoggerHandler");
    try {
      File file = new File(LOG_FILE_PATH);
      if (!file.exists()) {
        if(!file.mkdir()) {
          throw new FileSystemException("Can't create directory");
        }
      }
      LocalDateTime now = LocalDateTime.now();
      String logFileName = now.getDayOfMonth() + "-"
          + now.getMonthValue() + "-"
          + now.getYear() + "_"
          + now.getHour() + "-"
          + now.getMinute() + "-"
          + now.getSecond() + "_log.log";
      System.out.println(logFileName);
      fileHandler = new FileHandler(LOG_FILE_PATH + logFileName, 500, 10, true);
      logger.addHandler(fileHandler);
      fileHandler.setFormatter(new SimpleFormatter());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Logger getLogger() {
    return logger;
  }

  public static void close() {
    fileHandler.close();
  }
}
