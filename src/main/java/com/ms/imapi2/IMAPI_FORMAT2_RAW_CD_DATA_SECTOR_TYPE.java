package com.ms.imapi2;

import com4j.*;

/**
 * <p>
 * Data sector type to use when writing to CD in Disc-At-Once (RAW) mode
 * </p>
 */
public enum IMAPI_FORMAT2_RAW_CD_DATA_SECTOR_TYPE implements ComEnum {
  /**
   * <p>
   * Raw Main Channel P an Q Sub-channel data (type 0x01)
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  IMAPI_FORMAT2_RAW_CD_SUBCODE_PQ_ONLY(1),
  /**
   * <p>
   * Raw Main Channel With Cooked P-W Subcode (type 0x02)
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  IMAPI_FORMAT2_RAW_CD_SUBCODE_IS_COOKED(2),
  /**
   * <p>
   * Raw Main Channel With Raw P-W Subcode (type 0x03)
   * </p>
   * <p>
   * The value of this constant is 3
   * </p>
   */
  IMAPI_FORMAT2_RAW_CD_SUBCODE_IS_RAW(3),
  ;

  private final int value;
  IMAPI_FORMAT2_RAW_CD_DATA_SECTOR_TYPE(int value) { this.value=value; }
  public int comEnumValue() { return value; }
}
