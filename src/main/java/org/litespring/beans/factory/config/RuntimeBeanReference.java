package org.litespring.beans.factory.config;

/**
 * 这个代表的是对象
 *  <bean id="petStore"
 *         class="org.litespring.service.v2.PetStoreService" >
 *   	<property name="accountDao" 是这个：ref="accountDao"/>
 */
public class RuntimeBeanReference {
	private final String beanName;
	public RuntimeBeanReference(String beanName) {
		this.beanName = beanName;
	}
	public String getBeanName() {
		return this.beanName;
	}
}
