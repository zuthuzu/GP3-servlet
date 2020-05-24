package ua.kpi.tef.zu.gp3servlet.entity;

/**
 * Created by Anton Domin on 2020-04-16
 */
public class User {
	private long id;
	private String login;
	private String name;
	private RoleType role;
	private String email;
	private String phone;
	private String password;
	//only boilerplate below

	public User() {
	}

	public long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public static class Builder {
		private long id;
		private String login;
		private String name;
		private RoleType role;
		private String email;
		private String phone;
		private String password;

		public Builder id(long i) {
			this.id = i;
			return this;
		}

		public Builder login(String l) {
			this.login = l;
			return this;
		}

		public Builder name(String s) {
			this.name = s;
			return this;
		}

		public Builder role(RoleType r) {
			this.role = r;
			return this;
		}

		public Builder email(String s) {
			this.email = s;
			return this;
		}

		public Builder phone(String s) {
			this.phone = s;
			return this;
		}

		public Builder password(String s) {
			this.password = s;
			return this;
		}

		public User build() {
			return new User(this);
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public User(Builder builder) {
		this.id = builder.id;
		this.login = builder.login;
		this.name = builder.name;
		this.role = builder.role;
		this.email = builder.email;
		this.phone = builder.phone;
		this.password = builder.password;
	}
}