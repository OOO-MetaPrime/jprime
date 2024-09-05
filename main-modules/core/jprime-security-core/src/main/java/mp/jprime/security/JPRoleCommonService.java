package mp.jprime.security;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Сервис работы с ролями. Базовая реализация
 */
@Service
public final class JPRoleCommonService implements JPRoleService {
  private static final Collection<String> DEFAULT_ALLOW_ROLES = List.of(Role.AUTH_ACCESS);

  /**
   * Возвращает список ролей, для которых разрешен доступ к системе
   *
   * @return список ролей
   */
  public Collection<String> getAllowRoles() {
    return DEFAULT_ALLOW_ROLES;
  }
}
