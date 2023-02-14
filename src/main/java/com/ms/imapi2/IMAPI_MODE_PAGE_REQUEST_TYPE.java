package com.ms.imapi2;

import com4j.*;

/**
 * <p>
 * Mode page data type to request
 * </p>
 */
public enum IMAPI_MODE_PAGE_REQUEST_TYPE {
  /**
   * <p>
   * Request the current mode page
   * </p>
   * <p>
   * The value of this constant is 0
   * </p>
   */
  IMAPI_MODE_PAGE_REQUEST_TYPE_CURRENT_VALUES, // 0
  /**
   * <p>
   * Request the changeable bitmask for a mode page
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  IMAPI_MODE_PAGE_REQUEST_TYPE_CHANGEABLE_VALUES, // 1
  /**
   * <p>
   * Request the default mode page
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  IMAPI_MODE_PAGE_REQUEST_TYPE_DEFAULT_VALUES, // 2
  /**
   * <p>
   * Request the saved mode page (if supported by device)
   * </p>
   * <p>
   * The value of this constant is 3
   * </p>
   */
  IMAPI_MODE_PAGE_REQUEST_TYPE_SAVED_VALUES, // 3
}
