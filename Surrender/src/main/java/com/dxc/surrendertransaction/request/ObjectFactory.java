//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.26 at 04:32:48 PM IST 
//


package com.dxc.surrendertransaction.request;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.dxc.surrendertransaction.request package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _TxSurrender_QNAME = new QName("http://www.dxc.com/request/", "TxSurrender");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.dxc.surrendertransaction.request
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TxSurrender }
     * 
     */
    public TxSurrender createTxSurrender() {
        return new TxSurrender();
    }

    /**
     * Create an instance of {@link IndexedData }
     * 
     */
    public IndexedData createIndexedData() {
        return new IndexedData();
    }

    /**
     * Create an instance of {@link TranDetails }
     * 
     */
    public TranDetails createTranDetails() {
        return new TranDetails();
    }

    /**
     * Create an instance of {@link WMAData }
     * 
     */
    public WMAData createWMAData() {
        return new WMAData();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TxSurrender }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.dxc.com/request/", name = "TxSurrender")
    public JAXBElement<TxSurrender> createTxSurrender(TxSurrender value) {
        return new JAXBElement<TxSurrender>(_TxSurrender_QNAME, TxSurrender.class, null, value);
    }

}
