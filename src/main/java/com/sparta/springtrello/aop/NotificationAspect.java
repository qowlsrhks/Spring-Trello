package com.sparta.springtrello.aop;

import com.sparta.springtrello.domain.notification.service.NotificationService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NotificationAspect {
    private final NotificationService notificationService;

    public NotificationAspect(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @AfterReturning(pointcut = "execution(* com.sparta.springtrello..*.*(..)) && @annotation(org.springframework.web.bind.annotation.PostMapping)", returning = "result")
    public void sendNotification(JoinPoint joinPoint, Object result) {
        // 슬랙 알림 메시지 생성
        String message = "API 호출: " + joinPoint.getSignature().getName();
        notificationService.sendSlackNotification(message);
    }
}
