package com.wan.nss.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

@Service
@Aspect
public class LogAdvice { // Advice : 횡단관심(공통로직)	
	@Before("PointcutCommon.aPointcut()")
	public void controllerPrintLog(JoinPoint jp) {
		System.out.println(jp.getSignature().getName()+"() 진입 예정");
	}
}