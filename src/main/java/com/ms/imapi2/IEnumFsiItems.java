package com.ms.imapi2  ;

import com4j.*;

/**
 * FileSystemImage item enumerator
 */
@IID("{2C941FDA-975B-59BE-A960-9A2A262853A5}")
public interface IEnumFsiItems extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Remoting support for Next (allow NULL pointer for item count when requesting single item)
   * </p>
   * @param celt Mandatory int parameter.
   * @param rgelt Mandatory Holder<IFsiItem> parameter.
   * @param pceltFetched Mandatory Holder<Integer> parameter.
   */

  @VTID(3)
  void remoteNext(
    int celt,
    Holder<IFsiItem> rgelt,
    Holder<Integer> pceltFetched);


  /**
   * <p>
   * Skip items in the enumeration
   * </p>
   * @param celt Mandatory int parameter.
   */

  @VTID(4)
  void skip(
    int celt);


  /**
   * <p>
   * Reset the enumerator
   * </p>
   */

  @VTID(5)
  void reset();


  /**
   * <p>
   * Make a copy of the enumerator
   * </p>
   * @return  Returns a value of type IEnumFsiItems
   */

  @VTID(6)
  IEnumFsiItems clone();


  // Properties:
}
