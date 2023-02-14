package com.ms.imapi2;

import com4j.*;

/**
 * CD Disc-At-Once RAW Writer
 */
@IID("{27354155-8F64-5B0F-8F00-5D77AFBE261E}")
public interface IDiscFormat2RawCD extends IDiscFormat2 {
  // Methods:
  /**
   * <p>
   * Locks the current media for use by this writer.
   * </p>
   */

  @DISPID(512) //= 0x200. The runtime will prefer the VTID if present
  @VTID(12)
  void prepareMedia();


  /**
   * <p>
   * Writes a RAW image that starts at 95:00:00 (MSF) to the currently inserted blank CD media.
   * </p>
   * @param data Mandatory com.ms.imapi2.IStream parameter.
   */

  @DISPID(513) //= 0x201. The runtime will prefer the VTID if present
  @VTID(13)
  void writeMedia(
    IStream data);


  /**
   * <p>
   * Writes a RAW image to the currently inserted blank CD media.  A stream starting at 95:00:00 (-5 minutes) would use 5*60*75 + 150 sectors pregap == 22,650 for the number of sectors
   * </p>
   * @param data Mandatory com.ms.imapi2.IStream parameter.
   * @param streamLeadInSectors Mandatory int parameter.
   */

  @DISPID(514) //= 0x202. The runtime will prefer the VTID if present
  @VTID(14)
  void writeMedia2(
    IStream data,
    int streamLeadInSectors);


  /**
   * <p>
   * Cancels the current write.
   * </p>
   */

  @DISPID(515) //= 0x203. The runtime will prefer the VTID if present
  @VTID(15)
  void cancelWrite();


  /**
   * <p>
   * Finishes use of the locked media.
   * </p>
   */

  @DISPID(516) //= 0x204. The runtime will prefer the VTID if present
  @VTID(16)
  void releaseMedia();


  /**
   * <p>
   * Sets the write speed (in sectors per second) of the attached disc recorder
   * </p>
   * @param requestedSectorsPerSecond Mandatory int parameter.
   * @param rotationTypeIsPureCAV Mandatory boolean parameter.
   */

  @DISPID(517) //= 0x205. The runtime will prefer the VTID if present
  @VTID(17)
  void setWriteSpeed(
    int requestedSectorsPerSecond,
    boolean rotationTypeIsPureCAV);


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
  @VTID(18)
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
  @VTID(19)
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

  @DISPID(258) //= 0x102. The runtime will prefer the VTID if present
  @VTID(20)
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

  @DISPID(258) //= 0x102. The runtime will prefer the VTID if present
  @VTID(21)
  boolean bufferUnderrunFreeDisabled();


  /**
   * <p>
   * The first sector of the next session.  May be negative for blank media.
   * </p>
   * <p>
   * Getter method for the COM property "StartOfNextSession"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(259) //= 0x103. The runtime will prefer the VTID if present
  @VTID(22)
  int startOfNextSession();


  /**
   * <p>
   * The last possible start for the leadout area.  Can be used to calculate available space on media.
   * </p>
   * <p>
   * Getter method for the COM property "LastPossibleStartOfLeadout"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(260) //= 0x104. The runtime will prefer the VTID if present
  @VTID(23)
  int lastPossibleStartOfLeadout();


  /**
   * <p>
   * Get the current physical media type.
   * </p>
   * <p>
   * Getter method for the COM property "CurrentPhysicalMediaType"
   * </p>
   * @return  Returns a value of type com.ms.imapi2.IMAPI_MEDIA_PHYSICAL_TYPE
   */

  @DISPID(261) //= 0x105. The runtime will prefer the VTID if present
  @VTID(24)
  IMAPI_MEDIA_PHYSICAL_TYPE currentPhysicalMediaType();


  /**
   * <p>
   * Supported data sector types for the current recorder
   * </p>
   * <p>
   * Getter method for the COM property "SupportedSectorTypes"
   * </p>
   * @return  Returns a value of type java.lang.Object[]
   */

  @DISPID(264) //= 0x108. The runtime will prefer the VTID if present
  @VTID(25)
  Object[] supportedSectorTypes();


  /**
   * <p>
   * Requested data sector to use during write of the stream
   * </p>
   * <p>
   * Setter method for the COM property "RequestedSectorType"
   * </p>
   * @param value Mandatory com.ms.imapi2.IMAPI_FORMAT2_RAW_CD_DATA_SECTOR_TYPE parameter.
   */

  @DISPID(265) //= 0x109. The runtime will prefer the VTID if present
  @VTID(26)
  void requestedSectorType(
    IMAPI_FORMAT2_RAW_CD_DATA_SECTOR_TYPE value);


  /**
   * <p>
   * Requested data sector to use during write of the stream
   * </p>
   * <p>
   * Getter method for the COM property "RequestedSectorType"
   * </p>
   * @return  Returns a value of type com.ms.imapi2.IMAPI_FORMAT2_RAW_CD_DATA_SECTOR_TYPE
   */

  @DISPID(265) //= 0x109. The runtime will prefer the VTID if present
  @VTID(27)
  IMAPI_FORMAT2_RAW_CD_DATA_SECTOR_TYPE requestedSectorType();


  /**
   * <p>
   * The friendly name of the client (used to determine recorder reservation conflicts).
   * </p>
   * <p>
   * Setter method for the COM property "ClientName"
   * </p>
   * @param value Mandatory java.lang.String parameter.
   */

  @DISPID(266) //= 0x10a. The runtime will prefer the VTID if present
  @VTID(28)
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

  @DISPID(266) //= 0x10a. The runtime will prefer the VTID if present
  @VTID(29)
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

  @DISPID(267) //= 0x10b. The runtime will prefer the VTID if present
  @VTID(30)
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

  @DISPID(268) //= 0x10c. The runtime will prefer the VTID if present
  @VTID(31)
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

  @DISPID(269) //= 0x10d. The runtime will prefer the VTID if present
  @VTID(32)
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

  @DISPID(270) //= 0x10e. The runtime will prefer the VTID if present
  @VTID(33)
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

  @DISPID(271) //= 0x10f. The runtime will prefer the VTID if present
  @VTID(34)
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

  @DISPID(272) //= 0x110. The runtime will prefer the VTID if present
  @VTID(35)
  Object[] supportedWriteSpeedDescriptors();


  // Properties:
}
