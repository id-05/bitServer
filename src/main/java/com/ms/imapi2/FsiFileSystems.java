package com.ms.imapi2  ;

import com4j.*;

/**
 * <p>
 * Type of file system
 * </p>
 */
public enum FsiFileSystems implements ComEnum {
  /**
   * <p>
   * The value of this constant is 0
   * </p>
   */
  FsiFileSystemNone(0),
  /**
   * <p>
   * The value of this constant is 1
   * </p>
   */
  FsiFileSystemISO9660(1),
  /**
   * <p>
   * The value of this constant is 2
   * </p>
   */
  FsiFileSystemJoliet(2),
  /**
   * <p>
   * The value of this constant is 4
   * </p>
   */
  FsiFileSystemUDF(4),
  /**
   * <p>
   * The value of this constant is 1073741824
   * </p>
   */
  FsiFileSystemUnknown(1073741824),
  ;

  private final int value;
  FsiFileSystems(int value) { this.value=value; }
  public int comEnumValue() { return value; }
}
