package be.vinci.pae.dal.services.interfaces;

import java.sql.Connection;

public interface DALServices {

  Connection start();

  boolean commit();

  boolean rollback();

}
