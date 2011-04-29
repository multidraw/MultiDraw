package utils;

public class UserName {
	private String userName;
	private boolean isDrawer;
	private boolean isSelf;
	
	/**
	 * Utility which facilitates populating the session tab of the GuiView.
	 * Maintains the integrity of usernames while allowing the session list
	 * to display other user attributes such as drawing control and identity.
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
