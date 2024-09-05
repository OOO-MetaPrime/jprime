package mp.jprime.lang.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.money.Monetary;
import javax.money.MonetaryAmountFactory;

/**
 * Fix problem was in concurrent moneta SPI initialization within Java 11
 */
@Configuration
public class JavaMoneyConfig {
  @Bean
  public MonetaryAmountFactory<?> monetaryAmountFactory() {
    return Monetary.getDefaultAmountFactory();
  }
}
