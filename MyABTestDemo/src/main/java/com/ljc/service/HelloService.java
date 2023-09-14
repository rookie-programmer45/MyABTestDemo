package com.ljc.service;

import com.ljc.common.annotation.VersionSwitch;
import com.ljc.common.enums.RoutingVersionEnum;

public interface HelloService {

    @VersionSwitch
    String hello();
}
