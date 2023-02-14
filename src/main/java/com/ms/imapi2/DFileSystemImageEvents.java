package com.ms.imapi2  ;

import com4j.*;

/**
 * Provides notification of file system creation progress
 */
@IID("{2C941FDF-975B-59BE-A960-9A2A262853A5}")
public interface DFileSystemImageEvents extends Com4jObject {
  // Methods:
  /**
   * <p>
   * File Import Progress
   * </p>
   * @param object Mandatory com4j.Com4jObject parameter.
   * @param currentFile Mandatory java.lang.String parameter.
   * @param copiedSectors Mandatory int parameter.
   * @param totalSectors Mandatory int parameter.
   */

  @DISPID(7)
  @VTID(7)
  void update(
    @MarshalAs(NativeType.Dispatch) com4j.Com4jObject object,
    java.lang.String currentFile,
    int copiedSectors,
    int totalSectors);


  // Properties:
}
