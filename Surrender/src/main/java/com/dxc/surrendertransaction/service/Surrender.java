package com.dxc.surrendertransaction.service;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import com.dxc.surrendertransaction.request.TranDetails;
import com.dxc.surrendertransaction.request.TxSurrender;
/*
 * developed by smedisetti4
 * 
 */
@WebService(serviceName="SurrenderService",portName="SurrenderPort")
public interface Surrender {
	@WebMethod(operationName="Surrender")
	public TranDetails surrenderCreate(@WebParam(name="TxSurrender") TxSurrender txSurrender) ;

}
