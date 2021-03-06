package com.lagou.config;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration =configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException {

        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");

        // Element = <select></select> block
        List<Element> selectNodes = rootElement.selectNodes("//select");
        configureMappedStatementMap(selectNodes, namespace);

        // Element = <insert></insert> block
        List<Element> insertNodes = rootElement.selectNodes("//insert");
        configureMappedStatementMap(insertNodes, namespace);

        // Element = <delete></delete> block
        List<Element> deleteNodes = rootElement.selectNodes("//delete");
        configureMappedStatementMap(deleteNodes, namespace);

        // Element = <update></update> block
        List<Element> updateNodes = rootElement.selectNodes("//update");
        configureMappedStatementMap(updateNodes, namespace);
    }

    private void configureMappedStatementMap(List<Element> list, String namespace) {
        for (Element element : list) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String parameterType = element.attributeValue("parameterType");
            String sqlText = element.getTextTrim();

            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setResultType(resultType);
            mappedStatement.setParameterType(parameterType);
            mappedStatement.setSql(sqlText);

            String key = namespace+"."+id;
            configuration.getMappedStatementMap().put(key,mappedStatement);
        }
    }
}
