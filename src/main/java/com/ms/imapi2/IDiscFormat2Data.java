package com.ms.imapi2;

import com4j.*;

/**
 * Data Writer
 */
@IID("{27354153-9F64-5B0F-8F00-5D77AFBE261E}")
public interface IDiscFormat2Data extends IDiscFormat2 {
  // Methods:
  /**
   * <p>
   * The disc recorder to use
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
   * The disc recorder to use
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
   * Buffer Underrun Free recording should be disabled
   * </p>
   * <p>
   * Setter method for the COM property "BufferUnderrunFreeDisabled"
   * </p>
   * @param value Mandatory boolean parameter.
   */

  @DISPID(257) //= 0x101. The runtime will prefer the VTID if present
  @VTID(14)
  void bufferUnderrunFreeDisabled(
    boolean value);


  /**
   * <p>
   * Buffer Underrun Free recording should be disabled
   * </p>
   * <p>
   * Getter method for the COM property "BufferUnderrunFreeDisabled"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(257) //= 0x101. The runtime will prefer the VTID if present
  @VTID(15)
  boolean bufferUnderrunFreeDisabled();


  /**
   * <p>
   * Postgap is included in image
   * </p>
   * <p>
   * Setter method for the COM property "PostgapAlreadyInImage"
   * </p>
   * @param value Mandatory boolean parameter.
   */

  @DISPID(260) //= 0x104. The runtime will prefer the VTID if present
  @VTID(16)
  void postgapAlreadyInImage(
    boolean value);


  /**
   * <p>
   * Postgap is included in image
   * </p>
   * <p>
   * Getter method for the COM property "PostgapAlreadyInImage"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(260) //= 0x104. The runtime will prefer the VTID if present
  @VTID(17)
  boolean postgapAlreadyInImage();


  /**
   * <p>
   * The state (usability) of the current media
   * </p>
   * <p>
   * Getter method for the COM property "CurrentMediaStatus"
   * </p>
   * @return  Returns a value of type com.ms.imapi2.IMAPI_FORMAT2_DATA_MEDIA_STATE
   */

  @DISPID(262) //= 0x106. The runtime will prefer the VTID if present
  @VTID(18)
  IMAPI_FORMAT2_DATA_MEDIA_STATE currentMediaStatus();


  /**
   * <p>
   * The write protection state of the current media.
   * </p>
   * <p>
   * Getter method for the COM property "WriteProtectStatus"
   * </p>
   * @return  Returns a value of type com.ms.imapi2.IMAPI_MEDIA_WRITE_PROTECT_STATE
   */

  @DISPID(263) //= 0x107. The runtime will prefer the VTID if present
  @VTID(19)
  IMAPI_MEDIA_WRITE_PROTECT_STATE writeProtectStatus();


  /**
   * <p>
   * Total sectors available on the media (used + free).
   * </p>
   * <p>
   * Getter method for the COM property "TotalSectorsOnMedia"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(264) //= 0x108. The runtime will prefer the VTID if present
  @VTID(20)
  int totalSectorsOnMedia();


  /**
   * <p>
   * Free sectors available on the media.
   * </p>
   * <p>
   * Getter method for the COM property "FreeSectorsOnMedia"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(265) //= 0x109. The runtime will prefer the VTID if present
  @VTID(21)
  int freeSectorsOnMedia();


  /**
   * <p>
   * Next writable address on the media (also used sectors).
   * </p>
   * <p>
   * Getter method for the COM property "NextWritableAddress"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(266) //= 0x10a. The runtime will prefer the VTID if present
  @VTID(22)
  int nextWritableAddress();


  /**
   * <p>
   * The first sector in the previous session on the media.
   * </p>
   * <p>
   * Getter method for the COM property "StartAddressOfPreviousSession"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(267) //= 0x10b. The runtime will prefer the VTID if present
  @VTID(23)
  int startAddressOfPreviousSession();


  /**
   * <p>
   * The last sector in the previous session on the media.
   * </p>
   * <p>
   * Getter method for the COM property "LastWrittenAddressOfPreviousSession"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(268) //= 0x10c. The runtime will prefer the VTID if present
  @VTID(24)
  int lastWrittenAddressOfPreviousSession();


  /**
   * <p>
   * Prevent further additions to the file system
   * </p>
   * <p>
   * Setter method for the COM property "ForceMediaToBeClosed"
   * </p>
   * @param value Mandatory boolean parameter.
   */

  @DISPID(269) //= 0x10d. The runtime will prefer the VTID if present
  @VTID(25)
  void forceMediaToBeClosed(
    boolean value);


  /**
   * <p>
   * Prevent further additions to the file system
   * </p>
   * <p>
   * Getter method for the COM property "ForceMediaToBeClosed"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(269) //= 0x10d. The runtime will prefer the VTID if present
  @VTID(26)
  boolean forceMediaToBeClosed();


  /**
   * <p>
   * Default is to maximize compatibility with DVD-ROM.  May be disabled to reduce time to finish writing the disc or increase usable space on the media for later writing.
   * </p>
   * <p>
   * Setter method for the COM property "DisableConsumerDvdCompatibilityMode"
   * </p>
   * @param value Mandatory boolean parameter.
   */

