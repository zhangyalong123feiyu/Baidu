package com.bibi.Baidu.Bean;

import java.io.Serializable;

/**
 * Created by bibinet on 2016/11/3.
 */
public class LoginResult extends Result implements Serializable {
    public UserInfo userinfo;

    public class UserInfo {
        public String uid;
        public String cert;
        public String token;
        public String account;

        @Override
        public String toString() {
            return "UserInfo [uid=" + uid + ", cert=" + cert + ", token=" + token + ", account=" + account + "]";
        }

    }

    @Override
    public String toString() {
        return "LoginResult [userinfo=" + userinfo + ", getFlag()=" + getFlag() + ", getMsg()=" + getMsg()
                + ", getError()=" + getError() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
                + ", toString()=" + super.toString() + "]";
    }
}
