package mp.jprime.security;

import mp.jprime.security.annotations.JPAccess;
import mp.jprime.security.annotations.JPPackage;
import mp.jprime.security.annotations.JPPackages;
import mp.jprime.security.beans.JPAccessType;
import mp.jprime.security.security.Role;

@JPPackages(
    {
        @JPPackage(
            code = AuthAccess.ONLY_ALL_AUTH_ADMIN,
            description = "Доступ всем auth-администраторам",
            name = "Доступ всем auth-администраторам",
            access = {
                @JPAccess(
                    type = JPAccessType.PERMIT,
                    role = Role.AUTH_ADMIN,
                    read = true,
                    create = true,
                    update = true,
                    delete = true
                ),
                @JPAccess(
                    type = JPAccessType.PERMIT,
                    role = Role.AUTH_ORG_ADMIN,
                    read = true,
                    create = true,
                    update = true,
                    delete = true
                ),
                @JPAccess(
                    type = JPAccessType.PERMIT,
                    role = Role.AUTH_SEPDEP_ADMIN,
                    read = true,
                    create = true,
                    update = true,
                    delete = true
                )
            }
        ),
        @JPPackage(
            code = AuthAccess.ONLY_ALL_AUTH_ADMIN_READONLY,
            description = "Доступ всем auth-администраторам только на чтение",
            name = "Доступ всем auth-администраторам только на чтение",
            access = {
                @JPAccess(
                    type = JPAccessType.PERMIT,
                    role = Role.AUTH_ADMIN,
                    read = true,
                    create = false,
                    update = false,
                    delete = false
                ),
                @JPAccess(
                    type = JPAccessType.PERMIT,
                    role = Role.AUTH_ORG_ADMIN,
                    read = true,
                    create = false,
                    update = false,
                    delete = false
                ),
                @JPAccess(
                    type = JPAccessType.PERMIT,
                    role = Role.AUTH_SEPDEP_ADMIN,
                    read = true,
                    create = false,
                    update = false,
                    delete = false
                )
            }
        )
    }
)
public class AuthAccess implements JPSecuritySettings {
  /**
   * Доступ всем auth-администраторам
   */
  public final static String ONLY_ALL_AUTH_ADMIN = "onlyAllAuthAdmin";
  /**
   * Доступ всем auth-администраторам только на чтение
   */
  public final static String ONLY_ALL_AUTH_ADMIN_READONLY = "onlyAllAuthAdminReadonly";
}