  @DISPID(270) //= 0x10e. The runtime will prefer the VTID if present
  @VTID(27)
  void disableConsumerDvdCompatibilityMode(
    boolean value);


  /**
   * <p>
   * Default is to maximize compatibility with DVD-ROM.  May be disabled to reduce time to finish writing the disc or increase usable space on the media for later writing.
   * </p>
   * <p>
   * Getter method for the COM property "DisableConsumerDvdCompatibilityMode"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(270) //= 0x10e. The runtime will prefer the VTID if present
  @VTID(28)
  boolean disableConsumerDvdCompatibilityMode();


  /**
   * <p>
   * Get the current physical media type.
   * </p>
   * <p>
   * Getter method for the COM property "CurrentPhysicalMediaType"
   * </p>
   * @return  Returns a value of type com.ms.imapi2.IMAPI_MEDIA_PHYSICAL_TYPE
   */

  @DISPID(271) //= 0x10f. The runtime will prefer the VTID if present
  @VTID(29)
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

  @DISPID(272) //= 0x110. The runtime will prefer the VTID if present
  @VTID(30)
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

  @DISPID(272) //= 0x110. The runtime will prefer the VTID if present
  @VTID(31)
  String clientName();


  /**
   * <p>
   * The last requested write speed.
   * </p>
   * <p>
   * Getter method for the COM property "RequestedWriteSpeed"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(273) //= 0x111. The runtime will prefer the VTID if present
  @VTID(32)
  int requestedWriteSpeed();


  /**
   * <p>
   * The last requested rotation type.
   * </p>
   * <p>
   * Getter method for the COM property "RequestedRotationTypeIsPureCAV"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(274) //= 0x112. The runtime will prefer the VTID if present
  @VTID(33)
  boolean requestedRotationTypeIsPureCAV();


  /**
   * <p>
   * The drive's current write speed.
   * </p>
   * <p>
   * Getter method for the COM property "CurrentWriteSpeed"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(275) //= 0x113. The runtime will prefer the VTID if present
  @VTID(34)
  int currentWriteSpeed();


  /**
   * <p>
   * The drive's current rotation type.
   * </p>
   * <p>
   * Getter method for the COM property "CurrentRotationTypeIsPureCAV"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(276) //= 0x114. The runtime will prefer the VTID if present
  @VTID(35)
  boolean currentRotationTypeIsPureCAV();


  /**
   * <p>
   * Gets an array of the write speeds supported for the attached disc recorder and current media
   * </p>
   * <p>
   * Getter method for the COM property "SupportedWriteSpeeds"
   * </p>
   * @return  Returns a value of type java.lang.Object[]
   */

  @DISPID(277) //= 0x115. The runtime will prefer the VTID if present
  @VTID(36)
  Object[] supportedWriteSpeeds();


  /**
   * <p>
   * Gets an array of the detailed write configurations supported for the attached disc recorder and current media
   * </p>
   * <p>
   * Getter method for the COM property "SupportedWriteSpeedDescriptors"
   * </p>
   * @return  Returns a value of type java.lang.Object[]
   */

  @DISPID(278) //= 0x116. The runtime will prefer the VTID if present
  @VTID(37)
  Object[] supportedWriteSpeedDescriptors();


  /**
   * <p>
   * Forces the Datawriter to overwrite the disc on overwritable media types
   * </p>
   * <p>
   * Setter method for the COM property "ForceOverwrite"
   * </p>
   * @param value Mandatory boolean parameter.
   */

  @DISPID(279) //= 0x117. The runtime will prefer the VTID if present
  @VTID(38)
  void forceOverwrite(
    boolean value);


  /**
   * <p>
   * Forces the Datawriter to overwrite the disc on overwritable media types
   * </p>
   * <p>
   * Getter method for the COM property "ForceOverwrite"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(279) //= 0x117. The runtime will prefer the VTID if present
  @VTID(39)
  boolean forceOverwrite();


  /**
   * <p>
   * Returns the array of available multi-session interfaces. The array shall not be empty
   * </p>
   * <p>
   * Getter method for the COM property "MultisessionInterfaces"
   * </p>
   * @return  Returns a value of type java.lang.Object[]
   */

  @DISPID(280) //= 0x118. The runtime will prefer the VTID if present
  @VTID(40)
  Object[] multisessionInterfaces();


  /**
   * <p>
   * Writes all the data provided in the IStream to the device
   * </p>
   * @param data Mandatory com.ms.imapi2.IStream parameter.
   */

  @DISPID(512) //= 0x200. The runtime will prefer the VTID if present
  @VTID(41)
  void write(
    IStream data);


  /**
   * <p>
   * Cancels the current write operation
   * </p>
   */

  @DISPID(513) //= 0x201. The runtime will prefer the VTID if present
  @VTID(42)
  void cancelWrite();


  /**
   * <p>
   * Sets the write speed (in sectors per second) of the attached disc recorder
   * </p>
   * @param requestedSectorsPerSecond Mandatory int parameter.
   * @param rotationTypeIsPureCAV Mandatory boolean parameter.
   */

  @DISPID(514) //= 0x202. The runtime will prefer the VTID if present
  @VTID(43)
  void setWriteSpeed(
    int requestedSectorsPerSecond,
    boolean rotationTypeIsPureCAV);


  // Properties:
}
