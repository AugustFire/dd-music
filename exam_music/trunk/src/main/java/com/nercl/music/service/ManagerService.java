package com.nercl.music.service;

import com.nercl.music.entity.user.Manager;

public interface ManagerService {

	Manager getByLogin(String login);

	Manager getSuperManager();

	boolean save(Manager manager);

}
