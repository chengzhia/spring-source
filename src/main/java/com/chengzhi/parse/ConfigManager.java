package com.chengzhi.parse;

import com.chengzhi.config.Bean;
import com.chengzhi.config.Property;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @param
 * @author RCZ
 * @create 2017-12-03 14:09
 */
public class ConfigManager {
    /**
     * 使用dom4j和xpath获取所有的bean和property
     * 封装到map中
     * @return
     */
    public static Map<String,Bean> getConfig(String path) {
        Map<String,Bean> map = new HashMap<String, Bean>();
        SAXReader reader = new SAXReader();
        InputStream inputStream = ConfigManager.class.getResourceAsStream(path);
        Document document = null;
        try {
            document = reader.read(inputStream);
        } catch (DocumentException e) {
            throw new RuntimeException("请检查xml配置文件");
        }
        String xpath = "//bean";
        List<Element> list = document.selectNodes(xpath);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Bean bean = new Bean();
                Element element = list.get(i);
                String name = element.attributeValue("name");
                String value = element.attributeValue("value");
                bean.setName(name);
                bean.setClassName(value);
                List<Element> childs = element.elements("property");
                if (childs != null) {
                    for (int j = 0; j < childs.size(); j++) {
                        Property property = new Property();
                        Element chlid = childs.get(j);
                        property.setName(chlid.attributeValue("name"));
                        property.setRef(chlid.attributeValue("value"));
                        property.setValue(chlid.attributeValue("ref"));
                        bean.getProperties().add(property);
                    }
                }
                map.put(name,bean);
            }
        }
        return map;
    }
}
