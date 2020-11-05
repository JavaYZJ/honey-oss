package com.eboy.honey.oss.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @author yangzhijie
 * @date 2020/11/5 11:03
 */
public class ParserUtils {

    private final static String SPEL_SYMBOL = "#";

    public static String parseSpel(String input, ProceedingJoinPoint joinPoint) {
        if (isSpelExpression(input)) {
            ExpressionParser parser = new SpelExpressionParser();
            StandardEvaluationContext context = new StandardEvaluationContext();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
            String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
            Expression expression = parser.parseExpression(input);
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                context.setVariable(Objects.requireNonNull(paramNames)[i], args[i]);
            }
            return expression.getValue(context, String.class);
        }
        return input;
    }

    public static boolean isSpelExpression(String str) {
        Assert.notNull(str, "the str will be check is SPEL must not null");
        return str.contains(SPEL_SYMBOL);
    }
}
