package ua.kpi.tef.zu.gp3servlet.entity;

/**
 * Created by Anton Domin on 2020-04-16
 */
public class User {
	private int id;
	private String login;
	private String name;
	private RoleType role;
	private String email;
	private String phone;
	private String password;
	//boilerplate below


	public User() {
	}

	public User(int id, String login, String name, RoleType role, String email, String phone, String password) {
		this.id = id;
		this.login = login;
		this.name = name;
		this.role = role;
		this.email = email;
		this.phone = phone;
		this.password = password;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public RoleType getRole() {
		return role;
	}
	public void setRole(RoleType role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", login='" + login + '\'' +
				", name='" + name + '\'' +
				", role=" + role +
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
