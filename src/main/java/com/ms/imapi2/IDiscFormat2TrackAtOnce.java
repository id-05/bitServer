package com.ms.imapi2;

import com4j.*;

/**
 * CD Track-at-Once Audio Writer
 */
@IID("{27354154-8F64-5B0F-8F00-5D77AFBE261E}")
public interface IDiscFormat2TrackAtOnce extends IDiscFormat2 {
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
   * Immediately writes a new audio track to the locked media.
   * </p>
   * @param data Mandatory com.ms.imapi2.IStream parameter.
   */

  @DISPID(513) //= 0x201. The runtime will prefer the VTID if present
  @VTID(13)
  void addAudioTrack(
    IStream data);


  /**
   * <p>
   * Cancels the current addition of a track.
   * </p>
   */

  @DISPID(514) //= 0x202. The runtime will prefer the VTID if present
  @VTID(14)
  void cancelAddTrack();


  /**
   * <p>
   * Finishes use of the locked media.
   * </p>
   */

  @DISPID(515) //= 0x203. The runtime will prefer the VTID if present
  @VTID(15)
  void releaseMedia();


  /**
   * <p>
   * Sets the write speed (in sectors per second) of the attached disc recorder
   * </p>
   * @param requestedSectorsPerSecond Mandatory int parameter.
   * @param rotationTypeIsPureCAV Mandatory boolean parameter.
   */

  @DISPID(516) //= 0x204. The runtime will prefer the VTID if present
  @VTID(16)
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
  @VTID(17)
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
  @VTID(18)
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
  @VTID(19)
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
  @VTID(20)
  boolean bufferUnderrunFreeDisabled();


  /**
   * <p>
   * Number of tracks already written to the locked media
   * </p>
   * <p>
   * Getter method for the COM property "NumberOfExistingTracks"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(259) //= 0x103. The runtime will prefer the VTID if present
  @VTID(21)
  int numberOfExistingTracks();


  /**
   * <p>
   * Total sectors available on locked media if writing one continuous audio track
   * </p>
   * <p>
   * Getter method for the COM property "TotalSectorsOnMedia"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(260) //= 0x104. The runtime will prefer the VTID if present
  @VTID(22)
  int totalSectorsOnMedia();


  /**
   * <p>
   * Number of sectors available for adding a new track to the media
   * </p>
   * <p>
   * Getter method for the COM property "FreeSectorsOnMedia"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(261) //= 0x105. The runtime will prefer the VTID if present
  @VTID(23)
  int freeSectorsOnMedia();


  /**
   * <p>
   * Number of sectors used on the locked media, including overhead (space between tracks)
   * </p>
   * <p>
   * Getter method for the COM property "UsedSectorsOnMedia"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(262) //= 0x106. The runtime will prefer the VTID if present
  @VTID(24)
  int usedSectorsOnMedia();


  /**
   * <p>
   * Set the media to be left 'open' after writing, to allow multisession discs.
   * </p>
   * <p>
   * Setter method for the COM property "DoNotFinalizeMedia"
   * </p>
   * @param value Mandatory boolean parameter.
   */

  @DISPID(263) //= 0x107. The runtime will prefer the VTID if present
  @VTID(25)
  void doNotFinalizeMedia(
    boolean value);


  /**
   * <p>
   * Set the media to be left 'open' after writing, to allow multisession discs.
   * </p>
   * <p>
   * Getter method for the COM property "DoNotFinalizeMedia"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(263) //= 0x107. The runtime will prefer the VTID if present
  @VTID(26)
  boolean doNotFinalizeMedia();


  /**
   * <p>
   * The expected TOC if the media is closed without adding additional tracks.
   * </p>
   * <p>
   * Getter method for the COM property "ExpectedTableOfContents"
   * </p>
   * @return  Returns a value of type java.lang.Object[]
   */

  @DISPID(266) //= 0x10a. The runtime will prefer the VTID if present
  @VTID(27)
  Object[] expectedTableOfContents();


  /**
   * <p>
   * Get the current physical media type.
   * </p>
   * <p>
   * Getter method for the COM property "CurrentPhysicalMediaType"
   * </p>
   * @return  Returns a value of type com.ms.imapi2.IMAPI_MEDIA_PHYSICAL_TYPE
   */

  @DISPID(267) //= 0x10b. The runtime will prefer the VTID if present
  @VTID(28)
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

  @DISPID(270) //= 0x10e. The runtime will prefer the VTID if present
  @VTID(29)
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

  @DISPID(270) //= 0x10e. The runtime will prefer the VTID if present
  @VTID(30)
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

  @DISPID(271) //= 0x10f. The runtime will prefer the VTID if present
  @VTID(31)
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

  @DISPID(272) //= 0x110. The runtime will prefer the VTID if present
  @VTID(32)
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

  @DISPID(273) //= 0x111. The runtime will prefer the VTID if present
  @VTID(33)
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

  @DISPID(274) //= 0x112. The runtime will prefer the VTID if present
  @VTID(34)
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

  @DISPID(275) //= 0x113. The runtime will prefer the VTID if present
  @VTID(35)
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

  @DISPID(276) //= 0x114. The runtime will prefer the VTID if present
  @VTID(36)
  Object[] supportedWriteSpeedDescriptors();


  // Properties:
}
