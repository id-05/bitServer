package ru.bitServer.dicom;

public class UploadDicomFile {
    byte[] Data;
    String FileName;

    public byte[] getData() {
        return Data;
    }

    public void setData(byte[] data) {
        Data = data;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public UploadDicomFile(byte[] data, String fileName) {
        Data = data;
        FileName = fileName;
    }
}
