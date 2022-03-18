package com.git.blog.feign.al;

import com.alibaba.fastjson.JSONArray;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * nacos的一些api，只利用查询命名空间和微服务的
 * @author authorZhao
 * @since 2021-03-09
 */

@FeignClient(name ="nacos" ,url = "http://nacos.office.qunjielong.com/")
public interface NacosFeign {


    /**
     * 登录nacos
     * @param username 用户名
     * @param password 密码
     * @return 返回token
     */
    @PostMapping(value = "nacos/v1/auth/users/login", consumes = {"application/x-www-form-urlencoded"})
    String login(@RequestParam String username, @RequestParam String password);



    @GetMapping(value = "nacos/v1/cs/configs", consumes = {"application/x-www-form-urlencoded; charset=utf-8"})
    String showConfig(@RequestParam String dataId, @RequestParam String namespaceId, @RequestParam String tenant, @RequestParam String accessToken,
                      @RequestParam String group, @RequestParam String show);

    @PostMapping(value = "nacos/v1/auth/users", consumes = {"application/x-www-form-urlencoded; charset=utf-8"})
    String createUser(@RequestParam String username, @RequestParam String password, @RequestParam String accessToken);

    @PostMapping(value = "nacos/v1/auth/roles", consumes = {"application/x-www-form-urlencoded; charset=utf-8"})
    String createRole(@RequestParam String role, @RequestParam String username, @RequestParam String accessToken);

    @PostMapping(value = "nacos/v1/auth/permissions", consumes = {"application/x-www-form-urlencoded; charset=utf-8"})
    String createSource(@RequestParam String role, @RequestParam String resource, @RequestParam String action, @RequestParam String accessToken);

    @PostMapping(value = "nacos/v1/console/namespaces", consumes = {"application/x-www-form-urlencoded; charset=utf-8"})
    String createNamespace(@RequestParam String customNamespaceId, @RequestParam String namespaceName, @RequestParam String namespaceDesc,                           @RequestParam String namespaceId,@RequestParam String accessToken);

    @PostMapping(value = "nacos/v1/cs/configs", consumes = {"application/json; charset=utf-8"})
    String copeNamespace(@RequestParam Boolean clone, @RequestParam String policy, @RequestParam String tenant, @RequestParam String namespaceId, @RequestBody JSONArray list, @RequestParam String accessToken);


    @PostMapping(value = "nacos/v1/cs/configs", consumes = {"application/x-www-form-urlencoded; charset=utf-8"})
    Boolean createGroupText(@RequestParam String dataId, @RequestParam String group, @RequestParam String content,
                            @RequestParam(value = "config_tags") String configTags,@RequestParam String desc,
                            @RequestParam String type, @RequestParam String appName,@RequestParam String tenant,                         @RequestParam String namespaceId,@RequestParam String accessToken);


    /**
     * 获取所有的命名空间
     * @param accessToken
     * @return
     */
    @GetMapping(value = "nacos/v1/console/namespaces")
    Map<String,Object> getAllNamespace(@RequestParam("accessToken")String accessToken);


    /**
     * 根据命名空间获取微服务
     * @param accessToken
     * @param namespaceId
     * @return
     */
    @GetMapping(value = "nacos/v1/ns/catalog/services?hasIpCount=true&withInstances=false&pageNo=1&pageSize=200&serviceNameParam=&groupNameParam=")
    Map<String,Object> getAllServiceByNamespace(@RequestParam("accessToken")String accessToken,@RequestParam("namespaceId") String namespaceId);


    @GetMapping(value = "nacos/v1/ns/catalog/services")
    Map<String,Object> pageServiceByNamespace(@RequestParam("accessToken")String accessToken,
                                              @RequestParam("namespaceId") String namespaceId,
                                              @RequestParam("hasIpCount") Boolean hasIpCount,
                                              @RequestParam("withInstances") Boolean withInstances,
                                              @RequestParam("pageNo") Integer pageNo,
                                              @RequestParam("PageSize") Integer PageSize,
                                              @RequestParam("serviceNameParam") String serviceNameParam,
                                              @RequestParam("groupNameParam") String groupNameParam

    );

}
