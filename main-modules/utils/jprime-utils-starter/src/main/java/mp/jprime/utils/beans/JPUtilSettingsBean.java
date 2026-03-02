package mp.jprime.utils.beans;

import mp.jprime.utils.JPUtilAction;
import mp.jprime.utils.JPUtilModeSettings;
import mp.jprime.utils.JPUtilSettings;

import java.util.Collection;
import java.util.Collections;

/**
 * Реализация настроек утилиты
 */
public final class JPUtilSettingsBean implements JPUtilSettings {
  private final String code;
  private final String title;
  private final String qName;
  private final String jpPackage;
  private final String[] authRoles;
  private final boolean uni;
  private final String[] jpClasses;
  private final String[] jpClassTags;
  private final String[] jpUtilTags;
  private final JPUtilAction action;
  private final Collection<JPUtilModeSettings> modeList;

  private JPUtilSettingsBean(String code, String title, String qName,
                             String jpPackage, String[] authRoles,
                             boolean uni, String[] jpClasses, String[] jpClassTags, String[] jpUtilTags,
                             JPUtilAction action, Collection<JPUtilModeSettings> modeList) {
    this.code = code;
    this.title = title;
    this.qName = qName;
    this.jpPackage = jpPackage;
    this.authRoles = authRoles;
    this.uni = uni;
    this.jpClasses = jpClasses;
    this.jpClassTags = jpClassTags;
    this.jpUtilTags = jpUtilTags;

    this.action = action;
    this.modeList = modeList != null ? Collections.unmodifiableCollection(modeList) : Collections.emptyList();
  }

  public static JPUtilSettings of(String code, String title, String qName,
                                  String jpPackage, String[] authRoles,
                                  boolean uni, String[] jpClasses, String[] jpClassTags, String[] jpUtilTags,
                                  JPUtilAction action, Collection<JPUtilModeSettings> modeList) {
    return new JPUtilSettingsBean(code, title, qName, jpPackage, authRoles,
        uni, jpClasses, jpClassTags, jpUtilTags,
        action, modeList);
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getQName() {
    return qName;
  }

  @Override
  public String getJpPackage() {
    return jpPackage;
  }

  @Override
  public String[] getAuthRoles() {
    return authRoles;
  }

  @Override
  public boolean isUni() {
    return uni;
  }

  @Override
  public String[] getJpClasses() {
    return jpClasses;
  }

  @Override
  public String[] getJpClassTags() {
    return jpClassTags;
  }

  @Override
  public String[] getJpUtilTags() {
    return jpUtilTags;
  }

  @Override
  public JPUtilAction getAction() {
    return action;
  }

  @Override
  public Collection<JPUtilModeSettings> getModeList() {
    return modeList;
  }
}
