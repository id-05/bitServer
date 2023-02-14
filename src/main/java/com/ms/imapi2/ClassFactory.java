package com.ms.imapi2;

import com4j.*;

/**
 * Defines methods to create COM objects
 */
public abstract class ClassFactory {
  private ClassFactory() {} // instanciation is not allowed


  /**
   * Microsoft IMAPIv2 Disc Master
   */
  public static IDiscMaster2 createMsftDiscMaster2() {
    return COM4J.createInstance( IDiscMaster2.class, "{2735412E-7F64-5B0F-8F00-5D77AFBE261E}" );
  }

  /**
   * Microsoft IMAPIv2 Disc Recorder
   */
  public static IDiscRecorder2 createMsftDiscRecorder2() {
    return COM4J.createInstance( IDiscRecorder2.class, "{2735412D-7F64-5B0F-8F00-5D77AFBE261E}" );
  }

  /**
   * Microsoft IMAPIv2 CD Write Engine
   */
  public static IWriteEngine2 createMsftWriteEngine2() {
    return COM4J.createInstance( IWriteEngine2.class, "{2735412C-7F64-5B0F-8F00-5D77AFBE261E}" );
  }

  /**
   * Microsoft IMAPIv2 Media Erase/Blank
   */
  public static IDiscFormat2Erase createMsftDiscFormat2Erase() {
    return COM4J.createInstance( IDiscFormat2Erase.class, "{2735412B-7F64-5B0F-8F00-5D77AFBE261E}" );
  }

  /**
   * Microsoft IMAPIv2 Data Writer
   */
  public static IDiscFormat2Data createMsftDiscFormat2Data() {
    return COM4J.createInstance( IDiscFormat2Data.class, "{2735412A-7F64-5B0F-8F00-5D77AFBE261E}" );
  }

  /**
   * Microsoft IMAPIv2 Track-at-Once Audio CD Writer
   */
  public static IDiscFormat2TrackAtOnce createMsftDiscFormat2TrackAtOnce() {
    return COM4J.createInstance( IDiscFormat2TrackAtOnce.class, "{27354129-7F64-5B0F-8F00-5D77AFBE261E}" );
  }

  /**
   * Microsoft IMAPIv2 Disc-at-Once RAW CD Image Writer
   */
  public static IDiscFormat2RawCD createMsftDiscFormat2RawCD() {
    return COM4J.createInstance( IDiscFormat2RawCD.class, "{27354128-7F64-5B0F-8F00-5D77AFBE261E}" );
  }

  /**
   * Microsoft IMAPIv2 /dev/zero Stream
   */
  public static IStream createMsftStreamZero() {
    return COM4J.createInstance( IStream.class, "{27354127-7F64-5B0F-8F00-5D77AFBE261E}" );
  }

  /**
   * Microsoft IMAPIv2 PRNG based Stream (LCG: 0x19660D, 0x3C6EF35F)
   */
  public static IStreamPseudoRandomBased createMsftStreamPrng001() {
    return COM4J.createInstance( IStreamPseudoRandomBased.class, "{27354126-7F64-5B0F-8F00-5D77AFBE261E}" );
  }

  /**
   * Microsoft IMAPIv2 concatenation stream
   */
  public static IStreamConcatenate createMsftStreamConcatenate() {
    return COM4J.createInstance( IStreamConcatenate.class, "{27354125-7F64-5B0F-8F00-5D77AFBE261E}" );
  }

  /**
   * Microsoft IMAPIv2 interleave stream
   */
  public static IStreamInterleave createMsftStreamInterleave() {
    return COM4J.createInstance( IStreamInterleave.class, "{27354124-7F64-5B0F-8F00-5D77AFBE261E}" );
  }

  /**
   * Microsoft IMAPIv2 RAW CD Image Creator
   */
  public static IRawCDImageCreator createMsftRawCDImageCreator() {
    return COM4J.createInstance( IRawCDImageCreator.class, "{25983561-9D65-49CE-B335-40630D901227}" );
  }

  /**
   * Boot options
   */
  public static IBootOptions createBootOptions() {
    return COM4J.createInstance( IBootOptions.class, "{2C941FCE-975B-59BE-A960-9A2A262853A5}" );
  }

  /**
   * File system image
   */
  public static IFileSystemImage3 createMsftFileSystemImage() {
    return COM4J.createInstance( IFileSystemImage3.class, "{2C941FC5-975B-59BE-A960-9A2A262853A5}" );
  }

  /**
   * Microsoft IMAPIv2 Iso Image Manager
   */
  public static IIsoImageManager createMsftIsoImageManager() {
    return COM4J.createInstance( IIsoImageManager.class, "{CEEE3B62-8F56-4056-869B-EF16917E3EFC}" );
  }
}
