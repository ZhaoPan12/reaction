package com.net.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;

public class JSONUtils {

	/**
	 * 此方法实现JDBCTemplate 返回的Map集合对数据的自动 封装功能 List集合存储着一系列的MAP 对象，obj为一个javaBean
	 * 
	 * @param listMap集合
	 * @param objjavaBean对象
	 * @return
	 */
	public List parse(List list, Class obj) {
		// 生成集合
		ArrayList ary = new ArrayList();
		// 遍历集合中的所有数据
		for (int i = 0; i < list.size(); i++) {
			try {
				//// 生成对象实历 将MAP中的所有参数封装到对象中
				Object o = this.addProperty((Map) list.get(i), obj.newInstance());
				// 把对象加入到集合中
				ary.add(o);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 返回封装好的集合
		return list;
	}

	/**
	 * Map对象中的值为 name=aaa,value=bbb 调用方法 addProperty(map,user); 将自动将map中的值赋给user类
	 * 此方法结合Spring框架的jdbcTemplete将非 常有用
	 * 
	 * @param map存储着名称和值集合
	 * @param obj要封装的对象
	 * @return封装好的对象
	 */
	public Object addProperty(Map map, Object obj) {
		// 遍历所有名称
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			// 取得名称
			String name = it.next().toString();
			// 取得值
			String value = map.get(name).toString();

			try {
				// 取得值的类形
				Class type = PropertyUtils.getPropertyType(obj, name);

				if (type != null) {
					// 设置参数
					PropertyUtils.setProperty(obj, name, ConvertUtils.convert(value, type));

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		return obj;

	}

}
