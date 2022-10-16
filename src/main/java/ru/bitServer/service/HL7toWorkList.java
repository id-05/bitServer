package ru.bitServer.service;

import java.io.*;
import java.util.Date;
import java.util.Map;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.group.ORM_O01_ORDER_DETAIL;
import ca.uhn.hl7v2.model.v25.group.ORM_O01_PATIENT;
import ca.uhn.hl7v2.model.v25.message.ORM_O01;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import ru.bitServer.dao.BitServerResources;
import ru.bitServer.dao.UserDao;
import ru.bitServer.util.LogTool;

public class HL7toWorkList implements UserDao, ReceivingApplication {

    @Override
    public boolean canProcess(Message message) {
        return true;
    }

    @Override
    public Message processMessage(Message receivedMessage, Map<String, Object> metaData)
            throws HL7Exception {
        LogTool.getLogger().info("HL7toWorkList");
        ORM_O01 axx = (ORM_O01) receivedMessage;
        ORM_O01_PATIENT patient = axx.getPATIENT();
        MSH msh =  axx.getMSH();
        ORM_O01_ORDER_DETAIL order = axx.getORDER().getORDER_DETAIL();

        WorkListRecord wkRec = new WorkListRecord(patient.getPID().getPatientIdentifierList()[0].encode(),
                patient.getPID().getPatientName()[0].encode(),
                patient.getPID().getDateTimeOfBirth().getTime().toString(),
                msh.getMessageControlID().toString(),
                patient.getPID().getAdministrativeSex().toString(),
                msh.getDateTimeOfMessage().getTime().encode(),
                order.getOBR().getObr4_UniversalServiceIdentifier().encode(),
                order.getOBR().getObr24_DiagnosticServSectID().toString());
/*
        PatientId
        PatientName
        DateBirth
        AcceccionNumber
        Sex
        Description
        Modality
*/

        String shortfilename = getBitServerResource("WorkListPath").getRvalue() + new Date().getTime();
        boolean deleteBufFileAfter = getBitServerResource("deleteBufFileAfter").getRvalue().equals("true");
        String filename = shortfilename+".txt";

        BitServerResources bufResources = getBitServerResource("worklistsamplefile");
        String pathToFile = bufResources.getRvalue();

        StringBuilder worklistFile = new StringBuilder();
        try(FileReader reader = new FileReader(pathToFile)) {
            int c;
            while ((c = reader.read()) != -1) {
                worklistFile.append((char) c);
            }
        } catch (Exception e) {
            LogTool.getLogger().warn("Error read luascript file: "+e.getMessage());
        }
        String worklistFileText = worklistFile.toString();

        worklistFileText = worklistFileText.replace("AcceccionNumber",wkRec.getAcceccionNumber());
        worklistFileText = worklistFileText.replace("PatientName",wkRec.getPatientName());
        worklistFileText = worklistFileText.replace("PatientId",wkRec.getPatientId());
        worklistFileText = worklistFileText.replace("DateBirth",wkRec.getDateBirth());
        worklistFileText = worklistFileText.replace("Sex",wkRec.getSex());
        worklistFileText = worklistFileText.replace("Modality",wkRec.getModality());
        worklistFileText = worklistFileText.replace("DateCreate",wkRec.getDateCreate());

        File file = new File(filename);
        FileOutputStream is;
        try {
            is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write(worklistFileText);
            w.close();
        }catch (Exception e){
            LogTool.getLogger().error("Error create wl-file: "+e.getMessage());
        }
//        try {
//            is = new FileOutputStream(file);
//            OutputStreamWriter osw = new OutputStreamWriter(is);
//            Writer w = new BufferedWriter(osw);
//            w.write("# Dicom-File-Format"+"\n");
//            w.write("\n");
//            w.write("# Dicom-Meta-Information-Header"+"\n");
//            w.write("(0002,0000) UL 202"+"\n");
//            w.write("(0002,0001) OB 00\\01"+"\n");
//            w.write("(0002,0002) UI [1.2.276.0.7230010.3.1.0.1]"+"\n");
//            w.write("(0002,0003) UI [1.2.276.0.7230010.3.1.4.2831176407.11154.1448031138.805061]"+"\n");
//            w.write("(0002,0010) UI =LittleEndianExplicit"+"\n");
//            w.write("(0002,0012) UI [1.2.276.0.7230010.3.0.3.6.0]"+"\n");
//            w.write("(0002,0013) SH [OFFIS_DCMTK_360]"+"\n");
//            w.write("\n");
//            w.write("# Dicom-Data-Set"+"\n");
//            w.write("# Used TransferSyntax: Little Endian Explicit"+"\n");
//            w.write("(0008,0005) CS [ISO_IR 100]"+"\n");
//            w.write("(0008,0050) SH ["+wkRec.getAcceccionNumber()+"]"+"\n");
//            w.write("(0010,0010) PN ["+wkRec.getPatientName()+"]"+"\n");
//            w.write("(0010,0020) LO ["+wkRec.getPatientId()+"]"+"\n");
//            w.write("(0010,0030) DA ["+wkRec.getDateBirth()+"]"+"\n");
//            w.write("(0010,0040) CS ["+wkRec.getSex()+"]"+"\n");
//            w.write("(0008,0060) CS ["+wkRec.getModality()+"]"+"\n");
//            w.write("(0008,0020) CS ["+wkRec.getDateCreate()+"]"+"\n");
//            w.close();
//
//        } catch (Exception e) {
//            LogTool.getLogger().error("Error create wl-file: "+e.getMessage());
//        }

        try {
            Process proc = Runtime.getRuntime().exec("dump2dcm "+filename+" "+shortfilename+".wl");
            LogTool.getLogger().info("new file: "+new Date()+": "+shortfilename);
            proc.waitFor();

            if(deleteBufFileAfter){
                if(!file.delete()){
                    LogTool.getLogger().error("Error delete wl-file, maybe tomcat not have rules");
                }
            }

        }catch (Exception e){
            LogTool.getLogger().error("Error create wl-file: "+e.getMessage());
        }

        try {
            return receivedMessage.generateACK();
        } catch (IOException e) {
            LogTool.getLogger().error("Error generateACK");
            throw new HL7Exception(e);
        }

    }

}
