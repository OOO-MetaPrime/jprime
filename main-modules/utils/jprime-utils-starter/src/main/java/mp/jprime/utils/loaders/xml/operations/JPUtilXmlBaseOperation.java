package mp.jprime.utils.loaders.xml.operations;

import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.json.beans.JsonExpr;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.parsers.ParserService;
import mp.jprime.utils.loaders.xml.JPUtilXmlOperation;
import mp.jprime.xml.services.JPXmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Базовая логика для реализации JPUtilXmlOperation
 */
public abstract class JPUtilXmlBaseOperation implements JPUtilXmlOperation {
  @Service
  private static final class Links {
    private static JPJsonMapper JSON_MAPPER;
    private static JPXmlMapper XML_MAPPER;
    private static ParserService PARSER_SERVICE;

    private Links(@Autowired JPJsonMapper jsonMapper,
                  @Autowired JPXmlMapper xmlMapper,
                  @Autowired ParserService parserService) {
      JSON_MAPPER = jsonMapper;
      XML_MAPPER = xmlMapper;
      PARSER_SERVICE = parserService;
    }
  }

  protected ParserService getParserService() {
    return Links.PARSER_SERVICE;
  }

  protected JPXmlMapper getXmlMapper() {
    return Links.XML_MAPPER;
  }

  protected Filter toFilter(String sFilter) {
    return JsonExpr.toFilter(Links.JSON_MAPPER.toObject(JsonExpr.class, sFilter));
  }
}
