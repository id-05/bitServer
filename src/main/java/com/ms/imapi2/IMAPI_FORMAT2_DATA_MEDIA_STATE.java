package com.ms.imapi2;

import com4j.*;

/**
 * <p>
 * Set of flags to indicate current media state
 * </p>
 */
public enum IMAPI_FORMAT2_DATA_MEDIA_STATE implements ComEnum {

  /**
   * It is a combination of both of
   * - IMAPI_FORMAT2_DATA_MEDIA_STATE_BLANK(2)
   * - IMAPI_FORMAT2_DATA_MEDIA_STATE_APPENDABLE(4)
   *  Basically a bitwise operation is calculated on the two (or more values)
   * See https://stackoverflow.com/questions/35019562/i-get-6-as-state-result-for-a-blank-cd
   * */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_BLANK_AND_APPENDABLE(6, "Media is blank and appendable"),

  /**
   * It is a combination of both of
   * - IMAPI_FORMAT2_DATA_MEDIA_STATE_WRITE_PROTECTED (8192)
   * - IMAPI_FORMAT2_DATA_MEDIA_STATE_UNSUPPORTED_MEDIA ( 32768 )
   *  Basically a bitwise operation is calculated on the two (or more values)
   * See https://stackoverflow.com/questions/35019562/i-get-6-as-state-result-for-a-blank-cd
   * */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_PROTECTED_AND_UNSUPPORTED(40960, "Protected and unsupported"),

  /**
   * <p>
   * Unknown
   * </p>
   * <p>
   * The value of this constant is 0
   * </p>
   */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_UNKNOWN(0, "Media status unknown"),
  /**
   * <p>
   * Mask of 'supported/informational' media flags
   * Reports information (but not errors) about the media state.
   *
   * </p>
   * <p>
   * The value of this constant is 15
   * </p>
   */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_INFORMATIONAL_MASK(15, "Reports information (but not errors) about the media state."),
  /**
   * <p>
   * Mask of 'not supported' media flags
   * </p>
   * <p>
   * The value of this constant is 64512
   * </p>
   */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_UNSUPPORTED_MASK(64512, "Reports an unsupported media state."),
  /**
   * <p>
   * Media may only be overwritten
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_OVERWRITE_ONLY(1, "Media may only be overwritten"),
  /**
   * Media is randomly writable. This indicates that a single session can be written to this disc.
   *
   * The value of this constant is 1
   * </p>
   */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_RANDOMLY_WRITABLE(1, "Media is randomly writable. This indicates that a single session can be written to this disc."),
  /**
   * Media is blank
   * <p>
   * The value of this constant is 2
   * </p>
   */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_BLANK(2, "Media is blank"),
  /**
   * <p>
   * Media is appendable
   * </p>
   * <p>
   * The value of this constant is 4
   * </p>
   */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_APPENDABLE(4, "Media is appendable"),
  /**
   * <p>
   * Media may only be written to one more time, or does not support multiple sessions
   * </p>
   * <p>
   * The value of this constant is 8
   * </p>
   */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_FINAL_SESSION(8, "Media may only be written to one more time, or does not support multiple sessions"),
  /**
   * <p>
   * Media is not usable by data writer (may require erase or other recovery)
   * </p>
   * <p>
   * The value of this constant is 1024
   * </p>
   */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_DAMAGED(1024, "Media is not usable by data writer (may require erase or other recovery)"),
  /**
   * <p>
   * Media must be erased before use
   * </p>
   * <p>
   * The value of this constant is 2048
   * </p>
   */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_ERASE_REQUIRED(2048, "Media must be erased before use"),
  /**
   * <p>
   * Media has a partially written last session, which is not supported
   * </p>
   * <p>
   * The value of this constant is 4096
   * </p>
   */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_NON_EMPTY_SESSION(4096, "Media has a partially written last session, which is not supported"),
  /**
   * <p>
   * Media (or drive) is write protected
   * </p>
   * <p>
   * The value of this constant is 8192
   * </p>
   */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_WRITE_PROTECTED(8192, "Media (or drive) is write protected"),
  /**
   * <p>
   * Media cannot be written to (finalized)
   * </p>
   * <p>
   * The value of this constant is 16384
   * </p>
   */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_FINALIZED(16384, "Media cannot be written to (finalized)"),
  /**
   * <p>
   * Media is not supported by data writer
   * </p>
   * <p>
   * The value of this constant is 32768
   * </p>
   */
  IMAPI_FORMAT2_DATA_MEDIA_STATE_UNSUPPORTED_MEDIA(32768, "Media is not supported by data writer"),
  ;

  private final int value;
  private final String desc;

  IMAPI_FORMAT2_DATA_MEDIA_STATE(int value, String desc) {
    this.value=value;
    this.desc = desc;
  }
  public int comEnumValue() { return value; }
  public String getDesc() {
    return desc;
  }

}
