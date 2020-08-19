package mp.jprime.security;

import java.util.Collection;

/**
 * Сервис работы с ролями
 */
public interface JPRoleService {
  /**
   * Возвращает список ролей, для которых разрешен доступ к системе
   *
   * @return список ролей, для которых разрешен доступ к системе
   */
  Collection<String> getAllowRoles();
}
