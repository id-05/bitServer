package com.ms.imapi2;

import com4j.*;

/**
 * <p>
 * Current action when writing to CD in Track-at-Once mode
 * </p>
 */
public enum IMAPI_FORMAT2_TAO_WRITE_ACTION {
  /**
   * <p>
   * Unknown
   * </p>
   * <p>
   * The value of this constant is 0
   * </p>
   */
  IMAPI_FORMAT2_TAO_WRITE_ACTION_UNKNOWN, // 0
  /**
   * <p>
   * Preparing to write track
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  IMAPI_FORMAT2_TAO_WRITE_ACTION_PREPARING, // 1
  /**
   * <p>
   * writing the track
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  IMAPI_FORMAT2_TAO_WRITE_ACTION_WRITING, // 2
  /**
   * <p>
   * closing the track
   * </p>
   * <p>
   * The value of this constant is 3
   * </p>
   */
  IMAPI_FORMAT2_TAO_WRITE_ACTION_FINISHING, // 3
  /**
   * <p>
   * verifying track data
   * </p>
   * <p>
   * The value of this constant is 4
   * </p>
   */
  IMAPI_FORMAT2_TAO_WRITE_ACTION_VERIFYING, // 4
}
