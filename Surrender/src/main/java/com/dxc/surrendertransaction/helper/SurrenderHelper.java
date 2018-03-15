package com.dxc.surrendertransaction.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import com.dxc.surrendertransaction.constants.SurrenderConstants;
import com.dxc.surrendertransaction.request.IndexedData;
import com.dxc.surrendertransaction.request.TranDetails;
import com.dxc.surrendertransaction.request.WMAData;
/*
 * developed by smedisetti4
 * 
 */
public class SurrenderHelper {

	private final static Logger log=Logger.getLogger(SurrenderHelper.class.getName());

	public Properties getConfigProp() throws IOException{
		Properties Config_properties = new Properties();
		InputStream fileInput=this.getClass().getResourceAsStream("/config.properties");
		Config_properties.load(fileInput);
		return Config_properties;
	}

	public Properties getProp() {
		Properties Service_properties=new Properties();
		log.info("Fetching paths from Properties file ::");
		try {
			FileInputStream Service_InputStream = new FileInputStream(new File(getConfigProp().getProperty("Service_Properties_Location")));
			log.info("Service Properties loaded from file: "+getConfigProp().getProperty("Service_Properties_Location"));
			Service_properties.load(Service_InputStream);
		} catch (IOException e) {
			log.error("Error occured while retreiving a Config Property value");
			e.printStackTrace();
		}
		return Service_properties;
	}

	public String getTimeStamp(){
		log.info("Getting TimeStamp ::");
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		log.info("Time Stamp :: "+formatter.format(new Date()));
		return formatter.format(new Date());	
	}

	public boolean createTxtFile(WMAData wdata){
		log.info("createTxtFile starting...");
		Properties Service_properties=getProp();
		WMAData wmdata=new WMAData();
		wmdata.setTransExeDate(wdata.getTransExeDate());
		HashMap<String,String> wmaDataMap=getWMAdataMap(wdata);
		String txt_file_Path;
		Iterator<String> wmaDataKeys= wmaDataMap.keySet().iterator();
		FileWriter writer;
		BufferedWriter bwriter;

		try {
			txt_file_Path=Service_properties.getProperty("Text_File_Path")+getTimeStamp()+".txt";
			log.info("AbsolutePath of the surrender txt file to be created:  "+txt_file_Path);
			File f=new File(txt_file_Path);
			writer=new FileWriter(f);
			bwriter=new BufferedWriter(writer);
			String key;
			while(wmaDataKeys.hasNext()){
				key=wmaDataKeys.next();	
				System.out.println(key);	
				bwriter.write(String.format("%20s \t %20s \r\n",key.toUpperCase(),wmaDataMap.get(key)));
				bwriter.newLine();
				bwriter.newLine();
			}
			bwriter.flush();
			bwriter.close();
			this.setText_file_path(f.getAbsolutePath());
		} catch (FileNotFoundException e) {
			log.error("contains file not found exception ::"+e.getMessage());
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			log.error("contains IO exception ::"+e.getMessage());
			e.printStackTrace();
			return false;
		}
		log.info("cretaeTextFile ending.");
		return true;
	}
	public boolean createXmlFile(IndexedData data,String text_file_path){
		log.info("createXmlFile starting...");
		String xml_file_path;
		Properties prop;
		try{
			prop = getProp();
			Document outdoc=DocumentHelper.createDocument();
			Element awd=outdoc.addElement("AWDRIP");
			Element tr=awd.addElement("Transaction");
			tr.addElement("field").addAttribute("name", SurrenderConstants.IDX_POL_NUMBER).addAttribute("value", data.getPolNumber());
			tr.addElement("field").addAttribute("name", SurrenderConstants.IDX_FIRST_NAME).addAttribute("value", data.getFirstName());
			tr.addElement("field").addAttribute("name", SurrenderConstants.IDX_LAST_NAME).addAttribute("value", data.getLastName());
			tr.addElement("field").addAttribute("name", SurrenderConstants.IDX_GOVT_ID).addAttribute("value", data.getGovtID());
			tr.addElement("field").addAttribute("name", SurrenderConstants.IDX_TRANS_SUB_TYPE).addAttribute("value", data.getTransSubType());
			tr.addElement("field").addAttribute("name", SurrenderConstants.IDX_LINE_OF_BUSINESS).addAttribute("value", data.getLineOfBusiness());
			tr.addElement("field").addAttribute("name", SurrenderConstants.IDX_PRODUCT_CODE).addAttribute("value", data.getProductCode());
			tr.addElement("field").addAttribute("name",SurrenderConstants.UNIT).addAttribute("value",prop.getProperty("UNIT"));
			tr.addElement("field").addAttribute("name",SurrenderConstants.WRKT).addAttribute("value",prop.getProperty("WRKT"));
			tr.addElement("field").addAttribute("name",SurrenderConstants.STAT).addAttribute("value",prop.getProperty("STAT"));

			Element src=tr.addElement("source");

			src.addElement("field").addAttribute("name",SurrenderConstants.UNIT).addAttribute("value",prop.getProperty("UNIT"));
			src.addElement("field").addAttribute("name",SurrenderConstants.WRKT).addAttribute("value",prop.getProperty("WRKT"));
			src.addElement("field").addAttribute("name",SurrenderConstants.STAT).addAttribute("value",prop.getProperty("STAT"));
			src.addElement("field").addAttribute("name",SurrenderConstants.OBJT).addAttribute("value", prop.getProperty("OBJT"));

			src.addElement("path").addAttribute("name",this.getText_file_path());

			xml_file_path=prop.getProperty("AWDRIP_XML_Path")+getTimeStamp()+".xml";
			log.info("AbsolutePath of the surrender xml to be created:  "+xml_file_path);
			FileOutputStream o=new FileOutputStream(new File(xml_file_path));
			OutputFormat form=OutputFormat.createPrettyPrint();
			XMLWriter writer=new XMLWriter(o,form);
			writer.write(outdoc);
			writer.flush();
			writer.close();
			o.close();
		} catch (FileNotFoundException e) {
			log.error("contains file not found exception ::"+e.getMessage());
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			log.error("contains IO exception ::"+e.getMessage());
			e.printStackTrace();
			return false;
		}
		log.info("createXmlFile ending.");
		return true;
	}

