package com.ms.imapi2;

import com4j.*;

/**
 * <p>
 * Address type provided for reading track information
 * </p>
 */
public enum IMAPI_READ_TRACK_ADDRESS_TYPE {
  /**
   * <p>
   * Read track information by providing an LBA in the track
   * </p>
   * <p>
   * The value of this constant is 0
   * </p>
   */
  IMAPI_READ_TRACK_ADDRESS_TYPE_LBA, // 0
  /**
   * <p>
   * Read track information by providing the track number
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  IMAPI_READ_TRACK_ADDRESS_TYPE_TRACK, // 1
  /**
   * <p>
   * Read track information for the first track in the provided session number
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  IMAPI_READ_TRACK_ADDRESS_TYPE_SESSION, // 2
}
