package be.vinci.pae.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerHandler {

  private static final String LOG_FILE_PATH = "src/main/java/be/vinci/pae/logs/";
  private static FileHandler fileHandler;
  private static Logger logger;

  public static void init() {
    logger = Logger.getLogger("LoggerHandler");
    try {
      File file = new File(LOG_FILE_PATH);
      if (!file.exists()) {
        if (!file.mkdir()) {
          throw new FileSystemException("Can't create directory");
        }
      }
      fileHandler = new FileHandler(LOG_FILE_PATH + "logs.log", Integer.MAX_VALUE, 10, true);
      fileHandler.setFormatter(new SimpleFormatter());
      logger.addHandler(fileHandler);
      logger.setUseParentHandlers(false);
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
