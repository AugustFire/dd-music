package com.nercl.music.listener;

import static com.nercl.music.util.Encryptor.encrypte2;
import static com.nercl.music.util.Encryptor.initSalt;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.beans.factory.annotation.Autowired;

import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Manager;
import com.nercl.music.entity.user.Person;
import com.nercl.music.entity.user.Person.Gender;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.LoginService;
import com.nercl.music.service.ManagerService;
import com.nercl.music.service.PersonService;

@WebListener
public class ManagerInitiallizer implements ServletContextListener {

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
			person.setGender(Gender.MAN);
			this.personService.save(person);

			Login lgin = new Login();
			lgin.setLogin(ADMIN_LOGIN);
			String salt = initSalt(16);
			lgin.setSalt(salt);
			lgin.setEncryptedPassword(encrypte2(ADMIN_PASSWORD, salt));
			lgin.setPersonId(person.getId());
			lgin.setRole(Role.MANAGER);
			lgin.setCreateAt(System.currentTimeMillis());
			this.loginService.save(lgin);

			Manager manager = new Manager();
			manager.setIsSuper(true);
			manager.setCode("admin");
			manager.setPersonId(person.getId());

			this.managerService.save(manager);
		}
	}

}
