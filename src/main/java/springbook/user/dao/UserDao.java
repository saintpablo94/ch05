package springbook.user.dao;

import java.util.Collection;

import springbook.user.domain.User;

public interface UserDao {
	void add(final User user);
	void deleteAll();
	User get(String id);
	int getCount();
	Collection<User> getAll();
}
