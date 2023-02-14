package com.ms.imapi2  ;

import com4j.*;

/**
 * Provides notification of file system import progress
 */
@IID("{D25C30F9-4087-4366-9E24-E55BE286424B}")
public interface DFileSystemImageImportEvents extends Com4jObject {
  // Methods:
  /**
   * <p>
   * File System Import Progress
   * </p>
   * @param object Mandatory com4j.Com4jObject parameter.
   * @param fileSystem Mandatory FsiFileSystems parameter.
   * @param currentItem Mandatory java.lang.String parameter.
   * @param importedDirectoryItems Mandatory int parameter.
   * @param totalDirectoryItems Mandatory int parameter.
   * @param importedFileItems Mandatory int parameter.
   * @param totalFileItems Mandatory int parameter.
   */

  @VTID(7)
  void updateImport(
    @MarshalAs(NativeType.Dispatch) com4j.Com4jObject object,
    FsiFileSystems fileSystem,
    java.lang.String currentItem,
    int importedDirectoryItems,
    int totalDirectoryItems,
    int importedFileItems,
    int totalFileItems);


  // Properties:
}
