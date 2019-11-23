package io.github.jorgeviana.beans;

import javax.ejb.Stateless;
import java.time.LocalDateTime;

@Stateless
public class EjbBean {

	public String sayHello() {
		return "Hello from Ejb at " + LocalDateTime.now();
	}
}