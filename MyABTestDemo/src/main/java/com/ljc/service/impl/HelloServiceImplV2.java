package com.ljc.service.impl;

import com.ljc.common.annotation.RoutingVersion;
import com.ljc.common.enums.RoutingVersionEnum;
import com.ljc.service.HelloService;
import org.springframework.stereotype.Service;

@Service
@RoutingVersion(version = RoutingVersionEnum.B)
public class HelloServiceImplV2 implements HelloService {
    @Override
    public String hello() {
        return "hello-v2";
    }
}
