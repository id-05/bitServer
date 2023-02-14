package com.ms.imapi2;

import com4j.*;

/**
 * Random write multisession support interface.
 */
@IID("{B507CA23-2204-11DD-966A-001AA01BBC58}")
public interface IMultisessionRandomWrite extends IMultisession {
  // Methods:
  /**
   * <p>
   * Write unit size (writes must be performed in these units).
   * </p>
   * <p>
   * Getter method for the COM property "WriteUnitSize"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(517) //= 0x205. The runtime will prefer the VTID if present
  @VTID(11)
  int writeUnitSize();


  /**
   * <p>
   * The last sector written on the media.
   * </p>
   * <p>
   * Getter method for the COM property "LastWrittenAddress"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(518) //= 0x206. The runtime will prefer the VTID if present
  @VTID(12)
  int lastWrittenAddress();


  /**
   * <p>
   * The total number of sectors available on the media.
   * </p>
   * <p>
   * Getter method for the COM property "TotalSectorsOnMedia"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(519) //= 0x207. The runtime will prefer the VTID if present
  @VTID(13)
  int totalSectorsOnMedia();


  // Properties:
}
