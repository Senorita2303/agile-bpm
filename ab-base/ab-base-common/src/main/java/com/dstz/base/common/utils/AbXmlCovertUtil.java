package com.dstz.base.common.utils;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @Name AbXmlCovertUtil
 * @description: xml pojo conversion tool class
 */
public class AbXmlCovertUtil {
    /**
     * XML to POJO type
     */
    public static <T> T covert2Object(String xml, Class<?>... classes) throws JAXBException, SAXException {
        JAXBContext jAXBContext = JAXBContext.newInstance(classes);

        // Create an XMLReader and disable external entities
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        // Return an empty string to prevent entity resolution
        xmlReader.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));

        SAXSource saxSource = new SAXSource(xmlReader, new InputSource(new StringReader(xml)));

        Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();
        return CastUtils.cast(unmarshaller.unmarshal(saxSource));
    }

    /**
     * POJO type conversion to XML
     */
    public static String covert2Xml(Object serObj) throws JAXBException  {
        JAXBContext jc = JAXBContext.newInstance(serObj.getClass());

        StringWriter out = new StringWriter();
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
        m.marshal(serObj, out);
        String tmp = out.toString();
        return tmp;
    }
}
