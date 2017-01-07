package net.logvv.raven.utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.security.InvalidParameterException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;

public final class JaxbUtils {

	private JaxbUtils() {

	};

	public static String obj2xml(Object o, Class<?> clazz) throws JAXBException {
		
		if(null == o){
			throw new InvalidParameterException();
		}
		JAXBContext context = JAXBContext.newInstance(clazz);
		
		Marshaller marshaller = context.createMarshaller();
		
		// 
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		// 是否去掉头部信息
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

		StringWriter writer = new StringWriter();
		marshaller.marshal(o, writer);
		return writer.toString();
	}
	
	public static Object xml2obj(String xmlContent, Class<?> clazz) throws JAXBException {
		
		if(StringUtils.isEmpty(xmlContent)){
			throw new InvalidParameterException();
		}
		
		JAXBContext context = JAXBContext.newInstance(clazz);

		Unmarshaller unmarshaller = context.createUnmarshaller();
		
		StringReader sr = new StringReader(xmlContent);
		
		return unmarshaller.unmarshal(sr);
		
	}

}
