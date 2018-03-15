package com.dxc.surrendertransaction.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.dxc.surrendertransaction.constants.SurrenderConstants;
import com.dxc.surrendertransaction.helper.SurrenderHelper;
import com.dxc.surrendertransaction.request.IndexedData;
import com.dxc.surrendertransaction.request.TranDetails;
import com.dxc.surrendertransaction.request.TxSurrender;
import com.dxc.surrendertransaction.request.WMAData;
/*
 * developed by smedisetti4
 * 
 */
@WebService(endpointInterface="com.dxc.surrendertransaction.service.Surrender", serviceName="SurrenderService",portName = "SurrenderPort")
public class SurrenderService implements Surrender {

	private	 static final Logger log=Logger.getLogger(SurrenderService.class.getName());

	SurrenderHelper surrenderHelper=new SurrenderHelper();
	String textFilePath;

	public  SurrenderService() {
		log.info("Log4j properties file loading starting...");
		Properties Config_properties = new Properties();
		InputStream fileInput=this.getClass().getResourceAsStream("/config.properties");
		try {
			Config_properties.load(fileInput);
			PropertyConfigurator.configure(Config_properties.getProperty("Log4jProperties_Location"));
			System.out.println(Config_properties.getProperty("Log4jProperties_Location"));
		} catch (IOException e) {
			log.error("Error occured while retreiving a log4j Property value"+e.getMessage());
			e.printStackTrace();
		}
	}
	@WebMethod
	public TranDetails surrenderCreate(@WebParam(name="TxSurrender") TxSurrender txSurrender)  {
		log.info("surrenderCreate  method starting..");
		IndexedData indexedData = txSurrender.getIndexedData();
		TranDetails tranInfo = txSurrender.getTranDetails();
		WMAData wmaData = txSurrender.getWMAData();
		boolean validate=surrenderHelper.indexedDataContainsNull(indexedData);
		log.info("Indexed data  null checking return is ::"+validate);
		if(validate == true) {
			log.info("Indexed Data contains null or empty values :");
			tranInfo.setTranID(tranInfo.getTransRefGUID());
			tranInfo.setTransExeDate(wmaData.getTransExeDate());
			tranInfo.setTransExeTime(wmaData.getTransExeTime());
			tranInfo.setTransType(wmaData.getTransType());
			tranInfo.setResultCode(SurrenderConstants.MSGCODEFAILURE);
			tranInfo.setResultDescription(SurrenderConstants.MSG_DESCRIPTION_FAILURE);
		}
		else{
			boolean txtFile=surrenderHelper.createTxtFile(wmaData);
			log.info("creteTxtfile return data value is ::"+txtFile);
			if(txtFile == true){
				log.info("Txt file creaed successfully.");
				textFilePath=surrenderHelper.getText_file_path();

				boolean xmlFile=surrenderHelper.createXmlFile(indexedData,textFilePath);
				log.info("creteXmlfile return data value is ::"+xmlFile);
				if(xmlFile == true){
					log.info("xml file created successfully.");
					tranInfo.setTranID(tranInfo.getTransRefGUID());
					tranInfo.setTransExeDate(wmaData.getTransExeDate());
					tranInfo.setTransExeTime(wmaData.getTransExeTime());
					tranInfo.setTransType(wmaData.getTransType());
					tranInfo.setResultCode(SurrenderConstants.MSGCODESUCCESS);
					tranInfo.setResultDescription(SurrenderConstants.MSG_DESCRIPTION_SUCCESS);
				}
			}
		}
		log.info("surrenderCreate method end.");
		return tranInfo;
	}
}
