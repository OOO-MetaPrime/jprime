package mp.jprime.dataaccess.functions.common;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import mp.jprime.dataaccess.functions.JPDataBaseFunction;
import mp.jprime.dataaccess.functions.JPDataFunctionParams;
import mp.jprime.dataaccess.functions.JPDataFunctionResult;
import mp.jprime.dataaccess.functions.beans.JPDataFunctionResultBean;
import mp.jprime.parsers.ParserService;
import mp.jprime.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * Функция генерации штрихкода
 */
@Service
public class JPBarcodeFunction extends JPDataBaseFunction<byte[]> {
  /**
   * Кодовое имя функции
   */
  private static final String CODE = "barcode";

  /**
   * Шаблоны
   */
  private interface Template {
    // Шаблон для генерации штрихкода
    String BARCODE_EAN_13 = "ean13Barcode";
    // Шаблон для генерации 2D штрихкода (QR-кода)
    String BARCODE_2D = "2DBarcode";
  }


  /**
   * Шаблоны для вызова функции
   */
  private static final Collection<String> TEMPLATES = List.of(
      Template.BARCODE_EAN_13,
      Template.BARCODE_2D
  );

  /**
   * Параметр: текст штрихкода
   */
  private static final String PARAM_TEXT = "text";

  /**
   * Параметр: высота штрихкода
   */
  private static final String PARAM_HEIGHT = "height";

  /**
   * Параметр: ширина штрихкода
   */
  private static final String PARAM_WIDTH = "width";

  /**
   * Параметр: отступ штрихкода
   */
  private static final String MARGIN = "margin";

  /**
   * Кодовые имена аргументов функции
   */
  private static final List<String> ARG_CODES = List.of(PARAM_TEXT, PARAM_HEIGHT, PARAM_WIDTH, MARGIN);

  private ParserService parserService;

  @Autowired
  private void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }

  @Override
  public String getCode() {
    return CODE;
  }

  @Override
  public Collection<String> getTemplates() {
    return TEMPLATES;
  }

  @Override
  protected List<String> getArgCodes() {
    return ARG_CODES;
  }

  @Override
  protected Map<JPDataFunctionParams, JPDataFunctionResult<byte[]>> compute(Collection<JPDataFunctionParams> args, AuthInfo auth) {
    Map<JPDataFunctionParams, JPDataFunctionResult<byte[]>> result = new HashMap<>(args.size());
    args.forEach(x -> {
      String barcodeText = parserService.parseTo(String.class, x.getArgs().get(0));
      int height = parserService.parseTo(Integer.class, x.getArgs().get(1));
      int width = parserService.parseTo(Integer.class, x.getArgs().get(2));
      int margin = parserService.parseTo(Integer.class, x.getArgs().get(3));

      try {
        Map<EncodeHintType, Object> hints = new HashMap<>() {{
          put(EncodeHintType.MARGIN, margin);
        }};

        BitMatrix bitMatrix = null;
        if (Template.BARCODE_EAN_13.equals(x.getTemplate())) {
          EAN13Writer barcodeWriter = new EAN13Writer();
          bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.EAN_13, width, height, hints);
        } else if (Template.BARCODE_2D.equals(x.getTemplate())) {
          QRCodeWriter barcodeWriter = new QRCodeWriter();
          bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, width, height, hints);
        }
        if (bitMatrix != null) {
          ByteArrayOutputStream stream = new ByteArrayOutputStream();
          MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream);
          result.put(x, JPDataFunctionResultBean.of(stream.toByteArray(), "${bitmap:" + width + "x" + height + "}"));
        }
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
      }
    });
    return result;
  }
}
