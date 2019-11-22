package com.hotjoe.beans;

import javax.ejb.Stateless;
import java.time.LocalDateTime;

@Stateless
public class HelloBean {

	public String sayHello() {
		return "Hello from Bean at " + LocalDateTime.now();
	}
}