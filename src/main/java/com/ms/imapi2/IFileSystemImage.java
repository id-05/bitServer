package com.ms.imapi2  ;

import com4j.*;

/**
 * File system image
 */
@IID("{2C941FE1-975B-59BE-A960-9A2A262853A5}")
public interface IFileSystemImage extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Root directory item
   * </p>
   * <p>
   * Getter method for the COM property "Root"
   * </p>
   * @return  Returns a value of type IFsiDirectoryItem
   */

  @DISPID(0) //= 0x0. The runtime will prefer the VTID if present
  @VTID(7)
  @DefaultMethod
  IFsiDirectoryItem root();


  @VTID(7)
  @ReturnValue(defaultPropertyThrough={IFsiDirectoryItem.class})
  IFsiItem root(
    java.lang.String path);

  /**
   * <p>
   * Disc start block for the image
   * </p>
   * <p>
   * Getter method for the COM property "SessionStartBlock"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(1) //= 0x1. The runtime will prefer the VTID if present
  @VTID(8)
  int sessionStartBlock();


  /**
   * <p>
   * Disc start block for the image
   * </p>
   * <p>
   * Setter method for the COM property "SessionStartBlock"
   * </p>
   * @param pVal Mandatory int parameter.
   */

  @DISPID(1) //= 0x1. The runtime will prefer the VTID if present
  @VTID(9)
  void sessionStartBlock(
    int pVal);


  /**
   * <p>
   * Maximum number of blocks available for the image
   * </p>
   * <p>
   * Getter method for the COM property "FreeMediaBlocks"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(2) //= 0x2. The runtime will prefer the VTID if present
  @VTID(10)
  int freeMediaBlocks();


  /**
   * <p>
   * Maximum number of blocks available for the image
   * </p>
   * <p>
   * Setter method for the COM property "FreeMediaBlocks"
   * </p>
   * @param pVal Mandatory int parameter.
   */

  @DISPID(2) //= 0x2. The runtime will prefer the VTID if present
  @VTID(11)
  void freeMediaBlocks(
    int pVal);


  /**
   * <p>
   * Set maximum number of blocks available based on the recorder supported discs. 0 for unknown maximum may be set.
   * </p>
   * @param discRecorder Mandatory IDiscRecorder2 parameter.
   */

  @DISPID(36) //= 0x24. The runtime will prefer the VTID if present
  @VTID(12)
  void setMaxMediaBlocksFromDevice(
    IDiscRecorder2 discRecorder);


  /**
   * <p>
   * Number of blocks in use
   * </p>
   * <p>
   * Getter method for the COM property "UsedBlocks"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(3) //= 0x3. The runtime will prefer the VTID if present
  @VTID(13)
  int usedBlocks();


  /**
   * <p>
   * Volume name
   * </p>
   * <p>
   * Getter method for the COM property "VolumeName"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(4) //= 0x4. The runtime will prefer the VTID if present
  @VTID(14)
  java.lang.String volumeName();


  /**
   * <p>
   * Volume name
   * </p>
   * <p>
   * Setter method for the COM property "VolumeName"
   * </p>
   * @param pVal Mandatory java.lang.String parameter.
   */

  @DISPID(4) //= 0x4. The runtime will prefer the VTID if present
  @VTID(15)
  void volumeName(
    java.lang.String pVal);


  /**
   * <p>
   * Imported Volume name
   * </p>
   * <p>
   * Getter method for the COM property "ImportedVolumeName"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(5) //= 0x5. The runtime will prefer the VTID if present
  @VTID(16)
  java.lang.String importedVolumeName();


  /**
   * <p>
   * Boot image and boot options
   * </p>
   * <p>
   * Getter method for the COM property "BootImageOptions"
   * </p>
   * @return  Returns a value of type IBootOptions
   */

  @DISPID(6) //= 0x6. The runtime will prefer the VTID if present
  @VTID(17)
  IBootOptions bootImageOptions();


  /**
   * <p>
   * Boot image and boot options
   * </p>
   * <p>
   * Setter method for the COM property "BootImageOptions"
   * </p>
   * @param pVal Mandatory IBootOptions parameter.
   */

  @DISPID(6) //= 0x6. The runtime will prefer the VTID if present
  @VTID(18)
  void bootImageOptions(
    IBootOptions pVal);


  /**
   * <p>
   * Number of files in the image
   * </p>
   * <p>
   * Getter method for the COM property "FileCount"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(7) //= 0x7. The runtime will prefer the VTID if present
  @VTID(19)
  int fileCount();


  /**
   * <p>
   * Number of directories in the image
   * </p>
   * <p>
   * Getter method for the COM property "DirectoryCount"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(8) //= 0x8. The runtime will prefer the VTID if present
  @VTID(20)
  int directoryCount();


  /**
   * <p>
   * Temp directory for stash files
   * </p>
   * <p>
   * Getter method for the COM property "WorkingDirectory"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(9) //= 0x9. The runtime will prefer the VTID if present
  @VTID(21)
  java.lang.String workingDirectory();


  /**
   * <p>
   * Temp directory for stash files
   * </p>
   * <p>
   * Setter method for the COM property "WorkingDirectory"
   * </p>
   * @param pVal Mandatory java.lang.String parameter.
   */

  @DISPID(9) //= 0x9. The runtime will prefer the VTID if present
  @VTID(22)
  void workingDirectory(
    java.lang.String pVal);


  /**
   * <p>
   * Change point identifier
   * </p>
   * <p>
   * Getter method for the COM property "ChangePoint"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(10) //= 0xa. The runtime will prefer the VTID if present
  @VTID(23)
  int changePoint();


  /**
   * <p>
   * Strict file system compliance option
   * </p>
   * <p>
   * Getter method for the COM property "StrictFileSystemCompliance"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(11) //= 0xb. The runtime will prefer the VTID if present
  @VTID(24)
  boolean strictFileSystemCompliance();


  /**
   * <p>
   * Strict file system compliance option
   * </p>
   * <p>
   * Setter method for the COM property "StrictFileSystemCompliance"
   * </p>
   * @param pVal Mandatory boolean parameter.
   */

  @DISPID(11) //= 0xb. The runtime will prefer the VTID if present
  @VTID(25)
  void strictFileSystemCompliance(
    boolean pVal);


  /**
   * <p>
   * If true, indicates restricted character set is being used for file and directory names
   * </p>
   * <p>
   * Getter method for the COM property "UseRestrictedCharacterSet"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(12) //= 0xc. The runtime will prefer the VTID if present
  @VTID(26)
  boolean useRestrictedCharacterSet();


  /**
   * <p>
   * If true, indicates restricted character set is being used for file and directory names
   * </p>
   * <p>
   * Setter method for the COM property "UseRestrictedCharacterSet"
   * </p>
   * @param pVal Mandatory boolean parameter.
   */

  @DISPID(12) //= 0xc. The runtime will prefer the VTID if present
  @VTID(27)
  void useRestrictedCharacterSet(
    boolean pVal);


  /**
   * <p>
   * File systems to create
   * </p>
   * <p>
   * Getter method for the COM property "FileSystemsToCreate"
   * </p>
   * @return  Returns a value of type FsiFileSystems
   */

  @DISPID(13) //= 0xd. The runtime will prefer the VTID if present
  @VTID(28)
  FsiFileSystems fileSystemsToCreate();


  /**
   * <p>
   * File systems to create
   * </p>
   * <p>
   * Setter method for the COM property "FileSystemsToCreate"
   * </p>
   * @param pVal Mandatory FsiFileSystems parameter.
   */

  @DISPID(13) //= 0xd. The runtime will prefer the VTID if present
  @VTID(29)
  void fileSystemsToCreate(
    FsiFileSystems pVal);


  /**
   * <p>
   * File systems supported
   * </p>
   * <p>
   * Getter method for the COM property "FileSystemsSupported"
   * </p>
   * @return  Returns a value of type FsiFileSystems
   */

  @DISPID(14) //= 0xe. The runtime will prefer the VTID if present
  @VTID(30)
  FsiFileSystems fileSystemsSupported();


  /**
   * <p>
   * UDF revision
   * </p>
   * <p>
   * Setter method for the COM property "UDFRevision"
   * </p>
   * @param pVal Mandatory int parameter.
   */

  @DISPID(37) //= 0x25. The runtime will prefer the VTID if present
  @VTID(31)
  void udfRevision(
    int pVal);


  /**
   * <p>
   * UDF revision
   * </p>
   * <p>
   * Getter method for the COM property "UDFRevision"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(37) //= 0x25. The runtime will prefer the VTID if present
  @VTID(32)
  int udfRevision();


  /**
   * <p>
   * UDF revision(s) supported
   * </p>
   * <p>
   * Getter method for the COM property "UDFRevisionsSupported"
   * </p>
   * @return  Returns a value of type java.lang.Object[]
   */

  @DISPID(31) //= 0x1f. The runtime will prefer the VTID if present
  @VTID(33)
  java.lang.Object[] udfRevisionsSupported();


  /**
   * <p>
   * Select filesystem types and image size based on the current media
   * </p>
   * @param discRecorder Mandatory IDiscRecorder2 parameter.
   */

  @DISPID(32) //= 0x20. The runtime will prefer the VTID if present
  @VTID(34)
  void chooseImageDefaults(
    IDiscRecorder2 discRecorder);


  /**
   * <p>
   * Select filesystem types and image size based on the media type
   * </p>
   * @param value Mandatory IMAPI_MEDIA_PHYSICAL_TYPE parameter.
   */

  @DISPID(33) //= 0x21. The runtime will prefer the VTID if present
  @VTID(35)
  void chooseImageDefaultsForMediaType(
    IMAPI_MEDIA_PHYSICAL_TYPE value);


  /**
   * <p>
   * ISO compatibility level to create
   * </p>
   * <p>
   * Setter method for the COM property "ISO9660InterchangeLevel"
   * </p>
   * @param pVal Mandatory int parameter.
   */

  @DISPID(34) //= 0x22. The runtime will prefer the VTID if present
  @VTID(36)
  void isO9660InterchangeLevel(
    int pVal);


  /**
   * <p>
   * ISO compatibility level to create
   * </p>
   * <p>
   * Getter method for the COM property "ISO9660InterchangeLevel"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(34) //= 0x22. The runtime will prefer the VTID if present
  @VTID(37)
  int isO9660InterchangeLevel();


  /**
   * <p>
   * ISO compatibility level(s) supported
   * </p>
   * <p>
   * Getter method for the COM property "ISO9660InterchangeLevelsSupported"
   * </p>
   * @return  Returns a value of type java.lang.Object[]
   */

  @DISPID(38) //= 0x26. The runtime will prefer the VTID if present
  @VTID(38)
  java.lang.Object[] isO9660InterchangeLevelsSupported();


  /**
   * <p>
   * Create result image stream
   * </p>
   * @return  Returns a value of type IFileSystemImageResult
   */

  @DISPID(15) //= 0xf. The runtime will prefer the VTID if present
  @VTID(39)
  IFileSystemImageResult createResultImage();


  /**
   * <p>
   * Check for existance an item in the file system
   * </p>
   * @param fullPath Mandatory java.lang.String parameter.
   * @return  Returns a value of type FsiItemType
   */

  @DISPID(16) //= 0x10. The runtime will prefer the VTID if present
  @VTID(40)
  FsiItemType exists(
    java.lang.String fullPath);


  /**
   * <p>
   * Return a string useful for identifying the current disc
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(18) //= 0x12. The runtime will prefer the VTID if present
  @VTID(41)
  java.lang.String calculateDiscIdentifier();


  /**
   * <p>
   * Identify file systems on a given disc
   * </p>
   * @param discRecorder Mandatory IDiscRecorder2 parameter.
   * @return  Returns a value of type FsiFileSystems
   */

  @DISPID(19) //= 0x13. The runtime will prefer the VTID if present
  @VTID(42)
  FsiFileSystems identifyFileSystemsOnDisc(
    IDiscRecorder2 discRecorder);


  /**
   * <p>
   * Identify which of the specified file systems would be imported by default
   * </p>
   * @param fileSystems Mandatory FsiFileSystems parameter.
   * @return  Returns a value of type FsiFileSystems
   */

  @DISPID(20) //= 0x14. The runtime will prefer the VTID if present
  @VTID(43)
  FsiFileSystems getDefaultFileSystemForImport(
    FsiFileSystems fileSystems);


  /**
   * <p>
   * Import the default file system on the current disc
   * </p>
   * @return  Returns a value of type FsiFileSystems
   */

  @DISPID(21) //= 0x15. The runtime will prefer the VTID if present
  @VTID(44)
  FsiFileSystems importFileSystem();


  /**
   * <p>
   * Import a specific file system on the current disc
   * </p>
   * @param fileSystemToUse Mandatory FsiFileSystems parameter.
   */

  @DISPID(22) //= 0x16. The runtime will prefer the VTID if present
  @VTID(45)
  void importSpecificFileSystem(
    FsiFileSystems fileSystemToUse);


  /**
   * <p>
   * Roll back to the specified change point
   * </p>
   * @param changePoint Mandatory int parameter.
   */

  @DISPID(23) //= 0x17. The runtime will prefer the VTID if present
  @VTID(46)
  void rollbackToChangePoint(
    int changePoint);


  /**
   * <p>
   * Lock in changes
   * </p>
   */

  @DISPID(24) //= 0x18. The runtime will prefer the VTID if present
  @VTID(47)
  void lockInChangePoint();


  /**
   * <p>
   * Create a directory item with the specified name
   * </p>
   * @param name Mandatory java.lang.String parameter.
   * @return  Returns a value of type IFsiDirectoryItem
   */

  @DISPID(25) //= 0x19. The runtime will prefer the VTID if present
  @VTID(48)
  IFsiDirectoryItem createDirectoryItem(
    java.lang.String name);


  /**
   * <p>
   * Create a file item with the specified name
   * </p>
   * @param name Mandatory java.lang.String parameter.
   * @return  Returns a value of type IFsiFileItem
   */

  @DISPID(26) //= 0x1a. The runtime will prefer the VTID if present
  @VTID(49)
  IFsiFileItem createFileItem(
    java.lang.String name);


  /**
   * <p>
   * Volume name
   * </p>
   * <p>
   * Getter method for the COM property "VolumeNameUDF"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(27) //= 0x1b. The runtime will prefer the VTID if present
  @VTID(50)
  java.lang.String volumeNameUDF();


  /**
   * <p>
   * Volume name
   * </p>
   * <p>
   * Getter method for the COM property "VolumeNameJoliet"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(28) //= 0x1c. The runtime will prefer the VTID if present
  @VTID(51)
  java.lang.String volumeNameJoliet();


  /**
   * <p>
   * Volume name
   * </p>
   * <p>
   * Getter method for the COM property "VolumeNameISO9660"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(29) //= 0x1d. The runtime will prefer the VTID if present
  @VTID(52)
  java.lang.String volumeNameISO9660();


  /**
   * <p>
   * Indicates whether or not IMAPI should stage the filesystem before the burn
   * </p>
   * <p>
   * Getter method for the COM property "StageFiles"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(30) //= 0x1e. The runtime will prefer the VTID if present
  @VTID(53)
  boolean stageFiles();


  /**
   * <p>
   * Indicates whether or not IMAPI should stage the filesystem before the burn
   * </p>
   * <p>
   * Setter method for the COM property "StageFiles"
   * </p>
   * @param pVal Mandatory boolean parameter.
   */

  @DISPID(30) //= 0x1e. The runtime will prefer the VTID if present
  @VTID(54)
  void stageFiles(
    boolean pVal);


  /**
   * <p>
   * Get array of available multi-session interfaces.
   * </p>
   * <p>
   * Getter method for the COM property "MultisessionInterfaces"
   * </p>
   * @return  Returns a value of type java.lang.Object[]
   */

  @DISPID(40) //= 0x28. The runtime will prefer the VTID if present
  @VTID(55)
  java.lang.Object[] multisessionInterfaces();


  /**
   * <p>
   * Get array of available multi-session interfaces.
   * </p>
   * <p>
   * Setter method for the COM property "MultisessionInterfaces"
   * </p>
   * @param pVal Mandatory java.lang.Object[] parameter.
   */

  @DISPID(40) //= 0x28. The runtime will prefer the VTID if present
  @VTID(56)
  void multisessionInterfaces(
    java.lang.Object[] pVal);


  // Properties:
}
