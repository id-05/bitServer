package com.ms.imapi2;

import com4j.*;

/**
 * A list of continuous LBA ranges
 */
@IID("{B507CA26-2204-11DD-966A-001AA01BBC58}")
public interface IBlockRangeList extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Returns an array of LBA ranges.
   * </p>
   * <p>
   * Getter method for the COM property "BlockRanges"
   * </p>
   * @return  Returns a value of type java.lang.Object[]
   */

  @DISPID(256) //= 0x100. The runtime will prefer the VTID if present
  @VTID(7)
  Object[] blockRanges();


  // Properties:
}
