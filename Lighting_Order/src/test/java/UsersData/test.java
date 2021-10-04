package UsersData;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes=UsersController.class)
class test {

	UsersController c;
	
	@BeforeEach
	void setUp() throws Exception {
		c = new UsersController();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testLogin() {
		List<String> roles = c.login("2");
		c.login("2");
		assertEquals(c.users.size(), 1);
		assertEquals(roles.get(0), "Cucina");
	}
	
	@Test
	void testGetUserById() {
		c.login("3");
		assertEquals(c.users.size(), 1);
		Optional<User> u = c.getUserById("3");
		assertTrue(u.isPresent());
		assertEquals(u.get().getName(), "Nicola");
		assertEquals(u.get().getRoles().get(0), "Forno");
	}
	
	@Test
	void testCheckRole() {
		c.login("1");
		assertTrue(c.checkRole("1", "Accoglienza"));
	}
	
	@Test
	void testUser() {
		c.login("1");
		assertTrue(c.checkUser("1"));
		assertFalse(c.checkUser("2"));
	}
	
	@Test
	void testGetUserByIdJSON() {
		c.login("1");
		Optional<User> u = c.getUserById("1");
		assertEquals(u.get().getJSONReppresentation(), c.getUserByIdJSON("1"));
	}
	
	@Test
	void testSetUserByIdJSON() {
		c.login("1");
		Optional<User> u = c.getUserById("1");
		u.get().setId("5");
		String user_json = u.get().getJSONReppresentation();
		c.setUserByJSON(user_json);
		Optional<User> user5= c.getUserById("5");
		assertTrue(user5.isPresent());
		assertTrue(user5.get().getId().equals("5"));
	}
	
	@Test
	void testLoginAll() {
		c.loginAll();
		assertTrue(c.users.size()==4);
		Optional<User> u = c.getUserById("4");
		assertEquals(u.get().getName(), "Antonio");
		assertEquals(u.get().getSurname(), "Emmanuele");
	}
	
	

}
