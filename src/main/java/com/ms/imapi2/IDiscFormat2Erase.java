package com.ms.imapi2;

import com4j.*;

/**
 * Optical media erase
 */
@IID("{27354156-8F64-5B0F-8F00-5D77AFBE261E}")
public interface IDiscFormat2Erase extends IDiscFormat2 {
  // Methods:
  /**
   * <p>
   * Sets the recorder object to use for an erase operation
   * </p>
   * <p>
   * Setter method for the COM property "Recorder"
   * </p>
   * @param value Mandatory com.ms.imapi2.IDiscRecorder2 parameter.
   */

  @DISPID(256) //= 0x100. The runtime will prefer the VTID if present
  @VTID(12)
  void recorder(
    IDiscRecorder2 value);


  /**
   * <p>
   * Sets the recorder object to use for an erase operation
   * </p>
   * <p>
   * Getter method for the COM property "Recorder"
   * </p>
   * @return  Returns a value of type com.ms.imapi2.IDiscRecorder2
   */

  @DISPID(256) //= 0x100. The runtime will prefer the VTID if present
  @VTID(13)
  IDiscRecorder2 recorder();


  /**
   * <p>
   * Gets the recorder object to use for an erase operation
   * </p>
   * <p>
   * Setter method for the COM property "FullErase"
   * </p>
   * @param value Mandatory boolean parameter.
   */

  @DISPID(257) //= 0x101. The runtime will prefer the VTID if present
  @VTID(14)
  void fullErase(
    boolean value);


  /**
   * <p>
   * Gets the recorder object to use for an erase operation
   * </p>
   * <p>
   * Getter method for the COM property "FullErase"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(257) //= 0x101. The runtime will prefer the VTID if present
  @VTID(15)
  boolean fullErase();


  /**
   * <p>
   * Get the current physical media type.
   * </p>
   * <p>
   * Getter method for the COM property "CurrentPhysicalMediaType"
   * </p>
   * @return  Returns a value of type com.ms.imapi2.IMAPI_MEDIA_PHYSICAL_TYPE
   */

  @DISPID(258) //= 0x102. The runtime will prefer the VTID if present
  @VTID(16)
  IMAPI_MEDIA_PHYSICAL_TYPE currentPhysicalMediaType();


  /**
   * <p>
   * The friendly name of the client (used to determine recorder reservation conflicts).
   * </p>
   * <p>
   * Setter method for the COM property "ClientName"
   * </p>
   * @param value Mandatory java.lang.String parameter.
   */

  @DISPID(259) //= 0x103. The runtime will prefer the VTID if present
  @VTID(17)
  void clientName(
    String value);


  /**
   * <p>
   * The friendly name of the client (used to determine recorder reservation conflicts).
   * </p>
   * <p>
   * Getter method for the COM property "ClientName"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(259) //= 0x103. The runtime will prefer the VTID if present
  @VTID(18)
  String clientName();


  /**
   * <p>
   * Erases the media in the active disc recorder
   * </p>
   */

  @DISPID(513) //= 0x201. The runtime will prefer the VTID if present
  @VTID(19)
  void eraseMedia();


  // Properties:
}
