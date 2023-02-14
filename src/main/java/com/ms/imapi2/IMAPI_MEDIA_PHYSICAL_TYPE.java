package com.ms.imapi2;

import com4j.*;

/**
 * <p>
 * Physical type of the optical media
 * </p>
 */
public enum IMAPI_MEDIA_PHYSICAL_TYPE implements ComEnum {
  /**
   * <p>
   * Media not present or unrecognized
   * </p>
   * <p>
   * The value of this constant is 0
   * </p>
   */
  IMAPI_MEDIA_TYPE_UNKNOWN(0, "Media not present or unrecognized"),
  /**
   * <p>
   * CD-ROM media
   * </p>
   * <p>
   * The value of this constant is 1
   * </p>
   */
  IMAPI_MEDIA_TYPE_CDROM(1, "CD-ROM media"),
  /**
   * <p>
   * CD-R media
   * </p>
   * <p>
   * The value of this constant is 2
   * </p>
   */
  IMAPI_MEDIA_TYPE_CDR(2, "CD-R media"),
  /**
   * <p>
   * CD-RW media
   * </p>
   * <p>
   * The value of this constant is 3
   * </p>
   */
  IMAPI_MEDIA_TYPE_CDRW(3, "CD-RW media"),
  /**
   * <p>
   * DVD-ROM media
   * </p>
   * <p>
   * The value of this constant is 4
   * </p>
   */
  IMAPI_MEDIA_TYPE_DVDROM(4, "DVD-ROM (Read-only DVD drive and/or disc)"),
  /**
   * <p>
   * DVD-RAM media
   * </p>
   * <p>
   * The value of this constant is 5
   * </p>
   */
  IMAPI_MEDIA_TYPE_DVDRAM(5, "DVD-RAM media"),
  /**
   * <p>
   * DVD+R media
   * </p>
   * <p>
   * The value of this constant is 6
   * </p>
   */
  IMAPI_MEDIA_TYPE_DVDPLUSR(6, "DVD+R media"),
  /**
   * <p>
   * DVD+RW media
   * </p>
   * <p>
   * The value of this constant is 7
   * </p>
   */
  IMAPI_MEDIA_TYPE_DVDPLUSRW(7, "DVD+RW media"),
  /**
   * <p>
   * DVD+R dual layer media
   * </p>
   * <p>
   * The value of this constant is 8
   * </p>
   */
  IMAPI_MEDIA_TYPE_DVDPLUSR_DUALLAYER(8, "DVD+R dual layer media"),
  /**
   * <p>
   * DVD-R media
   * </p>
   * <p>
   * The value of this constant is 9
   * </p>
   */
  IMAPI_MEDIA_TYPE_DVDDASHR(9, "DVD-R media"),
  /**
   * <p>
   * DVD-RW media
   * </p>
   * <p>
   * The value of this constant is 10
   * </p>
   */
  IMAPI_MEDIA_TYPE_DVDDASHRW(10, "DVD-RW media"),
  /**
   * <p>
   * DVD-R dual layer media
   * </p>
   * <p>
   * The value of this constant is 11
   * </p>
   */
  IMAPI_MEDIA_TYPE_DVDDASHR_DUALLAYER(11, "VD-R dual layer media"),
  /**
   * <p>
   * Randomly writable media
   * </p>
   * <p>
   * The value of this constant is 12
   * </p>
   */
  IMAPI_MEDIA_TYPE_DISK(12, "Randomly-writable, hardware-defect managed media type that reports the 'Disc' profile as current"),
  /**
   * <p>
   * DVD+RW dual layer media
   * </p>
   * <p>
   * The value of this constant is 13
   * </p>
   */
  IMAPI_MEDIA_TYPE_DVDPLUSRW_DUALLAYER(13, "DVD+RW dual layer media"),
  /**
   * <p>
   * HD DVD-ROM media
   * </p>
   * <p>
   * The value of this constant is 14
   * </p>
   */
  IMAPI_MEDIA_TYPE_HDDVDROM(14, "HD DVD-ROM media"),
  /**
   * <p>
   * HD DVD-R media
   * </p>
   * <p>
   * The value of this constant is 15
   * </p>
   */
  IMAPI_MEDIA_TYPE_HDDVDR(15, "HD DVD-R media"),
  /**
   * <p>
   * HD DVD-RAM media
   * </p>
   * <p>
   * The value of this constant is 16
   * </p>
   */
  IMAPI_MEDIA_TYPE_HDDVDRAM(16, "HD DVD-RAM media"),
  /**
   * <p>
   * BD-ROM media
   * </p>
   * <p>
   * The value of this constant is 17
   * </p>
   */
  IMAPI_MEDIA_TYPE_BDROM(17, "BD-ROM media"),
  /**
   * <p>
   * BD-R media
   * </p>
   * <p>
   * The value of this constant is 18
   * </p>
   */
  IMAPI_MEDIA_TYPE_BDR(18, "BD-R media"),
  /**
   * <p>
   * BD-RE media
   * </p>
   * <p>
   * The value of this constant is 19
   * </p>
   */
  IMAPI_MEDIA_TYPE_BDRE(19, "BD-RE media"),
  /**
   * <p>
   * Max value for a media type
   * </p>
   * <p>
   * The value of this constant is 19
   * </p>
   */
  IMAPI_MEDIA_TYPE_MAX(19, "Max value for a media type"),
  ;

  private final int value;
  private final String desc;

  IMAPI_MEDIA_PHYSICAL_TYPE(int value, String desc) {
    this.value=value;
    this.desc = desc;
  }

  public int comEnumValue() { return value; }
  public String getDesc() {
    return desc;
  }
}
