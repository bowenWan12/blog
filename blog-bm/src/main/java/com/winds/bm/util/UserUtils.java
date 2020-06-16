package com.winds.bm.util;

import com.winds.bm.entity.User;
import com.winds.common.constant.Base;
import org.apache.shiro.SecurityUtils;
public class UserUtils {

    public static User getCurrentUser() {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute(Base.CURRENT_USER);
        return user;
    }
}
