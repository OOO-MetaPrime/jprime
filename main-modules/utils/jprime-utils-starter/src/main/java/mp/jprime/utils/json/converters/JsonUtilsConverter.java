package mp.jprime.utils.json.converters;

import mp.jprime.common.JPClassAttr;
import mp.jprime.json.beans.JsonParam;
import mp.jprime.utils.JPUtilMode;
import mp.jprime.utils.json.JsonUtilClassAttr;
import mp.jprime.utils.json.JsonUtilMode;
import mp.jprime.utils.json.JsonUtilModeLabel;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class JsonUtilsConverter {

  public JsonUtilModeLabel toUtilModeLabel(JPUtilMode utilMode) {
    if (utilMode == null) {
      return null;
    }
    return JsonUtilModeLabel.newBuilder()
        .utilCode(utilMode.getUtilCode())
        .modeCode(utilMode.getModeCode())
        .title(utilMode.getTitle())
        .qName(utilMode.getQName())
        .uni(utilMode.isUni())
        .jpClasses(utilMode.getJpClasses())
        .jpClassTags(utilMode.getJpClassTags())
        .jpUtilTags(utilMode.getJpUtilTags())
        .type(utilMode.getType().getCode())
        .build();
  }

  public JsonUtilMode toUtilMode(JPUtilMode utilMode) {
    if (utilMode == null) {
      return null;
    }
    return JsonUtilMode.newBuilder()
        .utilCode(utilMode.getUtilCode())
        .modeCode(utilMode.getModeCode())
        .title(utilMode.getTitle())
        .qName(utilMode.getQName())
        .confirmMessage(utilMode.getConfirmMessage())
        .uni(utilMode.isUni())
        .jpClasses(utilMode.getJpClasses())
        .jpClassTags(utilMode.getJpClassTags())
        .jpUtilTags(utilMode.getJpUtilTags())
        .type(utilMode.getType().getCode())
        .jpAttrs(utilMode.getJpAttrs()
            .stream()
            .map(this::toUtilClassAttr)
            .collect(Collectors.toList())
        )
        .inParams(utilMode.getInParams()
            .stream()
            .map(JsonParam::external)
            .collect(Collectors.toList())
        )
        .inParamsDefValues(utilMode.isInParamsDefValues())
        .useDynamicParams(utilMode.isUseDynamicParams())
        .infoMessage(utilMode.getInfoMessage())
        .validate(utilMode.isValidate())
        .resultType(utilMode.getResultType())
        .outCustomParams(utilMode.getOutCustomParams()
            .stream()
            .map(JsonParam::external)
            .collect(Collectors.toList())
        )
        .build();
  }

  private JsonUtilClassAttr toUtilClassAttr(JPClassAttr classAttr) {
    return JsonUtilClassAttr.newBuilder()
        .jpClass(classAttr.getJpClass())
        .jpAttr(classAttr.getJpAttr())
        .build();
  }
}
