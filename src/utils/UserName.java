package utils;

public class UserName {
	private String userName;
	private boolean isDrawer;
	private boolean isSelf;
	
	public UserName(String user, boolean isDrawer, boolean isSelf) {
		this.userName = user;
		this.isDrawer = isDrawer;
		this.isSelf = isSelf;
	}
	
	
	public String toString() {
		StringBuilder builder = new StringBuilder(userName);
		if(isDrawer)
			builder.append(" ( You )");
		if(isSelf)
			builder.append(" << Drawing Control");
		
		return builder.toString();
	}
	
	public String getUserName() {
		return userName;
	}
}
