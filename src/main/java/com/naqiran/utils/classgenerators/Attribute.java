package com.naqiran.utils.classgenerators;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="attribute")
@XmlAccessorType(XmlAccessType.FIELD)
public class Attribute {
	
	@XmlAttribute(required=true)
	private String type;
	
	@XmlAttribute(required=true)
	private String name;
	
	@XmlAttribute
	private String fieldAnnotations;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFieldAnnotations() {
		return fieldAnnotations;
	}
	public void setFieldAnnotations(String fieldAnnotations) {
		this.fieldAnnotations = fieldAnnotations;
	}
}