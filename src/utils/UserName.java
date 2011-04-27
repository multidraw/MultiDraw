package utils;

public class UserName {
	private String userName;
	private boolean isDrawer;
	private boolean isSelf;
	
	/**
	 * 
	 * @param userName the username string
	 * @param isDrawer determines whether they are the drawer or not
	 * @param isSelf determines whether the member in the session window is the current user
	 */
	public UserName(String userName, boolean isDrawer, boolean isSelf) {
		this.userName = userName;
		this.isDrawer = isDrawer;
		this.isSelf = isSelf;
	}
	
	
	public String toString() {
		StringBuilder builder = new StringBuilder(userName);
		if(isSelf)
			builder.append(" ( You )");
		if(isDrawer)
			builder.append(" << Drawing Control");
			
		
		return builder.toString();
	}
	
	public String getUserName() {
		return userName;
	}
}
