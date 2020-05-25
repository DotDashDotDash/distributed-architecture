package org.codwh.redisson.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class SpelUtils {

    /**
     * Spel解析器
     */
    private SpelExpressionParser parser = new SpelExpressionParser();

    /**
     * 用于获取方法参数自定义名字
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 解析上下文对象，解析的对应关系存储于此
     */
    private EvaluationContext context = new StandardEvaluationContext();

    /**
     * 用在缓存之上，用Spel表达式解析缓存名称
     *
     * @param spelStr
     * @param joinPoint
     * @return
     */
    public String parseSpelStr(String spelStr, ProceedingJoinPoint joinPoint){
        if(!spelStr.contains("#")){
            return spelStr; //it is not a spel string
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String[] parametersInMethod = nameDiscoverer.getParameterNames(method);
        Expression expression = parser.parseExpression(spelStr);
        Object[] args = joinPoint.getArgs();    //args matches parameter names
        for(int i = 0; i < args.length; i++){
            context.setVariable(parametersInMethod[i], args[i]);    //bind parameter names to arguments
        }
        //使用时可根据context计算出实际的参数值
        //@annotation(key = "#student.name")
        //returnType methodName(Student student)
        return expression.getValue(context).toString();
    }

    public List<String> parseSpelStr(String spelStr, ProceedingJoinPoint joinPoint, final String suffix){
        if(!spelStr.contains("#")){
            List<String> res = new ArrayList<String>(); //it is not a spel string
            res.add(spelStr);
            return res;
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] parameterArgs = joinPoint.getArgs();
        Expression expression = parser.parseExpression(spelStr);
        for(int i = 0; i < parameterArgs.length; i++){
            context.setVariable(parameterNames[i], parameterArgs[i]);
        }
        Object o = expression.getValue(context);    //parse from context
        List<String> res = new ArrayList<String>();
        if(o instanceof List){
            List l = (List) o;
            for(Object obj : l){
                res.add("redisson:lock:" + obj.toString() + suffix);
            }
        }else if(o.getClass().isArray()){
            Object[] objs = (Object[]) o;
            for(Object obj : objs){
                res.add("redisson:lock" + obj.toString() + suffix);
            }
        }
        return res;
    }
}
