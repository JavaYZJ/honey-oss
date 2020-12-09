package red.honey.oss.job.application.utils;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import red.honey.oss.job.application.config.HoneyOssProperties;
import red.honey.oss.job.application.vo.AbstractRequestParam;

import javax.annotation.PostConstruct;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author yangzhijie
 * @date 2020/9/30 9:56
 */
@Component
public class SignUtil {


    private final static String Separator = "&";
    private static HoneyOssProperties properties;
    @Autowired
    private HoneyOssProperties honeyOssProperties;

    /**
     * 获取腾讯AI开发平台的sign签名
     * 将<key, value>请求参数对按key进行字典升序排序，得到有序的参数对列表N
     * 将列表N中的参数对按URL键值对的格式拼接成字符串，得到字符串T（如：key1=value1&key2=value2），URL键值拼接过程value部分需要URL编码，URL编码算法用大写字母，例如%E8，而不是小写%e8
     * 将应用密钥以app_key为键名，组成URL键值拼接到字符串T末尾，得到字符串S（如：key1=value1&key2=value2&app_key=密钥)
     * 对字符串S进行MD5运算，将得到的MD5值所有字符转换成大写，得到接口请求签名
     */
    @SneakyThrows
    public static String tencentAISign(AbstractRequestParam param) {
        Map<String, Object> map = CommonUtil.objectToMap(param);
        Map<String, Object> sortMap = sortMapByKey(map);
        String urlParams = getUrlParamsByMap(sortMap);
        return CommonUtil.md5(urlParams + Separator + "app_key" + "=" + properties.getAppKey()).toUpperCase();
    }

    /**
     * 根据map的key进行字典升序排序
     */
    protected static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        Map<String, Object> treeMap = new TreeMap<>(map);
        List<Map.Entry<String, Object>> list = new ArrayList<>(treeMap.entrySet());
        list.sort(Map.Entry.comparingByKey());
        return treeMap;
    }

    /**
     * 将map转换成url参数
     */
    protected static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                sb.append(Separator);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = sb.toString();
        if (s.endsWith(Separator)) {
            s = StringUtils.substringBeforeLast(s, Separator);
        }
        return s;
    }

    @PostConstruct
    public void init() {
        properties = honeyOssProperties;
    }
}
