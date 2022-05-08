package be.vinci.pae.biz.refusal.objects;

import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import be.vinci.pae.biz.refusal.interfaces.RefusalUCC;
import be.vinci.pae.dal.refusal.interfaces.RefusalDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import jakarta.inject.Inject;
import java.sql.SQLException;

public class RefusalUCCImpl implements RefusalUCC {

  @Inject
  private DALServices dalServices;

  @Inject
  private RefusalDAO refusalDAO;

  @Override
  public RefusalDTO getRefusal(String username) {
    try {
      this.dalServices.start();
      RefusalDTO refusalDTO = this.refusalDAO.getRefusal(username);
      this.dalServices.commit();
      if (refusalDTO == null) {
        String message = "Refusal information for the member " + username + " not found.";
        throw new ObjectNotFoundException(message);
      }
      return refusalDTO;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }
}
