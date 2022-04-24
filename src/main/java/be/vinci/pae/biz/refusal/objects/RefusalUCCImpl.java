package be.vinci.pae.biz.refusal.objects;

import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import be.vinci.pae.biz.refusal.interfaces.RefusalUCC;
import be.vinci.pae.dal.refusal.interfaces.RefusalDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import jakarta.inject.Inject;
import java.sql.SQLException;

public class RefusalUCCImpl implements RefusalUCC {

  @Inject
  private DALServices dalServices;

  @Inject
  private RefusalDAO refusalDAO;

  @Override
  public RefusalDTO getRefusal(String username) throws SQLException {
    try {
      this.dalServices.start();
      RefusalDTO refusalDTO = this.refusalDAO.getRefusal(username);
      this.dalServices.commit();
      return refusalDTO;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw e;
    }
  }
}
