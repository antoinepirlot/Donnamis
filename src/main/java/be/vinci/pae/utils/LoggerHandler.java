package be.vinci.pae.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerHandler {

  private static final String LOG_FILE_PATH = "src/main/java/be/vinci/pae/data/";
  private static final Logger logger = Logger.getGlobal();

  public static void infoLog(String method, Class<?> className) {
    infoLog(method, className, "");
  }

  public static void infoLog(String method, Class<?> className, String message) {
    logger.log(Level.INFO, getLog(method, className, message));
  }

  public static void severeLog(String method, Class<?> className) {
    severeLog(method, className, "");
  }

  public static void severeLog(String method, Class<?> className, String message) {
    logger.log(Level.SEVERE, getLog(method, className, message));
  }

  private static String getLog(String method, Class<?> className, String message) {
    String log = "Method: " + method + " Class: " + className.toString();
    if (!message.equals("")) {
      log += " Message: " + message;
    }
    File file = new File(LOG_FILE_PATH);
    if (!file.exists()) {
      file.mkdir();
    }
    try {
      FileHandler fileHandler = new FileHandler(LOG_FILE_PATH + className + ".xml");
      logger.addHandler(fileHandler);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return log;
  }

}
