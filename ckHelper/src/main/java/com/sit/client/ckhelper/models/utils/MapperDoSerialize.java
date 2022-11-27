package com.sit.client.ckhelper.models.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;
import com.google.gson.Gson;
import com.sit.client.ckhelper.models.manager.http.ClickHouseResponse;

/**
 * @program: ckHelper
 * @description: 对响应数据的一个业务解封装，反序列化处理
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-14 19:20:
 */
public final class MapperDoSerialize {
	private static final Gson GSON = new Gson();

	/**
	 * 结果集反序列化处理
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> Function<String, ClickHouseResponse<T>> toPOJO(Class<T> clazz) {
		return res -> GSON.fromJson(res, getType(ClickHouseResponse.class, clazz));
	}

	private static Type getType(Class<?> rawClass, Class<?> parameter) {
		return new ParameterizedType() {
			@Override
			public Type[] getActualTypeArguments() {
				return new Type[] { parameter };
			}

			@Override
			public Type getRawType() {
				return rawClass;
			}

			@Override
			public Type getOwnerType() {
				return null;
			}
		};
	}
}
