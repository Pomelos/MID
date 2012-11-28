package chatapp.components;

import java.util.LinkedList;
import chatapp.model.ChatMessage;
import chatapp.presentation.*;
import javax.ejb.Singleton;

@Stateful
@Singleton
public class ChatBoard{
	
	protected LinkedList<ChatMessage> messagelist;
	
	ChatBoard()
	{
		this.messagelist = new LinkedList<ChatMessage>();
	}
	
	public void addMessage(ChatMessage message)
	{
		this.messagelist.add(message);
	}
	
	public ChatMessage getMessage(int index)
	{
		return this.messagelist.get(index);
	}
	
	public ChatMessage getLastMessage()
	{
		return this.messagelist.getLast();
	}
}
