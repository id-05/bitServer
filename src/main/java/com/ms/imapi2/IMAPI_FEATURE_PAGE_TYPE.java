package com.ms.imapi2;

import com4j.*;

/**
 * <p>
 * Type of the feature page data
 * </p>
 */
public enum IMAPI_FEATURE_PAGE_TYPE implements ComEnum {
  /**
   * <p>
   * The value of this constant is 0
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_PROFILE_LIST(0),
  /**
   * <p>
   * The value of this constant is 1
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_CORE(1),
  /**
   * <p>
   * The value of this constant is 2
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_MORPHING(2),
  /**
   * <p>
   * The value of this constant is 3
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_REMOVABLE_MEDIUM(3),
  /**
   * <p>
   * The value of this constant is 4
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_WRITE_PROTECT(4),
  /**
   * <p>
   * The value of this constant is 16
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_RANDOMLY_READABLE(16),
  /**
   * <p>
   * The value of this constant is 29
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_CD_MULTIREAD(29),
  /**
   * <p>
   * The value of this constant is 30
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_CD_READ(30),
  /**
   * <p>
   * The value of this constant is 31
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_DVD_READ(31),
  /**
   * <p>
   * The value of this constant is 32
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_RANDOMLY_WRITABLE(32),
  /**
   * <p>
   * The value of this constant is 33
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_INCREMENTAL_STREAMING_WRITABLE(33),
  /**
   * <p>
   * The value of this constant is 34
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_SECTOR_ERASABLE(34),
  /**
   * <p>
   * The value of this constant is 35
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_FORMATTABLE(35),
  /**
   * <p>
   * The value of this constant is 36
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_HARDWARE_DEFECT_MANAGEMENT(36),
  /**
   * <p>
   * The value of this constant is 37
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_WRITE_ONCE(37),
  /**
   * <p>
   * The value of this constant is 38
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_RESTRICTED_OVERWRITE(38),
  /**
   * <p>
   * The value of this constant is 39
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_CDRW_CAV_WRITE(39),
  /**
   * <p>
   * The value of this constant is 40
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_MRW(40),
  /**
   * <p>
   * The value of this constant is 41
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_ENHANCED_DEFECT_REPORTING(41),
  /**
   * <p>
   * The value of this constant is 42
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_DVD_PLUS_RW(42),
  /**
   * <p>
   * The value of this constant is 43
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_DVD_PLUS_R(43),
  /**
   * <p>
   * The value of this constant is 44
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_RIGID_RESTRICTED_OVERWRITE(44),
  /**
   * <p>
   * The value of this constant is 45
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_CD_TRACK_AT_ONCE(45),
  /**
   * <p>
   * The value of this constant is 46
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_CD_MASTERING(46),
  /**
   * <p>
   * The value of this constant is 47
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_DVD_DASH_WRITE(47),
  /**
   * <p>
   * The value of this constant is 48
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_DOUBLE_DENSITY_CD_READ(48),
  /**
   * <p>
   * The value of this constant is 49
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_DOUBLE_DENSITY_CD_R_WRITE(49),
  /**
   * <p>
   * The value of this constant is 50
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_DOUBLE_DENSITY_CD_RW_WRITE(50),
  /**
   * <p>
   * The value of this constant is 51
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_LAYER_JUMP_RECORDING(51),
  /**
   * <p>
   * The value of this constant is 55
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_CD_RW_MEDIA_WRITE_SUPPORT(55),
  /**
   * <p>
   * The value of this constant is 56
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_BD_PSEUDO_OVERWRITE(56),
  /**
   * <p>
   * The value of this constant is 59
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_DVD_PLUS_R_DUAL_LAYER(59),
  /**
   * <p>
   * The value of this constant is 64
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_BD_READ(64),
  /**
   * <p>
   * The value of this constant is 65
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_BD_WRITE(65),
  /**
   * <p>
   * The value of this constant is 80
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_HD_DVD_READ(80),
  /**
   * <p>
   * The value of this constant is 81
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_HD_DVD_WRITE(81),
  /**
   * <p>
   * The value of this constant is 256
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_POWER_MANAGEMENT(256),
  /**
   * <p>
   * The value of this constant is 257
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_SMART(257),
  /**
   * <p>
   * The value of this constant is 258
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_EMBEDDED_CHANGER(258),
  /**
   * <p>
   * The value of this constant is 259
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_CD_ANALOG_PLAY(259),
  /**
   * <p>
   * The value of this constant is 260
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_MICROCODE_UPDATE(260),
  /**
   * <p>
   * The value of this constant is 261
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_TIMEOUT(261),
  /**
   * <p>
   * The value of this constant is 262
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_DVD_CSS(262),
  /**
   * <p>
   * The value of this constant is 263
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_REAL_TIME_STREAMING(263),
  /**
   * <p>
   * The value of this constant is 264
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_LOGICAL_UNIT_SERIAL_NUMBER(264),
  /**
   * <p>
   * The value of this constant is 265
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_MEDIA_SERIAL_NUMBER(265),
  /**
   * <p>
   * The value of this constant is 266
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_DISC_CONTROL_BLOCKS(266),
  /**
   * <p>
   * The value of this constant is 267
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_DVD_CPRM(267),
  /**
   * <p>
   * The value of this constant is 268
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_FIRMWARE_INFORMATION(268),
  /**
   * <p>
   * The value of this constant is 269
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_AACS(269),
  /**
   * <p>
   * The value of this constant is 272
   * </p>
   */
  IMAPI_FEATURE_PAGE_TYPE_VCPS(272),
  ;

  private final int value;
  IMAPI_FEATURE_PAGE_TYPE(int value) { this.value=value; }
  public int comEnumValue() { return value; }
}
