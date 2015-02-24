package sk.branislavremen.universityapp.vo;

import com.parse.ParseClassName;
import com.parse.ParseObject;



@ParseClassName("Message")
public class MessageData extends ParseObject {

	String room;
	
	public String getUserId() {
		return getString("userId");
	}

	public String getBody() {
		return getString("body");
	}
	
	public String getRoom() {
		return getString("room");
	}

	public void setUserId(String userId) {
		put("userId", userId);
	}

	public void setBody(String body) {
		put("body", body);
	}
	
	public void setRoom(String room) {
		put("room", room);
	}
}
