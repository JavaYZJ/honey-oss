package com.eboy.honey.oss.utils;

import com.eboy.honey.oss.api.entiy.Response;

/**
 * @author yangzhijie
 * @date 2020/11/13 16:50
 */
public class HoneyWarpUtils {

    public static Response<String> warpResponse(String fileKey) {
        Response<String> response = new Response<>();
        response.setFileKey(fileKey);
        return response;
    }
}
