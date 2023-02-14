package com.ms.imapi2;

import com4j.*;

/**
 * <p>
 * Detected write protection type of the media
 * </p>
 */
public enum IMAPI_MEDIA_WRITE_PROTECT_STATE implements ComEnum {
  /**
   * <p>
   * Software Write Protected Until Powerdown
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  IMAPI_WRITEPROTECTED_UNTIL_POWERDOWN(1),
  /**
   * <p>
   * Cartridge Write Protect
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  IMAPI_WRITEPROTECTED_BY_CARTRIDGE(2),
  /**
   * <p>
   * Media Specific Write Inhibit
   * </p>
   * <p>
   * The value of this constant is 4
   * </p>
   */
  IMAPI_WRITEPROTECTED_BY_MEDIA_SPECIFIC_REASON(4),
  /**
   * <p>
   * Persistent Write Protect
   * </p>
   * <p>
   * The value of this constant is 8
   * </p>
   */
  IMAPI_WRITEPROTECTED_BY_SOFTWARE_WRITE_PROTECT(8),
  /**
   * <p>
   * Write Inhibit by Disc Control Block
   * </p>
   * <p>
   * The value of this constant is 16
   * </p>
   */
  IMAPI_WRITEPROTECTED_BY_DISC_CONTROL_BLOCK(16),
  /**
   * <p>
   * Read-only media
   * </p>
   * <p>
   * The value of this constant is 16384
   * </p>
   */
  IMAPI_WRITEPROTECTED_READ_ONLY_MEDIA(16384),
  ;

  private final int value;
  IMAPI_MEDIA_WRITE_PROTECT_STATE(int value) { this.value=value; }
  public int comEnumValue() { return value; }
}
