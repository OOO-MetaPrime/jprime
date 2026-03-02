package mp.jprime.repositories.services;

import mp.jprime.dataaccess.JPObjectAccessService;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.globalsettings.JPGlobalSettingsService;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPFile;
import mp.jprime.meta.beans.JPAttrBean;
import mp.jprime.meta.beans.JPClassBean;
import mp.jprime.meta.beans.JPFileBean;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.repositories.RepositoryGlobalStorage;
import mp.jprime.security.services.JPSecurityStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JPFileCommonUploaderTest.Config.class)
class JPFileCommonUploaderTest {
  @Configuration
  @ComponentScan(
      basePackages = {
          "mp.jprime.repositories.services"
      })
  @EnableConfigurationProperties
  public static class Config {
  }

  @Autowired
  private JPFileCommonUploader uploader;

  @MockitoBean
  private JPGlobalSettingsService globalSettingsService;
  @MockitoBean
  private JPMetaStorage metaStorage;
  @MockitoBean
  private JPObjectAccessService objectAccessService;
  @MockitoBean
  private JPSecurityStorage securityManager;
  @MockitoBean
  private RepositoryGlobalStorage repositoryStorage;

  @Test
  void upload_shouldChangePath_whenTemplatePath_year_month_day() {
    final LocalDateTime now = LocalDateTime.now();
    int month = now.getMonthValue();
    int day = now.getDayOfMonth();
    int hour = now.getHour();
    int minute = now.getMinute();
    String EXPECTED_FILE_PATH = String.format(
        "filePath_%d_%s_%s_%s_%s",
        now.getYear(),
        month < 10 ? "0" + month : String.valueOf(month),
        day < 10 ? "0" + day : String.valueOf(day),
        hour < 10 ? "0" + hour : String.valueOf(hour),
        minute < 10 ? "0" + minute : String.valueOf(minute)
    );
    JPCreate.Builder jpCreate = JPCreate.create("class");
    String fileName = "someFileName";
    JPFile file = JPFileBean.newBuilder("fileAttrCode")
        .storageFilePathAttrCode("storageFilePathAttrCode")
        .storageFilePath("filePath_#year#_#month#_#day#_#hour#_#minute#")
        .build();
    JPAttr attr = JPAttrBean.newBuilder()
        .identifier(true)
        .code("attrCode")
        .type(JPType.FILE)
        .refJpFile(file)
        .build();
    JPClass jpClass = JPClassBean.newBuilder()
        .attrs(Collections.singleton(attr))
        .build();
    Mockito.when(metaStorage.getJPClassByCode("class")).thenReturn(jpClass);

    JPCreate.Builder builder = uploader.upload(jpCreate, attr.getCode(), fileName, null);

    assertEquals(EXPECTED_FILE_PATH, uploader.getStoragePath(file.getStorageFilePath()));
    assertNotNull(builder);
  }
}
