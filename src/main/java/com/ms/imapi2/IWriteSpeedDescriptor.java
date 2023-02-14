package com.ms.imapi2;

import com4j.*;

/**
 * A single optical drive Write Speed Configuration
 */
@IID("{27354144-7F64-5B0F-8F00-5D77AFBE261E}")
public interface IWriteSpeedDescriptor extends Com4jObject {
  // Methods:
  /**
   * <p>
   * The type of media that this descriptor is valid for.
   * </p>
   * <p>
   * Getter method for the COM property "MediaType"
   * </p>
   * @return  Returns a value of type com.ms.imapi2.IMAPI_MEDIA_PHYSICAL_TYPE
   */

  @DISPID(257) //= 0x101. The runtime will prefer the VTID if present
  @VTID(7)
  IMAPI_MEDIA_PHYSICAL_TYPE mediaType();


  /**
   * <p>
   * Whether or not this descriptor represents a writing configuration that uses Pure CAV rotation control.
   * </p>
   * <p>
   * Getter method for the COM property "RotationTypeIsPureCAV"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(258) //= 0x102. The runtime will prefer the VTID if present
  @VTID(8)
  boolean rotationTypeIsPureCAV();


  /**
   * <p>
   * The maximum speed at which the media will be written in the write configuration represented by this descriptor.
   * </p>
   * <p>
   * Getter method for the COM property "WriteSpeed"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(259) //= 0x103. The runtime will prefer the VTID if present
  @VTID(9)
  int writeSpeed();


  // Properties:
}
