package com.nercl.music.api;

import java.util.Observable;
import java.util.Observer;

public class HearbeatObserver implements Observer {

	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println("-------------");

	}

}
