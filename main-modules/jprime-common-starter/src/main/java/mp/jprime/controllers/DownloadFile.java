package mp.jprime.controllers;

import mp.jprime.files.FileTypeDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

public interface DownloadFile {
  Logger LOG = LoggerFactory.getLogger(DownloadFile.class);
  FileTypeDetector FILE_TYPE_DETECTOR = new FileTypeDetector();

  String CONTENT_DISP_PREFIX = "filename=";
  String CONTENT_DISP_EXTRA_PREFIX = "filename*=UTF-8''";
  String USER_AGENT_FIREFOX = "Firefox";

  default Mono<ResponseEntity> read(InputStream is, String fileTitle, String userAgent) {
    return read(is, fileTitle, null, null, userAgent);
  }

  default Mono<ResponseEntity> read(InputStream is, String fileTitle, Long fileLength, String userAgent) {
    return read(is, fileTitle, fileLength, null, userAgent);
  }

  default Mono<ResponseEntity> read(InputStream is, String fileTitle, Long fileLength, String contentType, String userAgent) {
    return Mono.justOrEmpty(contentType)
        .map(x -> {
          if (x == null) {
            x = FILE_TYPE_DETECTOR.mediaType(fileTitle);
          }
          return x;
        })
        .map(x -> {
              ResponseEntity.BodyBuilder b = ResponseEntity.ok()
                  .contentType(x == null ? APPLICATION_OCTET_STREAM : MediaType.valueOf(x));
              if (fileLength != null) {
                b.contentLength(fileLength);
              }
              String encodedFileName = rfc5987_encode(fileTitle);
              return b.header("Content-Disposition",
                  userAgent == null || !userAgent.contains(USER_AGENT_FIREFOX) ?
                      CONTENT_DISP_PREFIX + "\"" + encodedFileName + "\"" : CONTENT_DISP_EXTRA_PREFIX + encodedFileName)
                  .body(new InputStreamResource(is));
            }
        );
  }

  default String rfc5987_encode(final String s) {
    final byte[] s_bytes = s.getBytes(StandardCharsets.UTF_8);
    final int len = s_bytes.length;
    final StringBuilder sb = new StringBuilder(len << 1);
    final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    final byte[] attr_char = {'!', '#', '$', '&', '+', '-', '.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '|', '~'};
    for (byte b : s_bytes) {
      if (Arrays.binarySearch(attr_char, b) >= 0) {
        sb.append((char) b);
      } else {
        sb.append('%');
        sb.append(digits[0x0f & (b >>> 4)]);
        sb.append(digits[b & 0x0f]);
      }
    }
    return sb.toString();
  }
}
