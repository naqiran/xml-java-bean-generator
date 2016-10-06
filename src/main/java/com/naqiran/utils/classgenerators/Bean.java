package com.naqiran.utils.classgenerators;

import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bean")
@XmlAccessorType(XmlAccessType.FIELD)
public class Bean {
	
	@XmlAttribute(required=true)
	private String name;
	
	@XmlAttribute(required=true)
	private String packageName;
	
	@XmlAttribute
	private BeanType type;
	
	@XmlAttribute
	private String implementsName;
	
	@XmlAttribute
	private String extendsName;
	
	@XmlElementWrapper(name="attributes")
	@XmlElement(name="attribute")
	private List<Attribute> attributes;
	
	private Set<String> imports;
	
	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public String getImplementsName() {
		return implementsName;
	}

	public void setImplementsName(String implementsName) {
		this.implementsName = implementsName;
	}

	public String getExtendsName() {
		return extendsName;
	}

	public void setExtendsName(String extendsName) {
		this.extendsName = extendsName;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public BeanType getType() {
		return type;
	}

	public void setType(BeanType type) {
		this.type = type;
	}

	public Set<String> getImports() {
		return imports;
	}

	public void setImports(Set<String> imports) {
		this.imports = imports;
	}
}
