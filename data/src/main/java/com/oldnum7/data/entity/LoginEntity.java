package com.oldnum7.data.entity;

/**
 * author : denglin
 * time   : 2017/04/05/17:44
 * desc   :
 * version: 1.0
 */


public class LoginEntity {

    private String mobile;
    private String roleId;
    private String roleName;
    private String storeName;
    private String token;
    private String userId;
    private String userName;

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "LoginEntity{" +
                "mobile='" + mobile + '\'' +
                ", roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", storeName='" + storeName + '\'' +
                ", token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
