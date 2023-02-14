package com.ms.imapi2  ;

import com4j.*;

/**
 * FileSystemImage directory item
 */
@IID("{2C941FDC-975B-59BE-A960-9A2A262853A5}")
public interface IFsiDirectoryItem extends IFsiItem,Iterable<Com4jObject> {
  // Methods:
  /**
   * <p>
   * Get an enumerator for the collection
   * </p>
   * <p>
   * Getter method for the COM property "_NewEnum"
   * </p>
   */

  @DISPID(-4) //= 0xfffffffc. The runtime will prefer the VTID if present
  @VTID(19)
  java.util.Iterator<Com4jObject> iterator();

  /**
   * <p>
   * Get the item with the given relative path
   * </p>
   * <p>
   * Getter method for the COM property "Item"
   * </p>
   * @param path Mandatory java.lang.String parameter.
   * @return  Returns a value of type IFsiItem
   */

  @DISPID(0) //= 0x0. The runtime will prefer the VTID if present
  @VTID(20)
  @DefaultMethod
  IFsiItem item(
    java.lang.String path);


  /**
   * <p>
   * Number of items in the collection
   * </p>
   * <p>
   * Getter method for the COM property "Count"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(1) //= 0x1. The runtime will prefer the VTID if present
  @VTID(21)
  int count();


  /**
   * <p>
   * Get a non-variant enumerator
   * </p>
   * <p>
   * Getter method for the COM property "EnumFsiItems"
   * </p>
   * @return  Returns a value of type IEnumFsiItems
   */

  @DISPID(2) //= 0x2. The runtime will prefer the VTID if present
  @VTID(22)
  IEnumFsiItems enumFsiItems();


  /**
   * <p>
   * Add a directory with the specified relative path
   * </p>
   * @param path Mandatory java.lang.String parameter.
   */

  @DISPID(30) //= 0x1e. The runtime will prefer the VTID if present
  @VTID(23)
  void addDirectory(
    java.lang.String path);


  /**
   * <p>
   * Add a file with the specified relative path and data
   * </p>
   * @param path Mandatory java.lang.String parameter.
   * @param fileData Mandatory IStream parameter.
   */

  @DISPID(31) //= 0x1f. The runtime will prefer the VTID if present
  @VTID(24)
  void addFile(
    java.lang.String path,
    IStream fileData);


  /**
   * <p>
   * Add files and directories from the specified source directory
   * </p>
   * @param sourceDirectory Mandatory java.lang.String parameter.
   * @param includeBaseDirectory Mandatory boolean parameter.
   */

  @DISPID(32) //= 0x20. The runtime will prefer the VTID if present
  @VTID(25)
  void addTree(
    java.lang.String sourceDirectory,
    boolean includeBaseDirectory);


  /**
   * <p>
   * Add an item
   * </p>
   * @param item Mandatory IFsiItem parameter.
   */

  @DISPID(33) //= 0x21. The runtime will prefer the VTID if present
  @VTID(26)
  void add(
    IFsiItem item);


  /**
   * <p>
   * Remove an item with the specified relative path
   * </p>
   * @param path Mandatory java.lang.String parameter.
   */

  @DISPID(34) //= 0x22. The runtime will prefer the VTID if present
  @VTID(27)
  void remove(
    java.lang.String path);


  /**
   * <p>
   * Remove a subtree with the specified relative path
   * </p>
   * @param path Mandatory java.lang.String parameter.
   */

  @DISPID(35) //= 0x23. The runtime will prefer the VTID if present
  @VTID(28)
  void removeTree(
    java.lang.String path);


  // Properties:
}
