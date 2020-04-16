package ua.kpi.tef.zu.gp3servlet.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Anton Domin on 2020-04-16
 */
@Getter
@Setter
@Builder
@ToString
public class User {
	private int id;
	private String login;
	private String name;
	private RoleType role;
	private String email;
	private String phone;
	private String password;
}
