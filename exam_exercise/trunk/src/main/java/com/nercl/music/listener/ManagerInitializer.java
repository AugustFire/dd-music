package com.nercl.music.listener;

import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Manager;
import com.nercl.music.entity.user.Person;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.LoginService;
import com.nercl.music.service.ManagerService;
import com.nercl.music.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import static com.nercl.music.util.Encryptor.encrypte2;
import static com.nercl.music.util.Encryptor.initSalt;

@WebListener
public class ManagerInitializer implements ServletContextListener {
	@Autowired
	private LoginService loginService;

	@Autowired
	private ManagerService managerService;

	@Autowired
	private PersonService personService;

	private static final String ADMIN_LOGIN = "admin";

	private static final String ADMIN_PASSWORD = "123456";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		this.initManager();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	private void initManager() {
		Manager superManager = this.managerService.getSuperManager();
		if (null == superManager) {
			Person person = new Person();
			person.setName("admin");
			person.setEmail("107888888@qq.com");
			person.setPhone("18602758888");
			person.setGender(Person.Gender.MAN);
			this.personService.save(person);

			Login login = new Login();
			login.setLogin(ADMIN_LOGIN);
			String salt = initSalt(16);
			login.setSalt(salt);
			login.setEncryptedPassword(encrypte2(ADMIN_PASSWORD, salt));
			login.setPersonId(person.getId());
			login.setRole(Role.MANAGER);
			login.setCreateAt(System.currentTimeMillis());
			this.loginService.save(login);

			Manager manager = new Manager();
			manager.setIsSuper(true);
			manager.setCode("admin");
			manager.setPersonId(person.getId());

			this.managerService.save(manager);
		}
	}
}
