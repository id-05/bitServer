package com.ms.imapi2  ;

import com4j.*;

/**
 * FileSystemImage directory item (rev.2)
 */
@IID("{F7FB4B9B-6D96-4D7B-9115-201B144811EF}")
public interface IFsiDirectoryItem2 extends IFsiDirectoryItem {
  // Methods:
  /**
   * <p>
   * Add files and directories from the specified source directory including named streams
   * </p>
   * @param sourceDirectory Mandatory java.lang.String parameter.
   * @param includeBaseDirectory Mandatory boolean parameter.
   */

  @DISPID(36) //= 0x24. The runtime will prefer the VTID if present
  @VTID(29)
  void addTreeWithNamedStreams(
    java.lang.String sourceDirectory,
    boolean includeBaseDirectory);


  // Properties:
}
