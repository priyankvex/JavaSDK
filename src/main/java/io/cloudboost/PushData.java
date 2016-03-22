package io.cloudboost;

public class PushData {
private String title;
private String message;
private String sound;
private String badge;
private String icon;

public String getSound() {
	return sound;
}
public void setSound(String sound) {
	this.sound = sound;
}
public String getBadge() {
	return badge;
}
public void setBadge(String badge) {
	this.badge = badge;
}
public String getIcon() {
	return icon;
}
public void setIcon(String icon) {
	this.icon = icon;
}
public PushData(String title, String message) {
	super();
	this.title = title;
	this.message = message;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}

}
