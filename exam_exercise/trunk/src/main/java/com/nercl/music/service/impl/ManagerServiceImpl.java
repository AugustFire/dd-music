package com.nercl.music.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.ManagerDao;
import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Manager;
import com.nercl.music.entity.user.Person;
import com.nercl.music.service.LoginService;
import com.nercl.music.service.ManagerService;

@Service
@Transactional
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private LoginService loginService;

	@Autowired
	private ManagerDao managerDao;

	@Override
	public Manager getByLogin(String login) {
		Login lgin = this.loginService.getByLogin(login);
		if (null != lgin) {
			Person person = lgin.getPerson();
			if (null != person) {
				Manager manager = this.managerDao.getByPerson(person.getId());
				return manager;
			}
		}
		return null;
	}

	@Override
	public Manager getSuperManager() {
		return this.managerDao.getSuperManager();
	}

	@Override
	public boolean save(Manager manager) {
		this.managerDao.save(manager);
		return true;
	}

}
