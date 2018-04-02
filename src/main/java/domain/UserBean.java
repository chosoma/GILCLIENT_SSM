package domain;

/**
 * 用户实体类
 */
public class UserBean {

	private String username, password;
	private int authority;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAuthority() {
		return authority;
	}

	public void setAuthority(int authority) {
		this.authority = authority;
	}

	@Override
	public String toString() {
		return "UserBean{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", authority=" + authority +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserBean userBean = (UserBean) o;

		return username != null ? username.equals(userBean.username) : userBean.username == null;
	}

	@Override
	public int hashCode() {
		return username != null ? username.hashCode() : 0;
	}
}
