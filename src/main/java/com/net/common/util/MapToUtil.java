package com.net.common.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class MapToUtil {

	public static <T> T decode(Class<T> clazz, Map<String, Object> msg) {
		try {
			T cls = clazz.newInstance();
			Set<Map.Entry<String, Object>> entrySet = msg.entrySet();
			for (Map.Entry<String, Object> entry : entrySet) {
				BeanUtils.setProperty(cls, entry.getKey(), entry.getValue());
			}
			return cls;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T decode(Class<T> clazz, Map<String, Object> m, String name) {
		m = (Map) m.get(name);
		try {
			T cls = clazz.newInstance();
			Set<Map.Entry<String, Object>> entrySet = m.entrySet();
			for (Map.Entry<String, Object> entry : entrySet) {
				BeanUtils.setProperty(cls, entry.getKey(), entry.getValue());
			}
			return cls;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> List<T> decodeList(Class<T> clazz, Map<String, Object> m, String name) {

		try {
			List<Map<String, Object>> mm = (List<Map<String, Object>>) m.get(name);
			List<T> cls = new ArrayList<T>();
			for (Map<String, Object> ma : mm) {
				T t = clazz.newInstance();
				Set<Map.Entry<String, Object>> entrySet = ma.entrySet();
				for (Map.Entry<String, Object> entry : entrySet) {
					BeanUtils.setProperty(t, entry.getKey(), entry.getValue());
				}
				cls.add(t);
			}
			return cls;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Object> sortMapByKey(Map<String, Object> oriMap) {
		if (oriMap == null || oriMap.isEmpty()) {
			return null;
		}
		Map<String, Object> sortedMap = oriMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors
				.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldVal, newVal) -> oldVal, LinkedHashMap::new));
		return sortedMap;
	}

	public static <T> List<T> getObjectList(String jsonString, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		try {
			Gson gson = new Gson();
			JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();
			for (JsonElement jsonElement : arry) {
				list.add(gson.fromJson(jsonElement, cls));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
