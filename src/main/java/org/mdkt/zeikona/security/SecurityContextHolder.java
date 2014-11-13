package org.mdkt.zeikona.security;

import org.mdkt.zeikona.model.entity.ZUser;

/**
 * Created by trung on 13/11/14.
 */
public class SecurityContextHolder {
    private static ThreadLocal<ZUser> securityContext = new ThreadLocal<>();

    public static void setZUser(ZUser user) {
        securityContext.set(user);
    }

    public static ZUser getZUser() {
        return securityContext.get();
    }

    public static void clearContext() {
        securityContext.remove();
    }
}
