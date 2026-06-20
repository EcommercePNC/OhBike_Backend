package com.example.OhBike.utils;

import java.util.UUID;

public class AuthUtil {

//     SPRINT 2: Metodo temporal
//     SPRINT 3: a actualizar para extraer el ID del token JWT real.

    public static UUID getCurrentUserId() {
        return UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    }
}

//public class AuthUtil {
//
//    public static UUID getCurrentUserId() {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return UUID.fromString(authentication.getName());
//    }
//}