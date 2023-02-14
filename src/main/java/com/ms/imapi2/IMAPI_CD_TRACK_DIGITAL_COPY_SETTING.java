package com.ms.imapi2;

import com4j.*;

/**
 * <p>
 * The audio track digital copy setting
 * </p>
 */
public enum IMAPI_CD_TRACK_DIGITAL_COPY_SETTING {
  /**
   * <p>
   * Digital Copies Allowed
   * </p>
   * <p>
   * The value of this constant is 0
   * </p>
   */
  IMAPI_CD_TRACK_DIGITAL_COPY_PERMITTED, // 0
  /**
   * <p>
   * Digital Copies Not Allowed
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  IMAPI_CD_TRACK_DIGITAL_COPY_PROHIBITED, // 1
  /**
   * <p>
   * Copy of an Original Copy Prohibited Track
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  IMAPI_CD_TRACK_DIGITAL_COPY_SCMS, // 2
}
