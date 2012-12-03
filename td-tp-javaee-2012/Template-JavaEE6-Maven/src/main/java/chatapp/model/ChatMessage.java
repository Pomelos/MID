package chatapp.model;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

@Named
@SessionScoped
public class ChatMessage implements Serializable{

  private Participant transmitter;

  private Date timestamp;

  private String message;

  ChatMessage() 
  {
    this.transmitter = new Participant();
    this.timestamp = new Date();
    this.message = "empty";
  }
  
  public ChatMessage(Participant participant, Date date, String themessage) 
  {
    this.transmitter = participant;
    this.timestamp = new Date();
    this.message = themessage;
  }
  
  public void setTransmitter(Participant messenger)
  {
    this.transmitter = messenger;
  }

  public Participant getTransmitter()
  { 
    return this.transmitter;
  }

  public void updateTime()
  {
    this.timestamp = new Date();
  }

  public Date getDate()
  {
    return this.timestamp;
  }
  
  public void setMessage(String watchasayin)
  {
		this.message = watchasayin;
	}
	
	public String getMessage()
	{
		return this.message;
	}

}
