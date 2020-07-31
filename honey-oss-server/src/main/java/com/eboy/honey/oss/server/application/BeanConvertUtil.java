package com.eboy.honey.oss.server.application;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/7/31 15:27
 */
public class BeanConvertUtil {

    /**
     * bean 的dto vo po 转换器
     *
     * @param source 源
     * @param clazz  目标class
     */
    public static <S, T> T convert(S source, Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            BeanUtils.copyProperties(source, instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("bean convert fail");
        }

    }

    /**
     * list bean 的dto vo po 转换器
     *
     * @param source 源
     * @param clazz  目标class
     */
    public static <T, S> List<T> convertByList(List<S> source, Class<T> clazz) {
        List<T> ts = new ArrayList<>();
        for (S s : source) {
            ts.add(convert(s, clazz));
        }
        return ts;
    }
}
