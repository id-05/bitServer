package com.ms.imapi2;

import com4j.*;

/**
 * <p>
 * The current action being performed for the data write
 * </p>
 */
public enum IMAPI_FORMAT2_DATA_WRITE_ACTION {
  /**
   * <p>
   * Validating the current media is supported
   * </p>
   * <p>
   * The value of this constant is 0
   * </p>
   */
  IMAPI_FORMAT2_DATA_WRITE_ACTION_VALIDATING_MEDIA, // 0
  /**
   * <p>
   * Formatting media, when required (i.e. DVD+RW)
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  IMAPI_FORMAT2_DATA_WRITE_ACTION_FORMATTING_MEDIA, // 1
  /**
   * <p>
   * Initializing the drive
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  IMAPI_FORMAT2_DATA_WRITE_ACTION_INITIALIZING_HARDWARE, // 2
  /**
   * <p>
   * Calibrating the drive's write power
   * </p>
   * <p>
   * The value of this constant is 3
   * </p>
   */
  IMAPI_FORMAT2_DATA_WRITE_ACTION_CALIBRATING_POWER, // 3
  /**
   * <p>
   * Writing user data to the media
   * </p>
   * <p>
   * The value of this constant is 4
   * </p>
   */
  IMAPI_FORMAT2_DATA_WRITE_ACTION_WRITING_DATA, // 4
  /**
   * <p>
   * Finalizing the media (synchronizing the cache, closing tracks/sessions, etc.
   * </p>
   * <p>
   * The value of this constant is 5
   * </p>
   */
  IMAPI_FORMAT2_DATA_WRITE_ACTION_FINALIZATION, // 5
  /**
   * <p>
   * The write process has completed
   * </p>
   * <p>
   * The value of this constant is 6
   * </p>
   */
  IMAPI_FORMAT2_DATA_WRITE_ACTION_COMPLETED, // 6
  /**
   * <p>
   * Performing requested burn verification
   * </p>
   * <p>
   * The value of this constant is 7
   * </p>
   */
  IMAPI_FORMAT2_DATA_WRITE_ACTION_VERIFYING, // 7
}
