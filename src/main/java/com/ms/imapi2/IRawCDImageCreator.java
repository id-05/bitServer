package com.ms.imapi2;

import com4j.*;

/**
 * CD Raw CD (Disc-at-Once) Image Creator
 */
@IID("{25983550-9D65-49CE-B335-40630D901227}")
public interface IRawCDImageCreator extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Creates the result stream.
   * </p>
   * @return  Returns a value of type com.ms.imapi2.IStream
   */

  @DISPID(512) //= 0x200. The runtime will prefer the VTID if present
  @VTID(7)
  IStream createResultImage();


  /**
   * <p>
   * Adds a track to the media (defaults to audio, always 2352 bytes/sector).
   * </p>
   * @param dataType Mandatory com.ms.imapi2.IMAPI_CD_SECTOR_TYPE parameter.
   * @param data Mandatory com.ms.imapi2.IStream parameter.
   * @return  Returns a value of type int
   */

  @DISPID(513) //= 0x201. The runtime will prefer the VTID if present
  @VTID(8)
  int addTrack(
    IMAPI_CD_SECTOR_TYPE dataType,
    IStream data);


  /**
   * <p>
   * Adds a special pregap to the first track, and implies an audio CD
   * </p>
   * @param data Mandatory com.ms.imapi2.IStream parameter.
   */

  @DISPID(514) //= 0x202. The runtime will prefer the VTID if present
  @VTID(9)
  void addSpecialPregap(
    IStream data);


  /**
   * <p>
   * Adds an R-W subcode generation object to supply R-W subcode (i.e. CD-Text or CD-G).
   * </p>
   * @param subcode Mandatory com.ms.imapi2.IStream parameter.
   */

  @DISPID(515) //= 0x203. The runtime will prefer the VTID if present
  @VTID(10)
  void addSubcodeRWGenerator(
    IStream subcode);


  /**
   * <p>
   * Setter method for the COM property "ResultingImageType"
   * </p>
   * @param value Mandatory com.ms.imapi2.IMAPI_FORMAT2_RAW_CD_DATA_SECTOR_TYPE parameter.
   */

  @DISPID(256) //= 0x100. The runtime will prefer the VTID if present
  @VTID(11)
  void resultingImageType(
    IMAPI_FORMAT2_RAW_CD_DATA_SECTOR_TYPE value);


  /**
   * <p>
   * Getter method for the COM property "ResultingImageType"
   * </p>
   * @return  Returns a value of type com.ms.imapi2.IMAPI_FORMAT2_RAW_CD_DATA_SECTOR_TYPE
   */

  @DISPID(256) //= 0x100. The runtime will prefer the VTID if present
  @VTID(12)
  IMAPI_FORMAT2_RAW_CD_DATA_SECTOR_TYPE resultingImageType();


  /**
   * <p>
   * Equal to (final user LBA+1), defines minimum disc size image can be written to.
   * </p>
   * <p>
   * Getter method for the COM property "StartOfLeadout"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(257) //= 0x101. The runtime will prefer the VTID if present
  @VTID(13)
  int startOfLeadout();


  /**
   * <p>
   * Setter method for the COM property "StartOfLeadoutLimit"
   * </p>
   * @param value Mandatory int parameter.
   */

  @DISPID(258) //= 0x102. The runtime will prefer the VTID if present
  @VTID(14)
  void startOfLeadoutLimit(
    int value);


  /**
   * <p>
   * Getter method for the COM property "StartOfLeadoutLimit"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(258) //= 0x102. The runtime will prefer the VTID if present
  @VTID(15)
  int startOfLeadoutLimit();


  /**
   * <p>
   * Disables gapless recording of consecutive audio tracks.
   * </p>
   * <p>
   * Setter method for the COM property "DisableGaplessAudio"
   * </p>
   * @param value Mandatory boolean parameter.
   */

  @DISPID(259) //= 0x103. The runtime will prefer the VTID if present
  @VTID(16)
  void disableGaplessAudio(
    boolean value);


  /**
   * <p>
   * Disables gapless recording of consecutive audio tracks.
   * </p>
   * <p>
   * Getter method for the COM property "DisableGaplessAudio"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(259) //= 0x103. The runtime will prefer the VTID if present
  @VTID(17)
  boolean disableGaplessAudio();


  /**
   * <p>
   * The Media Catalog Number for the CD image
   * </p>
   * <p>
   * Setter method for the COM property "MediaCatalogNumber"
   * </p>
   * @param value Mandatory java.lang.String parameter.
   */

  @DISPID(260) //= 0x104. The runtime will prefer the VTID if present
  @VTID(18)
  void mediaCatalogNumber(
    String value);


  /**
   * <p>
   * The Media Catalog Number for the CD image
   * </p>
   * <p>
   * Getter method for the COM property "MediaCatalogNumber"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(260) //= 0x104. The runtime will prefer the VTID if present
  @VTID(19)
  String mediaCatalogNumber();


  /**
   * <p>
   * The starting track number (only for pure audio CDs)
   * </p>
   * <p>
   * Setter method for the COM property "StartingTrackNumber"
   * </p>
   * @param value Mandatory int parameter.
   */

  @DISPID(261) //= 0x105. The runtime will prefer the VTID if present
  @VTID(20)
  void startingTrackNumber(
    int value);


  /**
   * <p>
   * The starting track number (only for pure audio CDs)
   * </p>
   * <p>
   * Getter method for the COM property "StartingTrackNumber"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(261) //= 0x105. The runtime will prefer the VTID if present
  @VTID(21)
  int startingTrackNumber();


  /**
   * <p>
   * Track-specific information
   * </p>
   * <p>
   * Getter method for the COM property "TrackInfo"
   * </p>
   * @param trackIndex Mandatory int parameter.
   * @return  Returns a value of type com.ms.imapi2.IRawCDImageTrackInfo
   */

  @DISPID(262) //= 0x106. The runtime will prefer the VTID if present
  @VTID(22)
  IRawCDImageTrackInfo trackInfo(
    int trackIndex);


  /**
   * <p>
   * Getter method for the COM property "NumberOfExistingTracks"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(263) //= 0x107. The runtime will prefer the VTID if present
  @VTID(23)
  int numberOfExistingTracks();


  /**
   * <p>
   * Getter method for the COM property "LastUsedUserSectorInImage"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(264) //= 0x108. The runtime will prefer the VTID if present
  @VTID(24)
  int lastUsedUserSectorInImage();


  /**
   * <p>
   * Getter method for the COM property "ExpectedTableOfContents"
   * </p>
   * @return  Returns a value of type java.lang.Object[]
   */

  @DISPID(265) //= 0x109. The runtime will prefer the VTID if present
  @VTID(25)
  Object[] expectedTableOfContents();


  // Properties:
}
