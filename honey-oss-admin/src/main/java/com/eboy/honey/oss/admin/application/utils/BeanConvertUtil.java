package com.eboy.honey.oss.admin.application.utils;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/7/31 15:27
 */
@Slf4j
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

    public static <S, T> PageInfo<T> pageInfoConvert(PageInfo<S> source, Class<T> clazz) {
        PageInfo<T> target = new PageInfo<>();
        BeanUtils.copyProperties(source, target);
        target.setList(BeanConvertUtil.convertByList(source.getList(), clazz));
        return target;
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
