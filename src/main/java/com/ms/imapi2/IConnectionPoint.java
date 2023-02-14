package com.ms.imapi2;

import com4j.*;

@IID("{B196B286-BAB4-101A-B69C-00AA00341D07}")
public interface IConnectionPoint extends Com4jObject {
  // Methods:
  /**
   * @return  Returns a value of type GUID
   */

  @VTID(3)
  GUID getConnectionInterface();


  /**
   * @return  Returns a value of type com.ms.imapi2.IConnectionPointContainer
   */

  @VTID(4)
  IConnectionPointContainer getConnectionPointContainer();


  /**
   * @param pUnkSink Mandatory com4j.Com4jObject parameter.
   * @return  Returns a value of type int
   */

  @VTID(5)
  int advise(
    Com4jObject pUnkSink);


  /**
   * @param dwCookie Mandatory int parameter.
   */

  @VTID(6)
  void unadvise(
    int dwCookie);


  /**
   * @return  Returns a value of type com.ms.imapi2.IEnumConnections
   */

  @VTID(7)
  IEnumConnections enumConnections();


  // Properties:
}
