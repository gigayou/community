package com.giga.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

	@Before("execution(* com.giga.controller.*Controller.*(..))")
	public void beforeMethod() {
		LOGGER.info("before method:" );
	}

	@After("execution(* com.giga.controller.*.*(..))")
	public void afterMethod() {
		LOGGER.info("after method");
	}

}
