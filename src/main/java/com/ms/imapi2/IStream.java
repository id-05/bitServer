package com.ms.imapi2;

import com4j.*;

@IID("{0000000C-0000-0000-C000-000000000046}")
public interface IStream extends ISequentialStream {
  // Methods:
      /**
       * @param grfCommitFlags Mandatory int parameter.
       */

      @VTID(8)
      void commit(
        int grfCommitFlags);


      /**
       */

      @VTID(9)
      void revert();


          /**
           * @return  Returns a value of type com.ms.imapi2.IStream
           */

          @VTID(13)
          IStream clone();


          // Properties:
        }
