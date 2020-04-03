package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

import static mp.jprime.formats.DateFormat.YYYYY_MM_DD;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonCond {
  // Кодовое имя атрибута
  private String attr;
  // Условия и значения атрибутов
  private Boolean isNull;
  private Boolean isNotNull;
  private String eq;
  private String neq;
  private String gt;
  private String gte;
  private String lt;
  private String lte;
  private JsonBetweenCond between;
  private String startsWith;
  private String like;
  private String fuzzyLike;
  private String fuzzyOrderLike;
  private Collection<String> in;
  private Collection<String> notIn;
  private JsonExpr exists;
  private JsonExpr notExists;
  // Условия на дату
  private Integer eqYear;
  private Integer neqYear;
  private Integer gtYear;
  private Integer gteYear;
  private Integer ltYear;
  private Integer lteYear;
  private LocalDate eqMonth;
  private LocalDate neqMonth;
  private LocalDate gtMonth;
  private LocalDate gteMonth;
  private LocalDate ltMonth;
  private LocalDate lteMonth;
  private LocalDate eqDay;
  private LocalDate neqDay;
  private LocalDate gtDay;
  private LocalDate gteDay;
  private LocalDate ltDay;
  private LocalDate lteDay;

  // Кодовое имя условия
  private String feature;
  // День проверки условия
  private LocalDate checkDay;
  // Начало периода проверки условия
  private LocalDate checkFromDay;
  // Окончание периода проверки условия
  private LocalDate checkToDay;

  public JsonCond() {

  }

  public static JsonCond newCond() {
    return new JsonCond();
  }

  public static JsonCond newAttrCond(String attr) {
    JsonCond cond = new JsonCond();
    cond.attr = attr;
    return cond;
  }

  public static JsonCond newFeatureCond(String feature) {
    JsonCond cond = new JsonCond();
    cond.feature = feature;
    return cond;
  }

  public String getAttr() {
    return attr;
  }

  public Boolean getIsNull() {
    return isNull;
  }

  public Boolean getIsNotNull() {
    return isNotNull;
  }

  public String getEq() {
    return eq;
  }

  public String getNeq() {
    return neq;
  }

  public String getGt() {
    return gt;
  }

  public String getGte() {
    return gte;
  }

  public String getLt() {
    return lt;
  }

  public String getLte() {
    return lte;
  }

  public JsonBetweenCond getBetween() {
    return between;
  }

  public String getStartsWith() {
    return startsWith;
  }

  public String getLike() {
    return like;
  }

  public String getFuzzyLike() {
    return fuzzyLike;
  }

  public String getFuzzyOrderLike() {
    return fuzzyOrderLike;
  }

  public Collection<String> getIn() {
    return in;
  }

  public Collection<String> getNotIn() {
    return notIn;
  }

  public JsonExpr getExists() {
    return exists;
  }

  public JsonExpr getNotExists() {
    return notExists;
  }

  public String getFeature() {
    return feature;
  }

  public LocalDate getCheckDay() {
    return checkDay;
  }

  public LocalDate getCheckFromDay() {
    return checkFromDay;
  }

  public LocalDate getCheckToDay() {
    return checkToDay;
  }

  public Integer getEqYear() {
    return eqYear;
  }

  public Integer getNeqYear() {
    return neqYear;
  }

  public Integer getGtYear() {
    return gtYear;
  }

  public Integer getGteYear() {
    return gteYear;
  }

  public Integer getLtYear() {
    return ltYear;
  }

  public Integer getLteYear() {
    return lteYear;
  }

  public LocalDate getEqMonth() {
    return eqMonth;
  }

  public LocalDate getNeqMonth() {
    return neqMonth;
  }

  public LocalDate getGtMonth() {
    return gtMonth;
  }

  public LocalDate getGteMonth() {
    return gteMonth;
  }

  public LocalDate getLtMonth() {
    return ltMonth;
  }

  public LocalDate getLteMonth() {
    return lteMonth;
  }

  public LocalDate getEqDay() {
    return eqDay;
  }

  public LocalDate getNeqDay() {
    return neqDay;
  }

  public LocalDate getGtDay() {
    return gtDay;
  }

  public LocalDate getGteDay() {
    return gteDay;
  }

  public LocalDate getLtDay() {
    return ltDay;
  }

  public LocalDate getLteDay() {
    return lteDay;
  }

  public JsonCond isNull(Boolean aNull) {
    isNull = aNull;
    return this;
  }

  public JsonCond isNotNull(Boolean notNull) {
    isNotNull = notNull;
    return this;
  }

  public JsonCond eq(String eq) {
    this.eq = eq;
    return this;
  }

  public JsonCond neq(String neq) {
    this.neq = neq;
    return this;
  }

  public JsonCond gt(String gt) {
    this.gt = gt;
    return this;
  }

  public JsonCond gte(String gte) {
    this.gte = gte;
    return this;
  }

  public JsonCond lt(String lt) {
    this.lt = lt;
    return this;
  }

  public JsonCond lte(String lte) {
    this.lte = lte;
    return this;
  }

  public JsonCond between(JsonBetweenCond between) {
    this.between = between;
    return this;
  }

  public JsonCond like(String like) {
    this.like = like;
    return this;
  }

  public JsonCond fuzzyLike(String fuzzyLike) {
    this.fuzzyLike = fuzzyLike;
    return this;
  }

  public JsonCond fuzzyOrderLike(String fuzzyOrderLike) {
    this.fuzzyOrderLike = fuzzyOrderLike;
    return this;
  }

  public JsonCond startsWith(String startsWith) {
    this.startsWith = startsWith;
    return this;
  }

  public JsonCond in(Collection<String> in) {
    this.in = in;
    return this;
  }

  public JsonCond notIn(Collection<String> notIn) {
    this.notIn = notIn;
    return this;
  }

  public JsonCond exists(JsonExpr exists) {
    this.exists = exists;
    return this;
  }

  public JsonCond notExists(JsonExpr notExists) {
    this.notExists = notExists;
    return this;
  }

  public JsonCond checkDay(Date value) {
    return checkDay(value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null);
  }

  public JsonCond checkDay(LocalDate checkDay) {
    this.checkDay = checkDay;
    return this;
  }

  public JsonCond checkFromDay(Date value) {
    return checkFromDay(value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null);
  }

  public JsonCond checkFromDay(LocalDate checkFromDay) {
    this.checkFromDay = checkFromDay;
    return this;
  }

  public JsonCond checkToDay(Date value) {
    return checkToDay(value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null);
  }

  public JsonCond checkToDay(LocalDate checkToDay) {
    this.checkToDay = checkToDay;
    return this;
  }

  public JsonCond eqYear(Integer date) {
    this.eqYear = date;
    return this;
  }

  public JsonCond neqYear(Integer date) {
    this.neqYear = date;
    return this;
  }

  public JsonCond gtYear(Integer date) {
    this.gtYear = date;
    return this;
  }


  public JsonCond gteYear(Integer date) {
    this.gteYear = date;
    return this;
  }

  public JsonCond ltYear(Integer date) {
    this.ltYear = date;
    return this;
  }

  public JsonCond lteYear(Integer date) {
    this.lteYear = date;
    return this;
  }

  public JsonCond eqMonth(Date value) {
    return eqMonth(value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null);
  }

  public JsonCond eqMonth(LocalDate date) {
    this.eqMonth = date;
    return this;
  }

  public JsonCond neqMonth(Date value) {
    return neqMonth(value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null);
  }

  public JsonCond neqMonth(LocalDate date) {
    this.neqMonth = date;
    return this;
  }

  public JsonCond gtMonth(Date value) {
    return gtMonth(value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null);
  }

  public JsonCond gtMonth(LocalDate date) {
    this.gtMonth = date;
    return this;
  }

  public JsonCond gteMonth(Date value) {
    return gteMonth(value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null);
  }

  public JsonCond gteMonth(LocalDate date) {
    this.gteMonth = date;
    return this;
  }

  public JsonCond ltMonth(Date value) {
    return ltMonth(value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null);
  }

  public JsonCond ltMonth(LocalDate date) {
    this.ltMonth = date;
    return this;
  }

  public JsonCond lteMonth(Date value) {
    return lteMonth(value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null);
  }

  public JsonCond lteMonth(LocalDate date) {
    this.lteMonth = date;
    return this;
  }

  public JsonCond eqDay(Date value) {
    return eqDay(value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null);
  }

  public JsonCond eqDay(LocalDate date) {
    this.eqDay = date;
    return this;
  }

  public JsonCond neqDay(Date value) {
    return neqDay(value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null);
  }

  public JsonCond neqDay(LocalDate date) {
    this.neqDay = date;
    return this;
  }

  public JsonCond gtDay(Date value) {
    return gtDay(value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null);
  }

  public JsonCond gtDay(LocalDate date) {
    this.gtDay = date;
    return this;
  }

  public JsonCond gteDay(Date value) {
    return gteDay(value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null);
  }

  public JsonCond gteDay(LocalDate date) {
    this.gteDay = date;
    return this;
  }

  public JsonCond ltDay(Date value) {
    return ltDay(value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null);
  }

  public JsonCond ltDay(LocalDate date) {
    this.ltDay = date;
    return this;
  }

  public JsonCond lteDay(Date value) {
    return lteDay(value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null);
  }

  public JsonCond lteDay(LocalDate date) {
    this.lteDay = date;
    return this;
  }
}
