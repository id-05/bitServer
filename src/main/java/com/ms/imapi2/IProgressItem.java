package com.ms.imapi2  ;

import com4j.*;

/**
 * FileSystemImageResult progress item
 */
@IID("{2C941FD5-975B-59BE-A960-9A2A262853A5}")
public interface IProgressItem extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Progress item description
   * </p>
   * <p>
   * Getter method for the COM property "Description"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(1) //= 0x1. The runtime will prefer the VTID if present
  @VTID(7)
  java.lang.String description();


  /**
   * <p>
   * First block in the range of blocks used by the progress item
   * </p>
   * <p>
   * Getter method for the COM property "FirstBlock"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(2) //= 0x2. The runtime will prefer the VTID if present
  @VTID(8)
  int firstBlock();


  /**
   * <p>
   * Last block in the range of blocks used by the progress item
   * </p>
   * <p>
   * Getter method for the COM property "LastBlock"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(3) //= 0x3. The runtime will prefer the VTID if present
  @VTID(9)
  int lastBlock();


  /**
   * <p>
   * Number of blocks used by the progress item
   * </p>
   * <p>
   * Getter method for the COM property "BlockCount"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(4) //= 0x4. The runtime will prefer the VTID if present
  @VTID(10)
  int blockCount();


  // Properties:
}
