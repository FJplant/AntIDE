//Title:        Ant Developer
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Kwon, Young Mo
//Company:      Antsoft Co.
//Description:  Ant Developer Project

package test;

import java.rmi.*;
import javax.ejb.*;

public class Enterprise1Bean implements EntityBean {
  private EntityContext entityContext;

  public void ejbCreate() throws CreateException {
  }

  public void ejbActivate() throws RemoteException {
  }

  public void ejbLoad() throws RemoteException {
  }

  public void ejbPassivate() throws RemoteException {
  }

  public void ejbRemove() throws RemoteException, RemoveException {
  }

  public void ejbStore() throws RemoteException {
  }

  public void setEntityContext(EntityContext context) throws RemoteException {
    entityContext = context;
  }

  public void unsetEntityContext() throws RemoteException {
    entityContext = null;
  }
}
