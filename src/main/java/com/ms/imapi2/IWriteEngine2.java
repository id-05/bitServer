package com.ms.imapi2;

import com4j.*;

/**
 * Write Engine
 */
@IID("{27354135-7F64-5B0F-8F00-5D77AFBE261E}")
public interface IWriteEngine2 extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Writes data provided in the IStream to the device
   * </p>
   * @param data Mandatory com.ms.imapi2.IStream parameter.
   * @param startingBlockAddress Mandatory int parameter.
   * @param numberOfBlocks Mandatory int parameter.
   */

  @DISPID(512) //= 0x200. The runtime will prefer the VTID if present
  @VTID(7)
  void writeSection(
    IStream data,
    int startingBlockAddress,
    int numberOfBlocks);


  /**
   * <p>
   * Cancels the current write operation
   * </p>
   */

  @DISPID(513) //= 0x201. The runtime will prefer the VTID if present
  @VTID(8)
  void cancelWrite();


  /**
   * <p>
   * The disc recorder to use
   * </p>
   * <p>
   * Setter method for the COM property "Recorder"
   * </p>
   * @param value Mandatory com.ms.imapi2.IDiscRecorder2Ex parameter.
   */

  @DISPID(256) //= 0x100. The runtime will prefer the VTID if present
  @VTID(9)
  void recorder(
    IDiscRecorder2Ex value);


  /**
   * <p>
   * The disc recorder to use
   * </p>
   * <p>
   * Getter method for the COM property "Recorder"
   * </p>
   * @return  Returns a value of type com.ms.imapi2.IDiscRecorder2Ex
   */

  @DISPID(256) //= 0x100. The runtime will prefer the VTID if present
  @VTID(10)
  IDiscRecorder2Ex recorder();


  /**
   * <p>
   * If true, uses WRITE12 with the AV bit set to one; else uses WRITE10
   * </p>
   * <p>
   * Setter method for the COM property "UseStreamingWrite12"
   * </p>
   * @param value Mandatory boolean parameter.
   */

  @DISPID(257) //= 0x101. The runtime will prefer the VTID if present
  @VTID(11)
  void useStreamingWrite12(
    boolean value);


  /**
   * <p>
   * If true, uses WRITE12 with the AV bit set to one; else uses WRITE10
   * </p>
   * <p>
   * Getter method for the COM property "UseStreamingWrite12"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(257) //= 0x101. The runtime will prefer the VTID if present
  @VTID(12)
  boolean useStreamingWrite12();


  /**
   * <p>
   * The approximate number of sectors per second the device can write at the start of the write process.  This is used to optimize sleep time in the write engine.
   * </p>
   * <p>
   * Setter method for the COM property "StartingSectorsPerSecond"
   * </p>
   * @param value Mandatory int parameter.
   */

  @DISPID(258) //= 0x102. The runtime will prefer the VTID if present
  @VTID(13)
  void startingSectorsPerSecond(
    int value);


  /**
   * <p>
   * The approximate number of sectors per second the device can write at the start of the write process.  This is used to optimize sleep time in the write engine.
   * </p>
   * <p>
   * Getter method for the COM property "StartingSectorsPerSecond"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(258) //= 0x102. The runtime will prefer the VTID if present
  @VTID(14)
  int startingSectorsPerSecond();


  /**
   * <p>
   * The approximate number of sectors per second the device can write at the end of the write process.  This is used to optimize sleep time in the write engine.
   * </p>
   * <p>
   * Setter method for the COM property "EndingSectorsPerSecond"
   * </p>
   * @param value Mandatory int parameter.
   */

  @DISPID(259) //= 0x103. The runtime will prefer the VTID if present
  @VTID(15)
  void endingSectorsPerSecond(
    int value);


  /**
   * <p>
   * The approximate number of sectors per second the device can write at the end of the write process.  This is used to optimize sleep time in the write engine.
   * </p>
   * <p>
   * Getter method for the COM property "EndingSectorsPerSecond"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(259) //= 0x103. The runtime will prefer the VTID if present
  @VTID(16)
  int endingSectorsPerSecond();


  /**
   * <p>
   * The number of bytes to use for each sector during writing.
   * </p>
   * <p>
   * Setter method for the COM property "BytesPerSector"
   * </p>
   * @param value Mandatory int parameter.
   */

  @DISPID(260) //= 0x104. The runtime will prefer the VTID if present
  @VTID(17)
  void bytesPerSector(
    int value);


  /**
   * <p>
   * The number of bytes to use for each sector during writing.
   * </p>
   * <p>
   * Getter method for the COM property "BytesPerSector"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(260) //= 0x104. The runtime will prefer the VTID if present
  @VTID(18)
  int bytesPerSector();


  /**
   * <p>
   * Simple check to see if the object is currently writing to media.
   * </p>
   * <p>
   * Getter method for the COM property "WriteInProgress"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(261) //= 0x105. The runtime will prefer the VTID if present
  @VTID(19)
  boolean writeInProgress();


  // Properties:
}
