package com.example.mybookshopapp.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
public class ChangeBookStatusLoggerAspect {

    private final Logger logger = LoggerFactory.getLogger(ChangeBookStatusLoggerAspect.class);

    @Pointcut(value = "@annotation(com.example.mybookshopapp.annotations.BookStatusChangeable)")
    public void logBookStatusChangePointcut() {
    }

    @AfterReturning(pointcut = "args(slug, cartContents,response,model) && logBookStatusChangePointcut()", argNames = "joinPoint,slug,cartContents,response,model")
    public void changeBookStatusArgsCatcherAdvice(JoinPoint joinPoint,
                                                  String slug,
                                                  String cartContents,
                                                  HttpServletResponse response,
                                                  Model model) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        if (className.contains("CartPageController")) {
            if (methodName.equals("handleChangeBookStatus")) {
                logger.info("Книга с идентификатором {} была добавлена в корзину", slug);
            } else if (methodName.equals("handleRemoveBookFromCartRequest")) {
                logger.info("Книга с идентификатором {} была удалена из корзины", slug);
            }
        } else if (className.contains("PostponedPageController")) {
            if (methodName.equals("handleChangeBookStatus")) {
                logger.info("Книга с идентификатором {} была добавлена в отложенное", slug);
            } else if (methodName.equals("handleRemoveBookFromPostponedRequest")) {
                logger.info("Книга с идентификатором {} была удалена из отложенного", slug);
            }
        }
    }
}
