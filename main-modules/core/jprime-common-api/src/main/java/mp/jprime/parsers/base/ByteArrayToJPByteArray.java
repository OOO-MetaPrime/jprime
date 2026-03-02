package mp.jprime.parsers.base;

import mp.jprime.lang.JPByteArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * byte[] -> JPByteArray
 */
@Service
public class ByteArrayToJPByteArray extends BaseTypeParser<byte[], JPByteArray> {
  @Override
  public JPByteArray parse(byte[] value) {
    return JPByteArray.of(value);
  }

  @Override
  public Class<byte[]> getInputType() {
    return byte[].class;
  }

  @Override public Class<JPByteArray> getOutputType() {
    return JPByteArray.class;
  }
}
