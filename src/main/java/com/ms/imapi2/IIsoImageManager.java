package com.ms.imapi2  ;

import com4j.*;

/**
 * ISO Image Manager: Helper object for ISO image file manipulation
 */
@IID("{6CA38BE5-FBBB-4800-95A1-A438865EB0D4}")
public interface IIsoImageManager extends Com4jObject {
  // Methods:
  /**
   * <p>
   * Path to the ISO image file
   * </p>
   * <p>
   * Getter method for the COM property "path"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @VTID(7)
  java.lang.String path();


  /**
   * <p>
   * Stream from the ISO image
   * </p>
   * <p>
   * Getter method for the COM property "Stream"
   * </p>
   * @return  Returns a value of type IStream
   */

  @VTID(8)
  IStream stream();


  /**
   * <p>
   * Set path to the ISO image file, overwrites stream
   * </p>
   * @param val Mandatory java.lang.String parameter.
   */

  @VTID(9)
  void setPath(
    java.lang.String val);


  /**
   * <p>
   * Set stream from the ISO image, overwrites path
   * </p>
   * @param data Mandatory IStream parameter.
   */

  @VTID(10)
  void setStream(
    IStream data);


  /**
   * <p>
   * Validate if the ISO image file is a valid file
   * </p>
   */

  @VTID(11)
  void validate();


  // Properties:
}
