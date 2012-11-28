package chatapp.model;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class Participant implements Serializable{

  private String name;

  private String email;

  Participant() 
  {
    this.name = "N/A";
    this.email = "N/A";
  }
  
  public void setName(String newname)
  {
    this.name = newname;
  }

  public String getName()
  { 
    return this.name;
  }

  public void setEmail(String newemail)
  {
    this.email = newemail;
  }

  public String getEmail()
  {
    return this.email;
  }

}
