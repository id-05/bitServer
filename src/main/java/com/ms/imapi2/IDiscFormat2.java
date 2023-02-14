package com.ms.imapi2;

import com4j.*;

/**
 * Common Disc Format (writer) Operations
 */
@IID("{27354152-8F64-5B0F-8F00-5D77AFBE261E}")
public interface IDiscFormat2 extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Determines if the recorder object supports the given format
   * </p>
   * @param recorder Mandatory com.ms.imapi2.IDiscRecorder2 parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(2048) //= 0x800. The runtime will prefer the VTID if present
  @VTID(7)
  boolean isRecorderSupported(
    IDiscRecorder2 recorder);


  /**
   * <p>
   * Determines if the current media in a supported recorder object supports the given format
   * </p>
   * @param recorder Mandatory com.ms.imapi2.IDiscRecorder2 parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(2049) //= 0x801. The runtime will prefer the VTID if present
  @VTID(8)
  boolean isCurrentMediaSupported(
    IDiscRecorder2 recorder);


  /**
   * <p>
   * Determines if the current media is reported as physically blank by the drive
   * </p>
   * <p>
   * Getter method for the COM property "MediaPhysicallyBlank"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(1792) //= 0x700. The runtime will prefer the VTID if present
  @VTID(9)
  boolean mediaPhysicallyBlank();


  /**
   * <p>
   * Attempts to determine if the media is blank using heuristics (mainly for DVD+RW and DVD-RAM media)
   * </p>
   * <p>
   * Getter method for the COM property "MediaHeuristicallyBlank"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(1793) //= 0x701. The runtime will prefer the VTID if present
  @VTID(10)
  boolean mediaHeuristicallyBlank();


  /**
   * <p>
   * Supported media types
   * </p>
   * <p>
   * Getter method for the COM property "SupportedMediaTypes"
   * </p>
   * @return  Returns a value of type java.lang.Object[]
   */

  @DISPID(1794) //= 0x702. The runtime will prefer the VTID if present
  @VTID(11)
  Object[] supportedMediaTypes();


  // Properties:
}
