package com.ms.imapi2;

import com4j.*;

/**
 * <p>
 * Type of the mode page data
 * </p>
 */
public enum IMAPI_MODE_PAGE_TYPE implements ComEnum {
  /**
   * <p>
   * The parameters to use for error recovery during read and write operations
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  IMAPI_MODE_PAGE_TYPE_READ_WRITE_ERROR_RECOVERY(1),
  /**
   * <p>
   * Mt. Rainier (MRW) mode page for controlling MRW-specific features
   * </p>
   * <p>
   * The value of this constant is 3
   * </p>
   */
  IMAPI_MODE_PAGE_TYPE_MRW(3),
  /**
   * <p>
   * The parameters required to setup writing to and from some legacy media types
   * </p>
   * <p>
   * The value of this constant is 5
   * </p>
   */
  IMAPI_MODE_PAGE_TYPE_WRITE_PARAMETERS(5),
  /**
   * <p>
   * The parameters to enable or disable the use of caching for read and/or write operations
   * </p>
   * <p>
   * The value of this constant is 8
   * </p>
   */
  IMAPI_MODE_PAGE_TYPE_CACHING(8),
  /**
   * <p>
   * The parameters for exception reporting mechanisms which result in specific sense codes errors when failures are predicted
   * </p>
   * <p>
   * The value of this constant is 28
   * </p>
   */
  IMAPI_MODE_PAGE_TYPE_INFORMATIONAL_EXCEPTIONS(28),
  /**
   * <p>
   * Default timeouts for commands
   * </p>
   * <p>
   * The value of this constant is 29
   * </p>
   */
  IMAPI_MODE_PAGE_TYPE_TIMEOUT_AND_PROTECT(29),
  /**
   * <p>
   * The parameters which define how long the logical unit delays before changing its power state
   * </p>
   * <p>
   * The value of this constant is 26
   * </p>
   */
  IMAPI_MODE_PAGE_TYPE_POWER_CONDITION(26),
  /**
   * <p>
   * Legacy device capabilities, superceded by the feature pages returned by GetConfiguration command
   * </p>
   * <p>
   * The value of this constant is 42
   * </p>
   */
  IMAPI_MODE_PAGE_TYPE_LEGACY_CAPABILITIES(42),
  ;

  private final int value;
  IMAPI_MODE_PAGE_TYPE(int value) { this.value=value; }
  public int comEnumValue() { return value; }
}
