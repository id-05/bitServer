package com.ms.imapi2  ;

import com4j.*;

/**
 * FileSystemImage result stream
 */
@IID("{2C941FD8-975B-59BE-A960-9A2A262853A5}")
public interface IFileSystemImageResult extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Image stream
   * </p>
   * <p>
   * Getter method for the COM property "ImageStream"
   * </p>
   * @return  Returns a value of type IStream
   */

  @DISPID(1) //= 0x1. The runtime will prefer the VTID if present
  @VTID(7)
  IStream imageStream();


  /**
   * <p>
   * Progress item block mapping collection
   * </p>
   * <p>
   * Getter method for the COM property "ProgressItems"
   * </p>
   * @return  Returns a value of type IProgressItems
   */

  @DISPID(2) //= 0x2. The runtime will prefer the VTID if present
  @VTID(8)
  IProgressItems progressItems();


  @VTID(8)
  @ReturnValue(defaultPropertyThrough={IProgressItems.class})
  IProgressItem progressItems(
    int index);

  /**
   * <p>
   * Number of blocks in the result image
   * </p>
   * <p>
   * Getter method for the COM property "TotalBlocks"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(3) //= 0x3. The runtime will prefer the VTID if present
  @VTID(9)
  int totalBlocks();


  /**
   * <p>
   * Number of bytes in a block
   * </p>
   * <p>
   * Getter method for the COM property "BlockSize"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(4) //= 0x4. The runtime will prefer the VTID if present
  @VTID(10)
  int blockSize();


  /**
   * <p>
   * Disc Identifier (for identifing imported session of multi-session disc)
   * </p>
   * <p>
   * Getter method for the COM property "DiscId"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(5) //= 0x5. The runtime will prefer the VTID if present
  @VTID(11)
  java.lang.String discId();


  // Properties:
}
