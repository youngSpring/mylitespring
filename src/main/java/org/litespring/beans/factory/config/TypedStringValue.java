package org.litespring.beans.factory.config;

/**
 * 这个代表的是值
 * <bean id="petStore"
 *         class="org.litespring.service.v2.PetStoreService" >
 *     <property name="owner" 是这个：value="liuxin"/>
 */
public class TypedStringValue {
	private String value;
	public TypedStringValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return this.value;
	}
}
