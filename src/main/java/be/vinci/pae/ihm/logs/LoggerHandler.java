package be.vinci.pae.ihm.logs;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerHandler {

  private static final String LOG_FILE_PATH = "src/main/java/be/vinci/pae/ihm/logs/data/";
  private static FileHandler fileHandler;
  private static Logger logger;

  /**
   * Initialize the LoggerHandler by creating a file handler and the logger.
   */
  public static void init() {
    logger = Logger.getLogger("LoggerHandler");
    try {
      File file = new File(LOG_FILE_PATH);
      if (!file.exists() && !file.mkdir()) {
        throw new FileSystemException("Can't create directory");
      }
      fileHandler = new FileHandler(LOG_FILE_PATH + "logs.log", 20480, 5, true);
      fileHandler.setFormatter(new SimpleFormatter());
      logger.addHandler(fileHandler);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * return the logger.
   *
   * @return the logger
   */
  public static Logger getLogger() {
    return logger;
  }

  /**
   * Close the file handler.
   */
  public static void close() {
    fileHandler.close();
  }
}
