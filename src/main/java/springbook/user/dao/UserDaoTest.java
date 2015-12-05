package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.awt.List;
import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
/*@DirtiesContext*/
public class UserDaoTest {
	@Autowired ApplicationContext context;	
	@Autowired UserDao dao; 
	@Autowired DataSource dataSource;
	
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() throws InterruptedException {
		this.dao = this.context.getBean("userDao", UserDao.class);
		
//		TimeUnit.SECONDS.sleep(3);
		System.out.println(System.currentTimeMillis());
		this.user1 = new User("gyumee", "홍길동", "springno1",Level.BASIC,1,0);
		this.user2 = new User("leegw700", "강감찬", "springno2",Level.SILVER,55,10);
		this.user3 = new User("bumjin", "이순신", "springno3",Level.GOLD,100,40);
		System.out.println(System.currentTimeMillis());
	}
	
	@Test 
	public void andAndGet() throws SQLException {		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		User userget1 = dao.get(user1.getId());
		checkSameUser(userget1, user1);
				
		User userget2 = dao.get(user2.getId());
		checkSameUser(userget2, user2);
	}

	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unknown_id");
	}
	
	@Test public void getAll() throws SQLException {
		dao.deleteAll();
		
		Collection<User> users0 = dao.getAll();
		assertThat(users0.size(), is(0));
		
		dao.add(user1);
		Collection<User> users1 = dao.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1, (User)users1.toArray()[0]);
		
		dao.add(user2);
		Collection<User> users2 = dao.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user1, (User)users2.toArray().clone()[0]);
		checkSameUser(user2, (User)users2.toArray().clone()[1]);
		
		dao.add(user3);
		Collection<User> users3 = dao.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user3, (User)users3.toArray()[0]);
		checkSameUser(user1, (User)users3.toArray()[1]);
		checkSameUser(user2, (User)users3.toArray()[2]);
		
	}
	
	@Test
	public void count() throws SQLException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
				
		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}
	
	@Test(expected=DuplicateKeyException.class) 
	public void duplciatekey(){
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user1);
	}
	
	@Test
	public void sqlExceptionTranslate(){
		dao.deleteAll();
		try {
			dao.add(user1);
			dao.add(user1);
		} catch (DuplicateKeyException ex) {
			SQLException sqlEx = (SQLException) ex.getRootCause();
			SQLExceptionTranslator set = 
				new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
			System.out.println(set.translate(null, null, sqlEx));
			System.out.println(DuplicateKeyException.class);
			//assertThat(set.translate(null, null, sqlEx), is(DuplicateKeyException.class));
		}
	}
	
	@Test
	public void update(){
		dao.deleteAll();
		dao.add(user1);
		dao.add(user2);
		
		user1.setName("test");
		user1.setPassword("password");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommand(999);
		dao.update(user1);
		
		User userupdate = dao.get(user1.getId());
		checkSameUser(user1, userupdate);
		User user2same = dao.get(user2.getId());
		checkSameUser(user2, user2same);
		
	}
	
	private void checkSameUser(User user1, User user2) {
		System.out.println(user1.getId()+":"+user2.getId());
		System.out.println(user1.getName()+":"+user2.getName());
		System.out.println(user1.getPassword()+":"+user2.getPassword());
		System.out.println("-------------------");
		
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommand(), is(user2.getRecommand()));
	}
}
