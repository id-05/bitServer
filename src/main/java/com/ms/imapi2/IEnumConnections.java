package com.ms.imapi2;

import com4j.*;

@IID("{B196B287-BAB4-101A-B69C-00AA00341D07}")
public interface IEnumConnections extends Com4jObject {
  // Methods:
    /**
     * @param cConnections Mandatory int parameter.
     */

    @VTID(4)
    void skip(
      int cConnections);


    /**
     */

    @VTID(5)
    void reset();


    /**
     * @return  Returns a value of type com.ms.imapi2.IEnumConnections
     */

    @VTID(6)
    IEnumConnections clone();


    // Properties:
  }
