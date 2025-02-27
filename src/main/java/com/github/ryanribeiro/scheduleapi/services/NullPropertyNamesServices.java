package com.github.ryanribeiro.scheduleapi.services;

import org.springframework.stereotype.Service;

@Service
public class NullPropertyNamesServices {

	@SuppressWarnings("null")
	public String[] getNullPropertyNames(Object source) {
	    return java.util.Arrays.stream(org.springframework.beans.BeanUtils.getPropertyDescriptors(source.getClass()))
	            .map(java.beans.PropertyDescriptor::getName)
	            .filter(name -> {
	                try {
	                    return org.springframework.beans.BeanUtils.getPropertyDescriptor(source.getClass(), name).getReadMethod().invoke(source) == null;
	                } catch (Exception e) {
	                    return false;
	                }
	            }).toArray(String[]::new);
	}
}
