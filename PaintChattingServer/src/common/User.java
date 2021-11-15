package common;

public class User {
	public String		id;
	public String		nick;
	public Image		profilePic;
	public Font			font;
	public User[]		friends;
	public ChatRoom[]	chatRoom;
	
	public User (String id, String nick, Image profilePic, Font font, User[] friends, ChatRoom[] chatRoom) {
		this.id = id;
		this.nick = nick;
		this.profilePic = profilePic;
		this.font = font;
		this.friends = friends;
		this.chatRoom = chatRoom;
	}
	
}
