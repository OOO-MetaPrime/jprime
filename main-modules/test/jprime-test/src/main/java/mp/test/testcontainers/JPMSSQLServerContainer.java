package mp.test.testcontainers;

import org.testcontainers.containers.MSSQLServerContainer;
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

public final class JPMSSQLServerContainer {

  private JPMSSQLServerContainer() {

  }

  private final static String SETTINGS_PARAMS = ";sendStringParametersAsUnicode=false;encrypt=false";

  private final static BiConsumer<DatabaseDelegate, String> EXECUTE_FUNC = (DatabaseDelegate delegate, String path) -> {
    try {
      ScriptUtils.executeDatabaseScript(delegate, path, Files.readString(Paths.get(path), StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new ScriptUtils.ScriptLoadException("Could not load classpath init script: " + path, e);
    } catch (ScriptException e) {
      throw new ScriptUtils.UncategorizedScriptException("Error while executing init script: " + path, e);
    }
  };

  public static MSSQLServerContainer init(Collection<String> sqlPaths) {
    return init(sqlPaths, "mcr.microsoft.com/mssql/server:2022-latest");
  }

  public static MSSQLServerContainer init() {
    return init(null);
  }

  private static MSSQLServerContainer init(Collection<String> sqlPaths, String fullimageName) {
    MSSQLServerContainer container = new MSSQLServerContainer(
        DockerImageName.parse(fullimageName)) {
      @Override
      public String getJdbcUrl() {
        String url = super.getJdbcUrl();
        String format = url + SETTINGS_PARAMS;
        return format;
      }

      @Override
      protected void runInitScriptIfRequired() {
        if (sqlPaths != null) {
          DatabaseDelegate delegate = getDatabaseDelegate();
          sqlPaths.forEach(s -> EXECUTE_FUNC.accept(delegate, s));
        }
      }
    };

    container.withInitScript("sql");

    container.start();
    return container;
  }

}
