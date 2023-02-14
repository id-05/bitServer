package com.ms.imapi2  ;

import com4j.*;

/**
 * FileSystemImage result stream (rev.2)
 */
@IID("{B507CA29-2204-11DD-966A-001AA01BBC58}")
public interface IFileSystemImageResult2 extends IFileSystemImageResult {
  // Methods:
  /**
   * <p>
   * List of modified block ranges in the result stream
   * </p>
   * <p>
   * Getter method for the COM property "ModifiedBlocks"
   * </p>
   * @return  Returns a value of type IBlockRangeList
   */

  @DISPID(6) //= 0x6. The runtime will prefer the VTID if present
  @VTID(12)
  IBlockRangeList modifiedBlocks();


  // Properties:
}
