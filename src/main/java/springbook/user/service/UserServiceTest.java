package springbook.user.service;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {
	@Autowired
	UserService userService;
	@Autowired
	UserDao userDao;
	@Autowired
	DataSource dataSource;

	List<User> users;

	@Before
	public void setUp(){
		users = Arrays.asList(
				new User("test01","홍길동01","p1",Level.BASIC,MIN_LOGCOUNT_FOR_SILVER-1,0),
				new User("test02","홍길동02","p2",Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0),
				new User("test03","홍길동03","p3",Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD-1),
				new User("test04","홍길동04","p4",Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD),
				new User("test05","홍길동05","p5",Level.GOLD,100,Integer.MAX_VALUE)
				);
	}
	
	@Test
	public void bean() {
		assertThat(this.userService, is(notNullValue()));
	}
	
	@Test
	public void upgradeLevels() throws Exception{
		userDao.deleteAll();
		for (User user : users) {
			userDao.add(user);
		}
		
		userService.upgradeLevels();
		
		checkLevel(users.get(0), false);
		checkLevel(users.get(1), true);
		checkLevel(users.get(2), false);
		checkLevel(users.get(3), true);
		checkLevel(users.get(4), false);
	}
	
	@Test
	public void add(){
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelReadUser = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelReadUser.getLevel(), is(Level.BASIC));
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception{
		UserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao);
		testUserService.setDataSource(this.dataSource);
		
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
			
		try {
			testUserService.upgradeLevels();
			fail("TestUserServciceException expected");
		} catch (TestUserServiceException e) {
		}
		
		checkLevelUpgraded(users.get(1),false);
	}
	
	private void checkLevelUpgraded(User user, boolean upgade) {
		User userUpgrade = userDao.get(user.getId());
		if(upgade){
			assertThat(userUpgrade.getLevel(), is(user.getLevel().nextLevel()));
		}else {
			assertThat(userUpgrade.getLevel(), is(user.getLevel()));			
		}
	}

	private void checkLevel(User user, boolean upgrade) {
		User userUpgrade = userDao.get(user.getId());
		if(upgrade){
			assertThat(userUpgrade.getLevel(), is(user.getLevel().nextLevel()));
		}else{
			assertThat(userUpgrade.getLevel(), is(user.getLevel()));
		}
		
	}
}
