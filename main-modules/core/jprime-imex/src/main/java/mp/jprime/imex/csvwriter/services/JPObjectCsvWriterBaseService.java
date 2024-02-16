package mp.jprime.imex.csvwriter.services;

import mp.jprime.concurrent.JPForkJoinPoolService;
import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.JPObjectRepositoryServiceAware;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.imex.csvwriter.JPObjectCsvWriterService;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import mp.jprime.streams.JPPipedInputStream;
import mp.jprime.streams.JPPipedOutputStream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipOutputStream;

/**
 * Базовый сервис выгрузки {@link JPObject} в CSV
 */
@Service
public class JPObjectCsvWriterBaseService implements JPObjectCsvWriterService, ParserServiceAware, JPObjectRepositoryServiceAware {
  /**
   * Количество объектов в 1 выборке
   */
  private static final int DEFAULT_LIMIT = 10000;
  private static final Logger LOG = LoggerFactory.getLogger(JPObjectCsvWriterBaseService.class);

  private JPObjectRepositoryService repo;
  private ParserService parser;
  private JPMetaStorage metaStorage;

  @Override
  public void setJpObjectRepositoryService(JPObjectRepositoryService repositoryService) {
    this.repo = repositoryService;
  }

  @Override
  public void setParserService(ParserService parserService) {
    this.parser = parserService;
  }

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Override
  public InputStream zipOf(String jpClass, Collection<String> attrs, String filesName, Integer fileSize) {
    return getInputStream(jpClass, attrs, filesName, fileSize);
  }

  @Override
  public InputStream of(String jpClass, Collection<String> attrs, String filesName) {
    return getInputStream(jpClass, attrs, filesName, null);
  }

  private InputStream getInputStream(String jpClassCode, Collection<String> attrs,
                                     String filesName, Integer fileSize) {
    try {
      JPClass jpClass = metaStorage.getJPClassByCode(jpClassCode);
      if (CollectionUtils.isEmpty(attrs) || jpClass == null) {
        return InputStream.nullInputStream();
      } else {
        JPAttr primaryKeyAttr = jpClass.getPrimaryKeyAttr();
        if (primaryKeyAttr == null) {
          throw new JPRuntimeException("jprime.csvWriter.jpObject.wrongPrimaryKeyAttr", "Некорректный первичный ключ");
        }

        boolean isZip = fileSize != null;
        List<JPAttr> jpAttrs = getJpAttrs(attrs, jpClass);

        JPPipedOutputStream os = new JPPipedOutputStream();
        JPPipedInputStream is = new JPPipedInputStream(os);
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
              try (JPCsvBaseWriter<JPObject> writer = getWriter(filesName, fileSize, isZip, os, jpAttrs)) {
                writeFound(
                    writer,
                    JPSelect.from(jpClass)
                        .orderByAsc(primaryKeyAttr.getCode())
                        .attrs(attrs)
                        .limit(DEFAULT_LIMIT),
                    os
                );
              } catch (Exception e) {
                LOG.error("Exception occurred while writing objects of meta-class \"{}\": {}", jpClassCode, e.getMessage(), e);
              } finally {
                IOUtils.closeQuietly(os);
              }
            },
            JPForkJoinPoolService.pool());

        return is;
      }
    } catch (IOException e) {
      throw new JPRuntimeException(e);
    }
  }

  private List<JPAttr> getJpAttrs(Collection<String> attrs, JPClass jpClass) {
    List<JPAttr> jpAttrs = new ArrayList<>();
    for (String attr : attrs) {
      JPAttr jpAttr = jpClass.getAttr(attr);

      if (jpAttr == null) {
        throw new JPRuntimeException("jprime.csvWriter.jpObject.wrongAttr", "Некорректные атрибуты");
      }

      jpAttrs.add(jpAttr);
    }
    return jpAttrs;
  }

  private JPCsvBaseWriter<JPObject> getWriter(String filesName, Integer fileSize, boolean isZip, PipedOutputStream os, List<JPAttr> jpAttrs) {
    return isZip ?
        JPObjectCsvZipWriter.newBuilder(new ZipOutputStream(os), parser, jpAttrs, filesName).maxFileSize(fileSize).build() :
        JPObjectCsvWriter.newBuilder(os, parser, jpAttrs).build();
  }

  private void writeFound(JPCsvBaseWriter<JPObject> writer, JPSelect.Builder select, JPPipedOutputStream os) {
    int offset = 0;
    int foundCount;
    Collection<JPObject> jpObjects = repo.getList(select.offset(offset).build());
    while ((foundCount = jpObjects.size()) > 0 && !os.isClosed()) {
      writer.write(jpObjects);
      offset += foundCount;
      jpObjects = repo.getList(select.offset(offset).build());
    }
  }
}
