package mp.jprime.lang;

import java.io.Serializable;
import java.util.*;

public class JPByteArray implements Comparable<JPByteArray>, Serializable {
  private final byte[] bytes;

  public JPByteArray(byte[] bytes) {
    this.bytes = Arrays.copyOf(bytes, bytes.length);
  }

  public JPByteArray(Byte[] bytesObjects) {
    this.bytes = new byte[bytesObjects.length];
    for (int i = 0; i < bytesObjects.length; i++) {
      this.bytes[i] = bytesObjects[i];
    }
  }

  /**
   * Конвертация в массив
   *
   * @return Массив байт
   */
  public byte[] toArray() {
    return bytes;
  }

  /**
   * Конвертация в JPByteArray
   *
   * @param bytes Массив байт
   * @return JPByteArray
   */
  public static JPByteArray of(byte[] bytes) {
    return new JPByteArray(bytes == null || bytes.length == 0 ? new byte[0] : bytes);
  }

  /**
   * Создать JPByteArray
   *
   * @param bytes Массив объектов байт
   * @return JPByteArray
   */
  public static JPByteArray of(Byte[] bytes) {
    return new JPByteArray(bytes == null || bytes.length == 0 ? new Byte[0] : bytes);
  }

  /**
   * Создать JPByteArray
   *
   * @param values Коллекция объектов байт
   * @return JPByteArray
   */
  public static JPByteArray of(Collection<Byte> values) {
    if (values == null || values.isEmpty()) {
      return new JPByteArray(new byte[0]);
    }
    byte[] arr = new byte[values.size()];
    int i = 0;
    for (Byte b : values) {
      arr[i++] = b;
    }
    return new JPByteArray(arr);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    JPByteArray that = (JPByteArray) obj;
    return Arrays.equals(this.bytes, that.bytes);
  }

  @Override
  public int compareTo(JPByteArray other) {
    if (other == null) {
      return -1;
    }
    int lengthComparison = Integer.compare(this.bytes.length, other.bytes.length);
    if (lengthComparison != 0) {
      return lengthComparison;
    }
    for (int i = 0; i < this.bytes.length; i++) {
      int byteComparison = Byte.compare(this.bytes[i], other.bytes[i]);
      if (byteComparison != 0) {
        return byteComparison;
      }
    }
    return 0;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(bytes);
  }

}
