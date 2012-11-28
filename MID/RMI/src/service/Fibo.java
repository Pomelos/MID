package service ;
public class Fibo {
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
  public static void main (String [] arg) throws Exception{
    Fibo s=new Fibo();
    System.out.println("Calcul "+s.calcul(Integer.parseInt(arg[0])));
  }
}
