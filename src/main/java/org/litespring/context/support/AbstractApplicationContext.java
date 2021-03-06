package org.litespring.context.support;

import org.litespring.beans.factory.NoSuchBeanDefinitionException;
import org.litespring.beans.factory.annotation.AutowiredAnnotationProcessor;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.context.ApplicationContext;
import org.litespring.core.io.Resource;
import org.litespring.util.ClassUtils;

public abstract class AbstractApplicationContext implements ApplicationContext {

	private DefaultBeanFactory factory = null;
	private ClassLoader beanClassLoader;
	
	public AbstractApplicationContext(String configFile){
		factory = new DefaultBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		Resource resource = this.getResourceByPath(configFile);
		reader.loadBeanDefinitions(resource);
		factory.setBeanClassLoader(this.getBeanClassLoader());
		registerBeanPostProcessors(factory);
	}

	/**
	 * 将这些东西组装起来给factory,则factory可以生产出bean来
	 * @param beanID
	 * @return
	 */
	public Object getBean(String beanID) {
		
		return factory.getBean(beanID);
	}
	
	protected abstract Resource getResourceByPath(String path);
	
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}

    public ClassLoader getBeanClassLoader() {
		return (this.beanClassLoader != null ? this.beanClassLoader : ClassUtils.getDefaultClassLoader());
	}
	protected void registerBeanPostProcessors(ConfigurableBeanFactory beanFactory) {

		AutowiredAnnotationProcessor postProcessor = new AutowiredAnnotationProcessor();
		postProcessor.setBeanFactory(beanFactory);
		beanFactory.addBeanPostProcessor(postProcessor);

	}
	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		return this.factory.getType(name);
	}

}
