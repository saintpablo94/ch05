package springbook.user.domain;

import lombok.Data;

@Data
public class User {
	
	private static final int BASIC = 1;
	private static final int SILVER = 2;
	private static final int GOLD = 3;
	
	String id;
	String name;
	String password;
	Level level;
	int login;
	int recommand;
	
	public User() {
	}
	
	public User(String id, String name, String password, Level level, int login, int recommand) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.level = level;
		this.login = login;
		this.recommand = recommand;
	}
}
