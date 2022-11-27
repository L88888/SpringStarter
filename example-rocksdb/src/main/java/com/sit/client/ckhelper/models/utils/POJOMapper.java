package com.sit.client.ckhelper.models.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;

import com.google.gson.Gson;
import com.sit.client.ckhelper.models.manager.http.ClickHouseResponse;

public final class POJOMapper {
	private static final Gson GSON = new Gson();
	
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
