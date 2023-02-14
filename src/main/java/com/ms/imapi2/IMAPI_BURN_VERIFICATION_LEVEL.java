package com.ms.imapi2;

import com4j.*;

/**
 * <p>
 * Burn verification level
 * </p>
 */
public enum IMAPI_BURN_VERIFICATION_LEVEL {
  /**
   * <p>
   * No write verification
   * </p>
   * <p>
   * The value of this constant is 0
   * </p>
   */
  IMAPI_BURN_VERIFICATION_NONE, // 0
  /**
   * <p>
   * Quick write verification
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  IMAPI_BURN_VERIFICATION_QUICK, // 1
  /**
   * <p>
   * Full write verification
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  IMAPI_BURN_VERIFICATION_FULL, // 2
}
