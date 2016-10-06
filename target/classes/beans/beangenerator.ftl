<#if packageName??>package ${packageName};</#if>

<#-- Imports -->
<#list imports! as import>
import ${import};
</#list>

public class ${name}<#if extendsName??> extends ${extendsName?keep_after_last(".")}</#if><#if implementsName??> implements ${implementsName?keep_after_last(".")}</#if> {
<#-- Field -->

<#list attributes! as attribute>
	<#if attribute.fieldAnnotations??>@${attribute.fieldAnnotations}</#if>
	private ${attribute.type?keep_after_last(".")} ${attribute.name};
</#list>
	
<#-- Accessors and Mutators -->
<#list attributes! as attribute>
	public ${attribute.type?keep_after_last(".")} get${attribute.name?cap_first}(){
		return ${attribute.name};
	}
		
	public void set${attribute.name?cap_first}(${attribute.type?keep_after_last(".")} ${attribute.name}){
		this.${attribute.name} = ${attribute.name};
	}
</#list>
}