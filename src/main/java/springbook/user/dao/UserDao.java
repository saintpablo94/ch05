package springbook.user.dao;

import java.util.Collection;
import java.util.List;

import springbook.user.domain.User;

public interface UserDao {
	void add(final User user);
	void deleteAll();
	User get(String id);
	int getCount();
	List<User> getAll();
	void update(User user);
}
