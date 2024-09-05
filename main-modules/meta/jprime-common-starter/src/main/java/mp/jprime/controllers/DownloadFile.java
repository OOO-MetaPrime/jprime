package mp.jprime.controllers;

import mp.jprime.files.FileTypeDetector;
import mp.jprime.web.JPHttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

public interface DownloadFile {
  Logger LOG = LoggerFactory.getLogger(DownloadFile.class);

  String CONTENT_DISP_PREFIX = "filename=";
  String CONTENT_DISP_EXTRA_PREFIX = "filename*=UTF-8''";
  String USER_AGENT_FIREFOX = "Firefox";

  default Mono<Void> writeTo(ServerWebExchange swe, InputStream is, String fileTitle, String userAgent) {
    return writeTo(swe, is, fileTitle, null, null, userAgent);
  }

  default Mono<Void> writeTo(ServerWebExchange swe, InputStream is, String fileTitle, Long fileLength, String userAgent) {
    return writeTo(swe, is, fileTitle, fileLength, null, userAgent);
  }

  default Mono<Void> writeTo(ServerWebExchange swe, InputStream is, String fileTitle, Long fileLength,
                             String contentType, String userAgent) {
    return Mono.justOrEmpty(contentType)
        .defaultIfEmpty(FileTypeDetector.mediaType(fileTitle))
        .flatMap(x -> {
              String encodedFileName = rfc5987_encode(fileTitle);

              ServerHttpResponse resp = swe.getResponse();
              resp.setStatusCode(HttpStatus.OK);
              if (fileLength != null) {
                resp.getHeaders().set(HttpHeaders.CONTENT_LENGTH, Long.toString(fileLength));
              }
              resp.getHeaders().set(HttpHeaders.CONTENT_TYPE, x == null ? APPLICATION_OCTET_STREAM_VALUE : MediaType.valueOf(x).toString());
              resp.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, userAgent == null || !userAgent.contains(USER_AGENT_FIREFOX) ?
                  CONTENT_DISP_PREFIX + "\"" + encodedFileName + "\"" : CONTENT_DISP_EXTRA_PREFIX + encodedFileName);
              resp.getHeaders().set(JPHttpHeaders.X_JPRIME_FILE_TITLE, fileTitle);
              return resp.writeWith(DataBufferUtils.read(new InputStreamResource(is), resp.bufferFactory(), 512));
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
