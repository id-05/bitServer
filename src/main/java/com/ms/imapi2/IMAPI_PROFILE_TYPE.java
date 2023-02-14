package com.ms.imapi2;

import com4j.*;

/**
 * <p>
 * Type of the profile in feature page data
 * </p>
 */
public enum IMAPI_PROFILE_TYPE implements ComEnum {
  /**
   * <p>
   * The value of this constant is 0
   * </p>
   */
  IMAPI_PROFILE_TYPE_INVALID(0),
  /**
   * <p>
   * The value of this constant is 1
   * </p>
   */
  IMAPI_PROFILE_TYPE_NON_REMOVABLE_DISK(1),
  /**
   * <p>
   * The value of this constant is 2
   * </p>
   */
  IMAPI_PROFILE_TYPE_REMOVABLE_DISK(2),
  /**
   * <p>
   * The value of this constant is 3
   * </p>
   */
  IMAPI_PROFILE_TYPE_MO_ERASABLE(3),
  /**
   * <p>
   * The value of this constant is 4
   * </p>
   */
  IMAPI_PROFILE_TYPE_MO_WRITE_ONCE(4),
  /**
   * <p>
   * The value of this constant is 5
   * </p>
   */
  IMAPI_PROFILE_TYPE_AS_MO(5),
  /**
   * <p>
   * The value of this constant is 8
   * </p>
   */
  IMAPI_PROFILE_TYPE_CDROM(8),
  /**
   * <p>
   * The value of this constant is 9
   * </p>
   */
  IMAPI_PROFILE_TYPE_CD_RECORDABLE(9),
  /**
   * <p>
   * The value of this constant is 10
   * </p>
   */
  IMAPI_PROFILE_TYPE_CD_REWRITABLE(10),
  /**
   * <p>
   * The value of this constant is 16
   * </p>
   */
  IMAPI_PROFILE_TYPE_DVDROM(16),
  /**
   * <p>
   * The value of this constant is 17
   * </p>
   */
  IMAPI_PROFILE_TYPE_DVD_DASH_RECORDABLE(17),
  /**
   * <p>
   * The value of this constant is 18
   * </p>
   */
  IMAPI_PROFILE_TYPE_DVD_RAM(18),
  /**
   * <p>
   * The value of this constant is 19
   * </p>
   */
  IMAPI_PROFILE_TYPE_DVD_DASH_REWRITABLE(19),
  /**
   * <p>
   * The value of this constant is 20
   * </p>
   */
  IMAPI_PROFILE_TYPE_DVD_DASH_RW_SEQUENTIAL(20),
  /**
   * <p>
   * The value of this constant is 21
   * </p>
   */
  IMAPI_PROFILE_TYPE_DVD_DASH_R_DUAL_SEQUENTIAL(21),
  /**
   * <p>
   * The value of this constant is 22
   * </p>
   */
  IMAPI_PROFILE_TYPE_DVD_DASH_R_DUAL_LAYER_JUMP(22),
  /**
   * <p>
   * The value of this constant is 26
   * </p>
   */
  IMAPI_PROFILE_TYPE_DVD_PLUS_RW(26),
  /**
   * <p>
   * The value of this constant is 27
   * </p>
   */
  IMAPI_PROFILE_TYPE_DVD_PLUS_R(27),
  /**
   * <p>
   * The value of this constant is 32
   * </p>
   */
  IMAPI_PROFILE_TYPE_DDCDROM(32),
  /**
   * <p>
   * The value of this constant is 33
   * </p>
   */
  IMAPI_PROFILE_TYPE_DDCD_RECORDABLE(33),
  /**
   * <p>
   * The value of this constant is 34
   * </p>
   */
  IMAPI_PROFILE_TYPE_DDCD_REWRITABLE(34),
  /**
   * <p>
   * The value of this constant is 42
   * </p>
   */
  IMAPI_PROFILE_TYPE_DVD_PLUS_RW_DUAL(42),
  /**
   * <p>
   * The value of this constant is 43
   * </p>
   */
  IMAPI_PROFILE_TYPE_DVD_PLUS_R_DUAL(43),
  /**
   * <p>
   * The value of this constant is 64
   * </p>
   */
  IMAPI_PROFILE_TYPE_BD_ROM(64),
  /**
   * <p>
   * The value of this constant is 65
   * </p>
   */
  IMAPI_PROFILE_TYPE_BD_R_SEQUENTIAL(65),
  /**
   * <p>
   * The value of this constant is 66
   * </p>
   */
  IMAPI_PROFILE_TYPE_BD_R_RANDOM_RECORDING(66),
  /**
   * <p>
   * The value of this constant is 67
   * </p>
   */
  IMAPI_PROFILE_TYPE_BD_REWRITABLE(67),
  /**
   * <p>
   * The value of this constant is 80
   * </p>
   */
  IMAPI_PROFILE_TYPE_HD_DVD_ROM(80),
  /**
   * <p>
   * The value of this constant is 81
   * </p>
   */
  IMAPI_PROFILE_TYPE_HD_DVD_RECORDABLE(81),
  /**
   * <p>
   * The value of this constant is 82
   * </p>
   */
  IMAPI_PROFILE_TYPE_HD_DVD_RAM(82),
  /**
   * <p>
   * The value of this constant is 65535
   * </p>
   */
  IMAPI_PROFILE_TYPE_NON_STANDARD(65535),
  ;

  private final int value;
  IMAPI_PROFILE_TYPE(int value) { this.value=value; }
  public int comEnumValue() { return value; }
}
