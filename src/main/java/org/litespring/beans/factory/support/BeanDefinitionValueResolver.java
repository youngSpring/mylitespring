package org.litespring.beans.factory.support;

import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;

/**
 * 如果必要的话，将一个对象里边的引用也组成对象(目的是生成实例)
 */
public class BeanDefinitionValueResolver {
	private final BeanFactory beanFactory;
	
	public BeanDefinitionValueResolver(
			BeanFactory beanFactory) {

		this.beanFactory = beanFactory;
	}
	
	public Object resolveValueIfNecessary(Object value) {
		
		if (value instanceof RuntimeBeanReference) {
			RuntimeBeanReference ref = (RuntimeBeanReference) value;			
			String refName = ref.getBeanName();			
			Object bean = this.beanFactory.getBean(refName);				
			return bean;
			
		}else if (value instanceof TypedStringValue) {
			return ((TypedStringValue) value).getValue();
		} else{
			//TODO
			throw new RuntimeException("the value " + value +" has not implemented");
		}		
	}
}
