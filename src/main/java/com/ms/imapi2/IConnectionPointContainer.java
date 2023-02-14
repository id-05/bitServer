package com.ms.imapi2;

import com4j.*;

@IID("{B196B284-BAB4-101A-B69C-00AA00341D07}")
public interface IConnectionPointContainer extends Com4jObject {
  // Methods:
  /**
   * @return  Returns a value of type com.ms.imapi2.IEnumConnectionPoints
   */

  @VTID(3)
  IEnumConnectionPoints enumConnectionPoints();


  /**
   * @param riid Mandatory GUID parameter.
   * @return  Returns a value of type com.ms.imapi2.IConnectionPoint
   */

  @VTID(4)
  IConnectionPoint findConnectionPoint(
    GUID riid);


  // Properties:
}
