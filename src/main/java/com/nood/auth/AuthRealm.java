package com.nood.auth;

import com.nood.model.Permission;
import com.nood.model.Role;
import com.nood.model.User;
import com.nood.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    // Authorization 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        List<String> permissionList = new ArrayList<>();
        List<String> roleNameList = new ArrayList<>();

//        User user = (User) principalCollection.fromRealm(this.getClass().getName()).iterator().next();
        User user = (User) principalCollection.getPrimaryPrincipal(); // 获取已经认证的用户
        Set<Role> roles = user.getRoles();
        if (CollectionUtils.isNotEmpty(roles)) {
            for (Role role : roles) {
//                System.out.println("role:"+role.getRoleName());
                roleNameList.add(role.getRoleName());
                Set<Permission> permissions = role.getPermissions();
                if (CollectionUtils.isNotEmpty(permissions)) {
                    for (Permission permission : permissions) {
//                        System.out.println("permission:"+permission.getPermissionName());
                        permissionList.add(permission.getPermissionName());
                    }
                }
            }
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissionList);
        info.addRoles(roleNameList); // optional 在进行授权时也拿到了角色
        return info;
    }

    // Authentication login
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String username = usernamePasswordToken.getUsername();
        User user = userService.findByUserName(username);
        return new SimpleAuthenticationInfo(user, user.getPassword(), user.getClass().getName());
    }
}
