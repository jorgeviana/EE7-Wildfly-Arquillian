package io.github.jorgeviana.services;

import javax.ejb.Stateless;
import java.time.LocalDateTime;

@Stateless
public class ServiceBean {
    public String sayHello() {
        return "Hello from Service at " + LocalDateTime.now();
    }
}
