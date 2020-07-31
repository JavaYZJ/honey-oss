/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eboy.honey.oss.server.dubbo.demo.consumer;

import com.eboy.honey.oss.server.dubbo.demo.api.User;
import com.eboy.honey.oss.server.dubbo.demo.api.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Dubbo Spring Cloud Consumer Bootstrap.
 */
@EnableScheduling
@Configuration
public class DubboSpringCloudConsumerRunners {

    @Reference
    private UserService userService;

    @Bean
    public ApplicationRunner userServiceRunner() {
        return arguments -> {

            User user = new User();
            user.setId(1L);
            user.setName("小马哥");
            user.setAge(33);

            // save User
            System.out.printf("UserService.save(%s) : %s\n", user,
                    userService.save(user));

            // find all Users
            System.out.printf("UserService.findAll() : %s\n", user,
                    userService.findAll());

            // remove User
            System.out.printf("UserService.remove(%d) : %s\n", user.getId(),
                    userService.remove(user.getId()));

        };
    }
}
