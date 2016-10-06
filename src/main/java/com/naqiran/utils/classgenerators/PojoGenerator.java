package com.naqiran.utils.classgenerators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class PojoGenerator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PojoGenerator.class);
	
	public Beans parseBeansConfiguration(String beansConfig){
		return parseBeansConfiguration(beansConfig, false);
	}
	
	public Beans parseBeansConfiguration(String beansConfig,boolean validate){
		Beans beans = null;
		try {
			JAXBContext context = JAXBContext.newInstance(Beans.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			if(validate){
				validate(unmarshaller);
			}
			Source dataSource = new StreamSource(new File(beansConfig));
			JAXBElement<Beans> beansElement = unmarshaller.unmarshal(dataSource, Beans.class);
			beans = beansElement.getValue();
		} catch (JAXBException e) {
			LOGGER.error("JAXB Exception",e);
		}
		return beans;
	}
	
	public void validate(Unmarshaller unmarshaller){
		try{
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			StreamSource source = new StreamSource(ClassLoader.getSystemResourceAsStream(ClassGeneratorConstants.VALIDATOR_SCHEMA_LOCATION));
			Schema schema = schemaFactory.newSchema(source);
			unmarshaller.setSchema(schema);
		} catch (SAXException saxe){
			LOGGER.error("Invalid Source File - Error in parsing",saxe);
		}
	}
	
	public void generateClasses(String configFile, String destFolder){
		Beans beans = parseBeansConfiguration(configFile);
		if(beans != null){
			List<Bean> beanList = beans.getBeans();
			if(CollectionUtils.isNotEmpty(beanList)){
				for(Bean bean:beanList){
					generateClass(bean,destFolder);
				}
			}
		}
	}
	
	public void generateClass(Bean bean, String folderName){
		Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		try {
			preprocessBean(bean);
			configuration.setClassLoaderForTemplateLoading(ClassLoader.getSystemClassLoader(),ClassGeneratorConstants.BASE_PACKAGE);
			Template template = configuration.getTemplate(ClassGeneratorConstants.TEMPLATE_NAME);
			String packageFolder = bean.getPackageName() != null ? bean.getPackageName().replace(ClassGeneratorConstants.PERIOD_SEPARTOR, File.separatorChar) + File.separator : StringUtils.EMPTY;
			StringBuilder fileName = new StringBuilder().append(folderName).append(packageFolder);
			File gendirectory = new File(fileName.toString());
			gendirectory.mkdirs();
			fileName.append(bean.getName()).append(ClassGeneratorConstants.FILE_EXTENSION);
			Writer out = new FileWriter(fileName.toString());
			template.process(bean,out);
		} catch (IOException e) {
			LOGGER.error("I/O Exception",e);
		} catch (TemplateException e) {
			LOGGER.error("Template Exception",e);
		} 
	}
	
	private void preprocessBean(Bean bean){
		//Collect imports.
		List<Attribute> attributes = bean.getAttributes();
		Set<String> imports = new TreeSet<String>();
		if(!CollectionUtils.isEmpty(attributes)){
			for(Attribute attribute:attributes){
				addImports(imports,attribute.getType());
				String fieldAnnotations = attribute.getFieldAnnotations();
				if(StringUtils.isNotBlank(fieldAnnotations)){
					String[] fieldAnnotationArray = fieldAnnotations.split(ClassGeneratorConstants.COMMA_SEPARATOR);
					for(String fieldAnnotation :fieldAnnotationArray){
						int lastAnnotationPos = StringUtils.lastIndexOf(fieldAnnotation, ClassGeneratorConstants.ROUND_BRACE);
						if(lastAnnotationPos < 0){
							lastAnnotationPos = fieldAnnotation.length();
						}
						addImports(imports,StringUtils.substring(fieldAnnotation,0,lastAnnotationPos));
					}
				}
				
			}
		}
		
		if(StringUtils.isNotEmpty(bean.getExtendsName())){
			String[] extendsString = bean.getExtendsName().split(ClassGeneratorConstants.COMMA_SEPARATOR);
			for(String extendsStr:extendsString){
				addImports(imports,extendsStr);
			}
		}
		
		if(StringUtils.isNotEmpty(bean.getImplementsName())){
			String[] extendsString = bean.getImplementsName().split(ClassGeneratorConstants.COMMA_SEPARATOR);
			for(String extendsStr:extendsString){
				addImports(imports,extendsStr);
			}
		} 
		
		bean.setImports(imports);
	}
	
	private void addImports(Set<String> importSet, String imports){
		if(!StringUtils.isEmpty(imports) && !imports.startsWith(ClassGeneratorConstants.JAVA_LANG_PACKAGE)){
			importSet.add(imports);
		}
	}
}