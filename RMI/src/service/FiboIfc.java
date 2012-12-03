package service;

import java.rmi.*;

public interface FiboIfc extends Remote{

  public void main (String [] arg);
  public int calcul(int val) throws RemoteException;

}
