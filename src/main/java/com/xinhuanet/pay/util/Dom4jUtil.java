package com.xinhuanet.pay.util;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 采用DOM4J方式
 * 
 * @author TigerLee
 * 
 */
public class Dom4jUtil {
	
	private Logger logger = Logger.getLogger(Dom4jUtil.class);
	
	/**
	 * 获取doc对象（org.dom4j.Document）
	 * 
	 * @param fileName
	 * @return
	 */
	public Document getDocument(String fileName) {
		SAXReader sr = new SAXReader();
		Document doc = null;
		try {
			doc = sr.read(new File(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public Document getDocument(InputStream is){
		SAXReader sr = new SAXReader();
		Document doc = null;
		try {
			doc = sr.read(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 将文件保存到硬盘
	 * 
	 * @param doc
	 * @param fileName
	 */
	public void writeToFile(org.dom4j.Document doc, String fileName) {
		try {
			Writer writer = new FileWriter(fileName);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			XMLWriter xmlWriter = new XMLWriter(writer, format);
			xmlWriter.write(doc);
			xmlWriter.close();
			logger.debug(" 文件已经钝化！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输出文档节点值
	 * @param doc
	 * @param path
	 * @return
	 */
	public List<String> getPathValue(Document doc, String path){
		List<String> valueList = doc.selectNodes(path);
		return valueList;
	}
	
	/**
	 * 遍历xml文件输出节点值
	 * @Deprecated
	 * @param fileName
	 */
	@Deprecated
	public String readXml(String fileName) {
		org.dom4j.Document doc = getDocument(fileName);
		List nameList = doc.selectNodes("/students/student/name");
		List ageList = doc.selectNodes("/students/student/age");
		List sexList = doc.selectNodes("/students/student/sex");
		List addressList = doc.selectNodes("/students/student/address");
		String xmlContent = null;
		for (int i = 0; i < nameList.size(); i++) {
			StringBuilder sb = new StringBuilder();
			sb.append("name=" + ((Element) nameList.get(i)).getTextTrim());
			sb.append(",age=" + ((Element) ageList.get(i)).getTextTrim());
			sb.append(",sex=" + ((Element) sexList.get(i)).getTextTrim());
			sb.append(",address="
					+ ((Element) addressList.get(i)).getTextTrim());
			xmlContent = sb.toString();
			logger.debug(xmlContent);
		}
		return xmlContent;
	}

	/**
	 * 根据姓名修改一个学生的信息
	 * @Deprecated
	 * @param name
	 * @param newValue
	 * @param fileName
	 */
	@Deprecated
	public void updateXml(String name, Map newValue, String fileName) {
		org.dom4j.Document doc = getDocument(fileName);
		List nameList = doc.selectNodes("/students/student/name");
		Iterator iterator = nameList.iterator();

		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();

			if (name != null && name.equals(element.getText())) {
				Element pe = element.getParent();
				List childList = pe.elements();
				for (int i = 0; i < childList.size(); i++) {
					Iterator valueSet = newValue.entrySet().iterator();
					while (valueSet.hasNext()) {
						Map.Entry entry = (Map.Entry) valueSet.next();
						String nodeName = ((Element) childList.get(i))
								.getName();
						String key = entry.getKey().toString();
						if (key != null && key.equals(nodeName)) {
							((Element) childList.get(i)).setText((String) entry
									.getValue());
						}
					}
				}
			}
		}
		writeToFile(doc, fileName);
	}
}
