package com.nercl.music.cloud.entity.behavior;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("login")
public class LoginBehavior extends Behavior {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7500366641123469308L;

	private String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
