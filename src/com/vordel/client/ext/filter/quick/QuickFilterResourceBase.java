package com.vordel.client.ext.filter.quick;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.vordel.common.CompositeResourceBundle;
import com.vordel.common.ResourceBase;
import com.vordel.es.Entity;

public class QuickFilterResourceBase extends ResourceBase {
	private Map<String, ResourceBundle> resourcesMap = new HashMap<String, ResourceBundle>();
	private Entity definition;

	public QuickFilterResourceBase(Entity definition, Class<?> clazz, String resourcesFile) {
		super(clazz, resourcesFile);
		
		this.definition = definition;
	}

	public QuickFilterResourceBase(Entity definition, Class<?> clazz) {
		super(clazz);
		
		this.definition = definition;
	}

	public QuickFilterResourceBase(Entity definition, String packageName) {
		super(packageName);
		
		this.definition = definition;
	}
	
	
	private PropertyResourceBundle getDefinitionProperties() {
		String value = definition == null ? null : definition.getStringValue("resources");
		PropertyResourceBundle resources = null;
		
		if (value != null) {
			try {
				StringReader reader = new StringReader(value);
				
				resources = new PropertyResourceBundle(reader);
			} catch (IOException e) {
				resources = null;
			}
		}
		
		return resources;
	}

	@Override
	public ResourceBundle getBundle(String language) {
		ResourceBundle result = resourcesMap.get(language);
		
		if (result == null) {
			ResourceBundle parent = super.getBundle(language);
			PropertyResourceBundle properties = getDefinitionProperties();

			if (properties != null) {
				CompositeResourceBundle resources = new CompositeResourceBundle();
				
				resources.addBundle(properties);
				resources.addBundle(parent);
				
				result = resources;
			} else {
				result = parent;
			}
			
			resourcesMap.put(language, result);
		}
		
		return result;
	}
}