package com.wan.nss.biz.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

@Service
@Aspect
public class AfterReturningAdvice {
	
	@AfterReturning(pointcut="PointcutCommon.aPointcut()", returning="obj")
	public void printLogARA(JoinPoint jp, Object obj) {
		
		// 클래스네임 가져오기
		String className = jp.getTarget().toString();
		className = className.substring(className.indexOf(".", 17) + 1, className.lastIndexOf("Impl"));
				
		// 메서드네임 가져오기
		String methodName = jp.getSignature().getName() + "()";
		
		System.out.println();
		System.out.println("[Service 로그]");
		System.out.println("● 수행 메서드: " + className + "." + methodName);
//		System.out.println("● 수행 인  자: " +jp.getArgs()[0]);
		// 결과값이 list 종류가 아니면 나오게
//		if(!(obj instanceof List)) {
//			System.out.println("● 수행 결과값: "+obj);
//		}
		System.out.println();
		
	}
	
}