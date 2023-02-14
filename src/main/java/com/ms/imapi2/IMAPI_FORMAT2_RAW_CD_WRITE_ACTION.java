package com.ms.imapi2;

import com4j.*;

/**
 * <p>
 * Current action when writing to the CD in Disc-At-Once (RAW) mode
 * </p>
 */
public enum IMAPI_FORMAT2_RAW_CD_WRITE_ACTION {
  /**
   * <p>
   * Unknown
   * </p>
   * <p>
   * The value of this constant is 0
   * </p>
   */
  IMAPI_FORMAT2_RAW_CD_WRITE_ACTION_UNKNOWN, // 0
  /**
   * <p>
   * Preparing to write media
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  IMAPI_FORMAT2_RAW_CD_WRITE_ACTION_PREPARING, // 1
  /**
   * <p>
   * writing the media
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  IMAPI_FORMAT2_RAW_CD_WRITE_ACTION_WRITING, // 2
  /**
   * <p>
   * finishing writing the media
   * </p>
   * <p>
   * The value of this constant is 3
   * </p>
   */
  IMAPI_FORMAT2_RAW_CD_WRITE_ACTION_FINISHING, // 3
}
