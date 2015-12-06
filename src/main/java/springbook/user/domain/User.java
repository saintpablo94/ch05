package springbook.user.domain;

import lombok.Data;

@Data
public class User {
	
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
	
	public void upgradeLevel() {
		Level nextLevel = this.level.nextLevel();
		if(nextLevel == null){
			throw new IllegalStateException(this.level+ " 은 업그레디드 불가합니다.");
		}else {
			this.level = nextLevel;
		}
	}
}
