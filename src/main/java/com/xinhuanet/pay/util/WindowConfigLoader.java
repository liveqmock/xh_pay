package com.xinhuanet.pay.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.xinhuanet.pay.po.WindowConfig;

public class WindowConfigLoader {
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private static WindowConfigLoader instance;
	private static final String RESOURCE_URI = "/page_conf.xml";
	protected final Logger logger = Logger.getLogger(getClass());
	private ConcurrentHashMap<String,Map<String, WindowConfig>> config = null;

	private WindowConfigLoader() {
		config = load();
	}

	public static WindowConfigLoader getInstance() {
		if (instance == null) {
			instance = new WindowConfigLoader();
			return instance;
		}
		return instance;
	}

	protected ConcurrentHashMap<String,Map<String, WindowConfig>> load() {
		lock.writeLock().lock();
		ConcurrentHashMap<String,Map<String, WindowConfig>> globalConf = new ConcurrentHashMap<String,Map<String, WindowConfig>>();
		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		InputStream is = getClass().getResourceAsStream(RESOURCE_URI);
		try {
			DocumentBuilder dombuilder = domfac.newDocumentBuilder();
			Document doc = dombuilder.parse(is);
			Element root = doc.getDocumentElement();
			String rootNodeName = root.getNodeName();
			logger.debug(rootNodeName);
			// configuration
			if (!"configuration".equalsIgnoreCase(rootNodeName)) {
				return null;
			}
			NodeList childNodes = root.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node node = childNodes.item(i);
				// page
				if (node.getNodeType() == Node.ELEMENT_NODE
						&& "page".equalsIgnoreCase(node.getNodeName())) {
					ConcurrentHashMap<String, WindowConfig> pageConf = new ConcurrentHashMap<String, WindowConfig>();
					String id = node.getAttributes().getNamedItem("id")
							.getNodeValue();
					logger.debug("page id:"+id);
					String uri = node.getAttributes().getNamedItem("uri")
							.getNodeValue();
					logger.debug("page uri"+uri);
					// window
					NodeList subNodes = node.getChildNodes();
					for (int j = 0; j < subNodes.getLength(); j++) {
						Node subNode = subNodes.item(j);
						if (subNode.getNodeType() == Node.ELEMENT_NODE
								&& "window".equalsIgnoreCase(subNode
										.getNodeName())) {
							NodeList attrList = subNode.getChildNodes();
							String wid = subNode.getAttributes().getNamedItem("id").getNodeValue();
							//attr
							for(int k=0;k<attrList.getLength();k++){
								Node attrNode = attrList.item(k);
								if(attrNode.getNodeType() == Node.ELEMENT_NODE && "property".equalsIgnoreCase(attrNode.getNodeName())){
									String name = attrNode.getAttributes().getNamedItem("name").getNodeValue();
									String value = attrNode.getAttributes().getNamedItem("value").getNodeValue();
									logger.debug("property:"+name+"@"+value);
									WindowConfig wc = new WindowConfig(wid,value);
									pageConf.put(wid, wc);
								}
							}
						}
					}
					globalConf.put(id, pageConf);
				}
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lock.writeLock().unlock();
		logger.debug(globalConf);
		return globalConf;
	}

}
