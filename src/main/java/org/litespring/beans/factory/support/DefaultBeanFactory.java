package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 将xml中的bean都注册到了beanDefinitionMap中
 */
public class DefaultBeanFactory extends DefaultSingletonBeanRegistry 
	implements ConfigurableBeanFactory,BeanDefinitionRegistry {

	
	
	private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(64);
	private ClassLoader beanClassLoader;
	
	public DefaultBeanFactory() {
		
	}

	/**
	 * 这时候注册进去的还只是definition而不是对应的类的对象
	 * @param beanID
	 * @param bd
	 */
	public void registerBeanDefinition(String beanID,BeanDefinition bd){
		this.beanDefinitionMap.put(beanID, bd);
	}
	public BeanDefinition getBeanDefinition(String beanID) {
			
		return this.beanDefinitionMap.get(beanID);
	}

	/**
	 * 使用bean
	 * @param beanID
	 * @return
	 */
	public Object getBean(String beanID) {
		//从map中取，这时候已经全注册进去了
		BeanDefinition bd = this.getBeanDefinition(beanID);
		if(bd == null){
			return null;
		}
		//看一下这个是不是应该是单例
		if(bd.isSingleton()){
			//如果是的话则从单例中取
			Object bean = this.getSingleton(beanID);
			if(bean == null){
				//没有的话则创建，注册到单例中
				bean = createBean(bd);
				this.registerSingleton(beanID, bean);
			}
			return bean;
		}
		//不是单例的话则直接生成bean
		return createBean(bd);
	}
	private Object createBean(BeanDefinition bd) {
		//创建实例
		Object bean = instantiateBean(bd);
		//设置属性
		populateBean(bd, bean);
		
		return bean;		
		
	}

	/**
	 * 根据全限定名生成对象
	 * @param bd
	 * @return
	 */
	private Object instantiateBean(BeanDefinition bd) {
		ClassLoader cl = this.getBeanClassLoader();
		String beanClassName = bd.getBeanClassName();
		try {
			Class<?> clz = cl.loadClass(beanClassName);
			return clz.newInstance();
		} catch (Exception e) {			
			throw new BeanCreationException("create bean for "+ beanClassName +" failed",e);
		}	
	}

	/**
	 * 根据里边的信息，组装这个对象，用了反射的方法
	 * @param bd 还是这个对象的definition，里边保存了这个了类的信息
	 * @param bean 生成的对象
	 */
	protected void populateBean(BeanDefinition bd, Object bean){
		List<PropertyValue> pvs = bd.getPropertyValues();
		
		if (pvs == null || pvs.isEmpty()) {
			return;
		}
		
		BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);
		SimpleTypeConverter converter = new SimpleTypeConverter(); 
		try{
			
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			
			for (PropertyValue pv : pvs){
				String propertyName = pv.getName();
				Object originalValue = pv.getValue();
				Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);			
				
				for (PropertyDescriptor pd : pds) {
					if(pd.getName().equals(propertyName)){
						Object convertedValue = converter.convertIfNecessary(resolvedValue, pd.getPropertyType());
						pd.getWriteMethod().invoke(bean, convertedValue);
						break;
					}
				}
 
				
			}
		}catch(Exception ex){
			throw new BeanCreationException("Failed to obtain BeanInfo for class [" + bd.getBeanClassName() + "]", ex);
		}	
	}

	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}

    public ClassLoader getBeanClassLoader() {
		return (this.beanClassLoader != null ? this.beanClassLoader : ClassUtils.getDefaultClassLoader());
	}
}
