package com.example.OhBike.util;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.UUID;

public class AuthUtil {

//     SPRINT 2: Metodo temporal
//     SPRINT 3: a actualizar para extraer el ID del token JWT real.

    public static UUID getCurrentUserId() {
        return UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    }

    public static boolean currentUserHasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }
}



//public class AuthUtil {
//
//    public static UUID getCurrentUserId() {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return UUID.fromString(authentication.getName());
//    }
//}