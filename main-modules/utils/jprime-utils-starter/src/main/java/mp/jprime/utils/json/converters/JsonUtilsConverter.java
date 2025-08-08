package mp.jprime.utils.json.converters;

import mp.jprime.common.JPClassAttr;
import mp.jprime.common.JPEnum;
import mp.jprime.meta.json.beans.JsonJPMoney;
import mp.jprime.json.beans.JsonJPEnum;
import mp.jprime.json.beans.JsonParam;
import mp.jprime.meta.beans.JPType;
import mp.jprime.utils.JPUtilMode;
import mp.jprime.utils.JPUtilParam;
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
        .type(utilMode.getType().getCode())
        .jpAttrs(utilMode.getJpAttrs()
            .stream()
            .map(this::toUtilClassAttr)
            .collect(Collectors.toList())
        )
        .inParams(utilMode.getInParams()
            .stream()
            .map(this::toUtilParam)
            .collect(Collectors.toList())
        )
        .inParamsDefValues(utilMode.isInParamsDefValues())
        .infoMessage(utilMode.getInfoMessage())
        .validate(utilMode.isValidate())
        .resultType(utilMode.getResultType())
        .outCustomParams(utilMode.getOutCustomParams()
            .stream()
            .map(this::toUtilParam)
            .collect(Collectors.toList())
        )
        .build();
  }

  private JsonParam toUtilParam(JPUtilParam param) {
    JPType type = param.getType();

    return JsonParam.newBuilder()
        .code(param.getCode())
        .type(type != null ? type.getCode() : null)
        .stringFormat(param.getStringFormat() != null ? param.getStringFormat().getCode() : null)
        .stringMask(param.getStringMask())
        .fileTypes(param.getFileTypes())
        .length(param.getLength())
        .description(param.getDescription())
        .qName(param.getQName())
        .mandatory(param.isMandatory())
        .multiple(param.isMultiple())
        .external(true)
        .refJpClass(param.getRefJpClassCode())
        .refJpAttr(param.getRefJpAttrCode())
        .refFilter(param.getRefFilter())
        .money(type == JPType.MONEY ? JsonJPMoney.toJson(param.getMoney()) : null)
        .clientSearch(param.isClientSearch())
        .readOnly(param.isReadOnly())
        .enums(param.getEnums()
            .stream()
            .map(this::toUtilEnum)
            .collect(Collectors.toList()))
        .build();
  }

  private JsonJPEnum toUtilEnum(JPEnum paramEnum) {
    return JsonJPEnum.of(paramEnum.getValue(), paramEnum.getDescription(), paramEnum.getQName());
  }

  private JsonUtilClassAttr toUtilClassAttr(JPClassAttr classAttr) {
    return JsonUtilClassAttr.newBuilder()
        .jpClass(classAttr.getJpClass())
        .jpAttr(classAttr.getJpAttr())
        .build();
  }
}
