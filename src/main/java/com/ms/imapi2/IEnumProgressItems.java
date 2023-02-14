package com.ms.imapi2  ;

import com4j.*;

/**
 * FileSystemImageResult progress item enumerator
 */
@IID("{2C941FD6-975B-59BE-A960-9A2A262853A5}")
public interface IEnumProgressItems extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Remoting support for Next (allow NULL pointer for item count when requesting single item)
   * </p>
   * @param celt Mandatory int parameter.
   * @param rgelt Mandatory Holder<IProgressItem> parameter.
   * @param pceltFetched Mandatory Holder<Integer> parameter.
   */

  @VTID(3)
  void remoteNext(
    int celt,
    Holder<IProgressItem> rgelt,
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
   * @return  Returns a value of type IEnumProgressItems
   */

  @VTID(6)
  IEnumProgressItems clone();


  // Properties:
}
