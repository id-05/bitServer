package com.ms.imapi2  ;

import com4j.*;

/**
 * Boot options
 */
@IID("{2C941FD4-975B-59BE-A960-9A2A262853A5}")
public interface IBootOptions extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Get boot image data stream
   * </p>
   * <p>
   * Getter method for the COM property "BootImage"
   * </p>
   * @return  Returns a value of type IStream
   */

  @DISPID(1) //= 0x1. The runtime will prefer the VTID if present
  @VTID(7)
  IStream bootImage();


  /**
   * <p>
   * Get boot manufacturer
   * </p>
   * <p>
   * Getter method for the COM property "Manufacturer"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(2) //= 0x2. The runtime will prefer the VTID if present
  @VTID(8)
  java.lang.String manufacturer();


  /**
   * <p>
   * Get boot manufacturer
   * </p>
   * <p>
   * Setter method for the COM property "Manufacturer"
   * </p>
   * @param pVal Mandatory java.lang.String parameter.
   */

  @DISPID(2) //= 0x2. The runtime will prefer the VTID if present
  @VTID(9)
  void manufacturer(
    java.lang.String pVal);


  /**
   * <p>
   * Get boot platform identifier
   * </p>
   * <p>
   * Getter method for the COM property "PlatformId"
   * </p>
   * @return  Returns a value of type PlatformId
   */

  @DISPID(3) //= 0x3. The runtime will prefer the VTID if present
  @VTID(10)
  PlatformId platformId();


  /**
   * <p>
   * Get boot platform identifier
   * </p>
   * <p>
   * Setter method for the COM property "PlatformId"
   * </p>
   * @param pVal Mandatory PlatformId parameter.
   */

  @DISPID(3) //= 0x3. The runtime will prefer the VTID if present
  @VTID(11)
  void platformId(
    PlatformId pVal);


  /**
   * <p>
   * Get boot emulation type
   * </p>
   * <p>
   * Getter method for the COM property "Emulation"
   * </p>
   * @return  Returns a value of type EmulationType
   */

  @DISPID(4) //= 0x4. The runtime will prefer the VTID if present
  @VTID(12)
  EmulationType emulation();


  /**
   * <p>
   * Get boot emulation type
   * </p>
   * <p>
   * Setter method for the COM property "Emulation"
   * </p>
   * @param pVal Mandatory EmulationType parameter.
   */

  @DISPID(4) //= 0x4. The runtime will prefer the VTID if present
  @VTID(13)
  void emulation(
    EmulationType pVal);


  /**
   * <p>
   * Get boot image size
   * </p>
   * <p>
   * Getter method for the COM property "ImageSize"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(5) //= 0x5. The runtime will prefer the VTID if present
  @VTID(14)
  int imageSize();


  /**
   * <p>
   * Set the boot image data stream, emulation type, and image size
   * </p>
   * @param newVal Mandatory IStream parameter.
   */

  @DISPID(20) //= 0x14. The runtime will prefer the VTID if present
  @VTID(15)
  void assignBootImage(
    IStream newVal);


  // Properties:
}
