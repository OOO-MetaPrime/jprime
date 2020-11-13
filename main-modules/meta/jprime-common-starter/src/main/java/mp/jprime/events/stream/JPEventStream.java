package mp.jprime.events.stream;


import mp.jprime.security.AuthInfo;
import reactor.core.publisher.Flux;

/**
 * Передача событий в непрерывном stream
 */
public interface JPEventStream {
  /**
   * Возвращает stream событий
   *
   * @param authInfo AuthInfo
   * @return Поток события
   */
  Flux<JPEventInfo> stream(AuthInfo authInfo);
}
