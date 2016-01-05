/*
 * Copyright (C) 2015 WTFDYUM
 *
 * This file is part of the WTFDYUM project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jeanchampemont.wtfdyum.config;

import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * The Class DozerConfiguration.
 */
@Configuration
public class DozerConfiguration {

    /**
     * Dozer bean mapper factory bean.
     *
     * @return the dozer bean mapper factory bean
     */
    @Bean
    public DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean() {
        final DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean = new DozerBeanMapperFactoryBean();
        dozerBeanMapperFactoryBean.setMappingBuilders(Arrays.asList(beanMappingBuilder()));
        return dozerBeanMapperFactoryBean;
    }

    /**
     * Bean mapping builder.
     *
     * @return the bean mapping builder
     */
    private BeanMappingBuilder beanMappingBuilder() {
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                // add mappings here
            }
        };
    }
}
