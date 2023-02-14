package com.ms.imapi2  ;

import com4j.*;

/**
 * FileSystemImage file item (rev.2)
 */
@IID("{199D0C19-11E1-40EB-8EC2-C8C822A07792}")
public interface IFsiFileItem2 extends IFsiFileItem {
  // Methods:
  /**
   * <p>
   * Get the list of the named streams of the file
   * </p>
   * <p>
   * Getter method for the COM property "FsiNamedStreams"
   * </p>
   * @return  Returns a value of type IFsiNamedStreams
   */

  @DISPID(45) //= 0x2d. The runtime will prefer the VTID if present
  @VTID(24)
  IFsiNamedStreams fsiNamedStreams();


  @VTID(24)
  @ReturnValue(defaultPropertyThrough={IFsiNamedStreams.class})
  IFsiFileItem2 fsiNamedStreams(
    int index);

  /**
   * <p>
   * Flag indicating if file item is a named stream of a file
   * </p>
   * <p>
   * Getter method for the COM property "IsNamedStream"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(46) //= 0x2e. The runtime will prefer the VTID if present
  @VTID(25)
  boolean isNamedStream();


  /**
   * <p>
   * Add a new named stream to the collection
   * </p>
   * @param name Mandatory java.lang.String parameter.
   * @param streamData Mandatory IStream parameter.
   */

  @DISPID(47) //= 0x2f. The runtime will prefer the VTID if present
  @VTID(26)
  void addStream(
    java.lang.String name,
    IStream streamData);


  /**
   * <p>
   * Remove a specific named stream from the collection
   * </p>
   * @param name Mandatory java.lang.String parameter.
   */

  @DISPID(48) //= 0x30. The runtime will prefer the VTID if present
  @VTID(27)
  void removeStream(
    java.lang.String name);


  /**
   * <p>
   * Flag indicating if file is Real-Time
   * </p>
   * <p>
   * Getter method for the COM property "IsRealTime"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(49) //= 0x31. The runtime will prefer the VTID if present
  @VTID(28)
  boolean isRealTime();


  /**
   * <p>
   * Flag indicating if file is Real-Time
   * </p>
   * <p>
   * Setter method for the COM property "IsRealTime"
   * </p>
   * @param pVal Mandatory boolean parameter.
   */

  @DISPID(49) //= 0x31. The runtime will prefer the VTID if present
  @VTID(29)
  void isRealTime(
    boolean pVal);


  // Properties:
}
