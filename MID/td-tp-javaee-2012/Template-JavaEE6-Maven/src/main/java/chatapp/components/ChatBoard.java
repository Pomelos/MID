package chatapp.components;

import java.util.LinkedList;
import chatapp.model.ChatMessage;
import chatapp.presentation.*;
import javax.ejb.Singleton;
import javax.ejb.Stateful;
import chatapp.model.*;
import chatapp.components.*;
import java.util.Date;

@Stateful
@Singleton
public class ChatBoard{
	
	protected LinkedList<ChatMessage> messagelist;
	
	ChatBoard()
	{
		this.messagelist = new LinkedList<ChatMessage>();
		messagelist.add(new ChatMessage(new Participant(),new Date(),"pouet"));
	}
	
	public void addMessage(ChatMessage message)
	{
		this.messagelist.add(message);
	}
	
	public LinkedList<ChatMessage> getList()
	{
		return this.messagelist;
	}
	
	public ChatMessage getMessage(int index)
	{
		return this.messagelist.get(index);
	}
	
	public String getStringMessage(int index)
	{
		return this.messagelist.get(index).getMessage();
	}
	
	public ChatMessage getLastMessage()
	{
		return this.messagelist.getLast();
	}
}
