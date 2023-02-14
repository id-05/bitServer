package com.ms.imapi2;

import com4j.*;

/**
 * CD Write Engine
 */
@IID("{27354136-7F64-5B0F-8F00-5D77AFBE261E}")
public interface IWriteEngine2EventArgs extends Com4jObject {
  // Methods:
  /**
   * <p>
   * The starting logical block address for the current write operation.
   * </p>
   * <p>
   * Getter method for the COM property "StartLba"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(256) //= 0x100. The runtime will prefer the VTID if present
  @VTID(7)
  int startLba();


  /**
   * <p>
   * The number of sectors being written for the current write operation.
   * </p>
   * <p>
   * Getter method for the COM property "SectorCount"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(257) //= 0x101. The runtime will prefer the VTID if present
  @VTID(8)
  int sectorCount();


  /**
   * <p>
   * The last logical block address of data read for the current write operation.
   * </p>
   * <p>
   * Getter method for the COM property "LastReadLba"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(258) //= 0x102. The runtime will prefer the VTID if present
  @VTID(9)
  int lastReadLba();


  /**
   * <p>
   * The last logical block address of data written for the current write operation
   * </p>
   * <p>
   * Getter method for the COM property "LastWrittenLba"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(259) //= 0x103. The runtime will prefer the VTID if present
  @VTID(10)
  int lastWrittenLba();


  /**
   * <p>
   * The total bytes available in the system's cache buffer
   * </p>
   * <p>
   * Getter method for the COM property "TotalSystemBuffer"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(262) //= 0x106. The runtime will prefer the VTID if present
  @VTID(11)
  int totalSystemBuffer();


  /**
   * <p>
   * The used bytes in the system's cache buffer
   * </p>
   * <p>
   * Getter method for the COM property "UsedSystemBuffer"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(263) //= 0x107. The runtime will prefer the VTID if present
  @VTID(12)
  int usedSystemBuffer();


  /**
   * <p>
   * The free bytes in the system's cache buffer
   * </p>
   * <p>
   * Getter method for the COM property "FreeSystemBuffer"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(264) //= 0x108. The runtime will prefer the VTID if present
  @VTID(13)
  int freeSystemBuffer();


  // Properties:
}
