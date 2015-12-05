package springbook.user.service;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {
	@Autowired
	UserService userService;
	@Autowired
	UserDao userDao;

	List<User> users;

	@Before
	public void setUp(){
		users = Arrays.asList(
				new User("test01","홍길동01","p1",Level.BASIC,49,0),
				new User("test02","홍길동02","p2",Level.BASIC,50,0),
				new User("test03","홍길동03","p3",Level.SILVER,60,29),
				new User("test04","홍길동04","p4",Level.SILVER,60,30),
				new User("test05","홍길동05","p5",Level.GOLD,100,100)
				);
	}
	
	@Test
	public void bean() {
		assertThat(this.userService, is(notNullValue()));
	}
	
	@Test
	public void upgradeLevels(){
		userDao.deleteAll();
		for (User user : users) {
			userDao.add(user);
		}
		
		userService.upgradeLevels();
		checkLevel(users.get(0), Level.BASIC);
		checkLevel(users.get(1), Level.SILVER);
		checkLevel(users.get(2), Level.SILVER);
		checkLevel(users.get(3), Level.GOLD);
		checkLevel(users.get(4), Level.GOLD);
	}
	
	private void checkLevel(User user, Level expectedLevel) {
		User userUpgrade = userDao.get(user.getId());
		assertThat(userUpgrade.getLevel(), is(expectedLevel));
	}
}
