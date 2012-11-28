package service;

public class FiboImpl {

  public static void main(String [] arg) throws Exception{
    //creation du service distant
    service.FiboIfc srv=new service.FiboImpl();
    //si le serveur n'étend pas UnicastRemoteObject
    FiboIfc stub = (Fibo) UnicastRemoteObject.exportObject(srv,0);
    //enregistrement du service dans l'annuaire
    java.rmi.Naming.bind(FiboIfc.class.getName(), srv);
  
  }

  public int calcul(int val){
    int val1=0,val2=1,val3=0;
    for (int i=0; i<val; i++){
      val3=val1+val2;
      val1=val2;
      val2=val3;
      System.out.println("val "+val3);
    }  
    return val3;
  } 

}
