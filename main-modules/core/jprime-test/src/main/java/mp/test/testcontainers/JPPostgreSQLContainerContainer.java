package mp.test.testcontainers;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.delegate.DatabaseDelegate;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.utility.DockerImageName;

import javax.script.ScriptException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.function.BiConsumer;

public final class JPPostgreSQLContainerContainer {
  private JPPostgreSQLContainerContainer() {

  }

  private final static BiConsumer<DatabaseDelegate, String> EXECUTE_FUNC = (DatabaseDelegate delegate, String path) -> {
    try {
      ScriptUtils.executeDatabaseScript(delegate, path, Files.readString(Paths.get(path), StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new ScriptUtils.ScriptLoadException("Could not load classpath init script: " + path, e);
    } catch (ScriptException e) {
      throw new ScriptUtils.UncategorizedScriptException("Error while executing init script: " + path, e);
    }
  };

  public static PostgreSQLContainer init(Collection<String> sqlPaths) {
    PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer(
        DockerImageName.parse("mirror.gcr.io/library/postgres:latest")
            .asCompatibleSubstituteFor("postgres")) {
      @Override
      protected void runInitScriptIfRequired() {
        if (sqlPaths != null) {
          sqlPaths.forEach(s -> EXECUTE_FUNC.accept(getDatabaseDelegate(), s));
        }
      }
    }
        .withDatabaseName("postgresql_test")
        .withUsername("postgres")
        .withPassword("postgres");

    postgreSQLContainer.withInitScript("sql");

    postgreSQLContainer.start();
    return postgreSQLContainer;
  }
}
