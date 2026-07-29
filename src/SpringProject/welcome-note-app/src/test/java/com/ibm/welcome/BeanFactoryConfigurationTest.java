package com.ibm.welcome;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BeanFactoryConfigurationTest {

    @Test
    void shouldLoadStudentBeanFromXmlConfiguration() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(new ClassPathResource("bean-factory-demo.xml"));

        Student student = beanFactory.getBean("student", Student.class);

        assertNotNull(student);
        assertEquals("Aarav", student.getName());
        assertEquals(20, student.getAge());
    }
}