	public boolean wmaDataContainsNull(WMAData wmaData){

		String polNumber = wmaData.getPolNumber();
		String transRefGUID = wmaData.getTransRefGUID();
		String transExeDate = wmaData.getTransExeDate();
		String transExeTime = wmaData.getTransExeTime();
		String transType = wmaData.getTransType();
		String paymentForm= wmaData.getPaymentForm();
		String taxWithholdingType=wmaData.getTaxWithholdingType();
		String firstName=wmaData.getFirstName();
		String lastName=wmaData.getLastName();
		String line1=wmaData.getLine1();
		String city=wmaData.getCity();
		String addressStateTC=wmaData.getAddressStateTC();
		String addressTypeCode=wmaData.getAddressTypeCode();
		String zip=wmaData.getZip();

		if (polNumber == null || polNumber.isEmpty()
				|| transType == null || transType.isEmpty()
				|| transExeDate == null || transExeDate.isEmpty()
				|| transExeTime == null || transExeTime.isEmpty()
				|| firstName == null || firstName.isEmpty()
				|| lastName == null || lastName.isEmpty()
				|| line1 == null || line1.isEmpty()
				|| city == null || city.isEmpty()
				|| paymentForm == null || paymentForm.isEmpty()
				|| addressStateTC == null || addressStateTC.isEmpty()
				|| taxWithholdingType == null || taxWithholdingType.isEmpty()
				|| zip == null || zip.isEmpty()
				|| addressTypeCode == null || addressTypeCode.isEmpty()
				|| transRefGUID == null || transRefGUID.isEmpty()){

			return true;

		}
		return false;
	}

	public boolean indexedDataContainsNull(IndexedData indexedData){

		String polNumber = indexedData.getPolNumber();
		String firstName = indexedData.getFirstName();
		String lastName = indexedData.getLastName();
		String govtId = indexedData.getGovtID();
		String transSubType = indexedData.getTransSubType();
		String lineOfBusiness = indexedData.getLineOfBusiness();
		String productCode = indexedData.getProductCode();

		if (polNumber == null || polNumber.isEmpty()
				|| firstName == null || firstName.isEmpty()
				|| lastName == null || lastName.isEmpty()
				|| govtId == null || govtId.isEmpty()
				|| transSubType == null || transSubType.isEmpty()
				|| lineOfBusiness == null || lineOfBusiness.isEmpty()
				|| productCode == null || productCode.isEmpty()){
			return true;
		}
		return false;
	}

	public boolean transDetailsContainsNull(TranDetails tranDetails){

		String transRefGUID = tranDetails.getTransRefGUID();
		String 	transExeDate=tranDetails.getTransExeDate();

		if (transRefGUID == null || transRefGUID.isEmpty() || transExeDate == null || transExeDate.isEmpty()){
			return true;
		}
		return false;
	}

	private HashMap<String,String> getWMAdataMap(WMAData wdata){

		HashMap<String,String> WMADataMap=new LinkedHashMap<String,String>();

		WMADataMap.put(SurrenderConstants.WMA_POL_NUMBER, wdata.getPolNumber());
		WMADataMap.put(SurrenderConstants.WMA_TRANS_REF_GUID, wdata.getTransRefGUID());
		WMADataMap.put(SurrenderConstants.WMA_TRANS_TYPE, wdata.getTransType());
		WMADataMap.put(SurrenderConstants.WMA_TRANS_EXE_DATE, wdata.getTransExeDate());
		WMADataMap.put(SurrenderConstants.WMA_TRANS_EXE_TIME, wdata.getTransExeTime());
		WMADataMap.put(SurrenderConstants.WMA_FIRST_NAME, wdata.getFirstName());
		WMADataMap.put(SurrenderConstants.WMA_LAST_NAME, wdata.getLastName());
		WMADataMap.put(SurrenderConstants.WMA_ADDRESS_STATE_TC, wdata.getAddressStateTC());
		WMADataMap.put(SurrenderConstants.WMA_ADDRESS_TYPE_CODE, wdata.getAddressTypeCode());
		WMADataMap.put(SurrenderConstants.WMA_TAX_WITH_HOLDING_TYPE, wdata.getTaxWithholdingType());
		WMADataMap.put(SurrenderConstants.WMA_CITY, wdata.getCity());
		WMADataMap.put(SurrenderConstants.WMA_PAYMENT_FORM, wdata.getPaymentForm());
		WMADataMap.put(SurrenderConstants.WMA_ZIP, wdata.getZip());
		WMADataMap.put(SurrenderConstants.WMA_LINE1, wdata.getLine1());

		return WMADataMap;
	}
	public String getText_file_path() {
		return Text_file_path;
	}
	public void setText_file_path(String text_file_path) {
		Text_file_path = text_file_path;
	}
	private String Text_file_path;
}
