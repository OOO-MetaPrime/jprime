package mp.jprime.repositories.services;

import mp.jprime.dataaccess.JPObjectAccessService;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPFile;
import mp.jprime.meta.beans.JPAttrBean;
import mp.jprime.meta.beans.JPClassBean;
import mp.jprime.meta.beans.JPFileBean;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.services.JPSecurityStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JPFileCommonUploaderTest.Config.class)
public class JPFileCommonUploaderTest {
  @Lazy(value = false)
  @Configuration
  @ComponentScan(
      basePackages = {
          "mp.jprime.repositories.services"
      })
  @EnableConfigurationProperties
  public static class Config {
  }

  @Autowired
  @InjectMocks
  private JPFileCommonUploader uploader;

  @MockBean
  private RepositoryStorage repositoryStorage;
  @MockBean
  private JPMetaStorage metaStorage;
  @MockBean
  private JPObjectAccessService objectAccessService;
  @MockBean
  private JPSecurityStorage securityManager;

  @Test
  void upload_shouldChangePath_whenTemplatePath() {
    final LocalDate now = LocalDate.now();
    int intMonth = now.getMonth().getValue();
    int day = now.getDayOfMonth();
    String EXPECTED_FILE_PATH = String.format(
        "filePath_%d_%s_%s",
        now.getYear(),
        intMonth < 10 ? "0" + intMonth : String.valueOf(intMonth),
        day < 10 ? "0" + day : String.valueOf(day)
    );
    JPCreate.Builder jpCreate = JPCreate.create("class");
    String fileName = "someFileName";
    JPFile file = JPFileBean.newBuilder("fileAttrCode")
        .storageFilePathAttrCode("storageFilePathAttrCode")
        .storageFilePath("filePath_#year#_#month#_#day#")
        .build();
    JPAttr attr = JPAttrBean.newBuilder()
        .identifier(true)
        .code("attrCode")
        .type(JPType.FILE.getCode())
        .refJpFile(file)
        .build();
    JPClass jpClass = JPClassBean.newBuilder()
        .attrs(Collections.singleton(attr))
        .build();
    Mockito.when(metaStorage.getJPClassByCode("class")).thenReturn(jpClass);

    JPCreate.Builder builder = uploader.upload(jpCreate, attr.getCode(), fileName, null);

    Assertions.assertNotNull(builder);
    Assertions.assertEquals(EXPECTED_FILE_PATH, builder.build().getData().get("storageFilePathAttrCode"));
  }
}
