package com.ms.imapi2  ;

import com4j.*;

/**
 * <p>
 * Boot platform type
 * </p>
 */
public enum PlatformId implements ComEnum {
  /**
   * <p>
   * The value of this constant is 0
   * </p>
   */
  PlatformX86(0),
  /**
   * <p>
   * The value of this constant is 1
   * </p>
   */
  PlatformPowerPC(1),
  /**
   * <p>
   * The value of this constant is 2
   * </p>
   */
  PlatformMac(2),
  /**
   * <p>
   * The value of this constant is 239
   * </p>
   */
  PlatformEFI(239),
  ;

  private final int value;
  PlatformId(int value) { this.value=value; }
  public int comEnumValue() { return value; }
}
