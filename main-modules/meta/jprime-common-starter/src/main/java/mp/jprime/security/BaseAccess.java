package mp.jprime.security;

import mp.jprime.security.annotations.JPAccess;
import mp.jprime.security.annotations.JPPackage;
import mp.jprime.security.annotations.JPPackages;
import mp.jprime.security.beans.JPAccessType;

import static mp.jprime.security.Role.ADMIN;
import static mp.jprime.security.Role.AUTH_ACCESS;

@JPPackages(
    {
        @JPPackage(
            code = BaseAccess.ONLY_READONLY,
            description = "Всем ролям только на чтение",
            name = "Всем ролям только на чтение",
            access = {
                @JPAccess(
                    type = JPAccessType.PERMIT,
                    role = AUTH_ACCESS,
                    read = true,
                    create = false,
                    update = false,
                    delete = false
                )
            }
        ),
        @JPPackage(
            code = BaseAccess.ONLY_ADMIN_ONLY_READONLY,
            description = "Только для роли ADMIN только на чтение",
            name = "Только для роли ADMIN только на чтение",
            access = {
                @JPAccess(
                    type = JPAccessType.PERMIT,
                    role = ADMIN,
                    read = true,
                    create = false,
                    update = false,
                    delete = false
                )
            }
        ),
        @JPPackage(
            code = BaseAccess.ONLY_CREATE,
            description = "Всем ролям только на чтение и создание",
            name = "Всем ролям только на чтение и создание",
            access = {
                @JPAccess(
                    type = JPAccessType.PERMIT,
                    role = AUTH_ACCESS,
                    read = true,
                    create = true,
                    update = false,
                    delete = false
                )
            }
        ),
        @JPPackage(
            code = BaseAccess.COMMON_READONLY,
            description = "Полный доступ для роли ADMIN, остальным на чтение",
            name = "Полный доступ роли ADMIN, остальным на чтение",
            access = {
                @JPAccess(
                    type = JPAccessType.PERMIT,
                    role = ADMIN,
                    read = true,
                    create = true,
                    update = true,
                    delete = true
                ),
                @JPAccess(
                    type = JPAccessType.PERMIT,
                    role = AUTH_ACCESS,
                    read = true,
                    create = false,
                    update = false,
                    delete = false
                ),
                @JPAccess(
                    type = JPAccessType.PROHIBITION,
                    role = "DISABLED",
                    read = true,
                    create = true,
                    update = true,
                    delete = true
                )
            }
        ),
        @JPPackage(
            code = BaseAccess.ADMIN_ACCESS,
            description = "Полный доступ для роли ADMIN",
            name = "Полный доступ для роли ADMIN",
            access = {
                @JPAccess(
                    type = JPAccessType.PERMIT,
                    role = ADMIN,
                    read = true,
                    create = true,
                    update = true,
                    delete = true
                )
            }
        ),
        @JPPackage(
            code = BaseAccess.COMMON_ACCESS,
            description = "Полный доступ для любой роли",
            name = "Полный доступ для любой роли",
            access = {
                @JPAccess(
                    type = JPAccessType.PERMIT,
                    role = AUTH_ACCESS,
                    read = true,
                    create = true,
                    update = true,
                    delete = true
                )
            }
        ),
        @JPPackage(
            code = BaseAccess.COMMON_DENIED,
            description = "Полный запрет для любой роли",
            name = "Полный запрет для любой роли",
            access = {
                @JPAccess(
                    type = JPAccessType.PROHIBITION,
                    role = AUTH_ACCESS,
                    read = true,
                    create = true,
                    update = true,
                    delete = true
                )
            }
        ),
        @JPPackage(
            code = BaseAccess.DELETE_DENIED,
            description = "Запрет на удаление для любой роли",
            name = "Запрет на удаление для любой роли",
            access = {
                @JPAccess(
                    type = JPAccessType.PERMIT,
                    role = AUTH_ACCESS,
                    read = true,
                    create = true,
                    update = true,
                    delete = false
                ),
                @JPAccess(
                    type = JPAccessType.PROHIBITION,
                    role = AUTH_ACCESS,
                    read = false,
                    create = false,
                    update = false,
                    delete = true
                )
            }
        )
    }
)
/**
 * Типовые настройки
 */
public class BaseAccess implements JPSecuritySettings {
  /**
   * Всем ролям только на чтение
   */
  public final static String ONLY_READONLY = "onlyReadonly";
  /**
   * Только для роли ADMIN только на чтение
   */
  public final static String ONLY_ADMIN_ONLY_READONLY = "onlyAdminOnlyReadonly";
  /**
   * Всем ролям только на чтение + создание
   */
  public final static String ONLY_CREATE = "onlyCreate";
  /**
   * Полный доступ для роли ADMIN, остальным на чтение
   */
  public final static String COMMON_READONLY = "commonReadonly";
  /**
   * Полный доступ для роли ADMIN
   */
  public final static String ADMIN_ACCESS = "adminAccess";
  /**
   * Полный доступ для любой роли
   */
  public final static String COMMON_ACCESS = "commonAccess";
  /**
   * Полный запрет для любой роли
   */
  public final static String COMMON_DENIED = "commonDenied";
  /**
   * Запрет на удаление для любой роли
   */
  public final static String DELETE_DENIED = "deleteDenied";
}
