package mp.jprime.security;

import mp.jprime.security.beans.AuthInfoBean;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthParamsTest {
  @Test
  void checkOktmoPrefixList() {
    AuthParams inst = AuthInfoBean.newBuilder()
        .oktmoList(
            List.of("00000000", "10000000","11000000","12110000", "13111111")
        )
        .build();
    assertThat(inst.getOktmoPrefixList()).containsAll(List.of("", "10","11","12110", "13111111"));
  }
}
