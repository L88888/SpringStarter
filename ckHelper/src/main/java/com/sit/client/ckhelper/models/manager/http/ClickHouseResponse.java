package com.sit.client.ckhelper.models.manager.http;

import java.util.List;

/**
 * @program: ckHelper
 * @description: ck结果集数据管理，其中data属性一般是业务对象DO
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-14 19:20:
 */
public final class ClickHouseResponse<T> {
	// ck这边的业务表元数据对象集合
	public final List<MetaRow> meta;
	// 业务对象DO
	public final List<T> data;
	// 受影响的行数或者查询结果集返回的数据总量
	public final Long rows;
	// 返回数据集合的一些限制
	public final Long rows_before_limit_at_least;
	// sql文档远程执行的一些统计
	public final Statistics statistics;
	
	private ClickHouseResponse(List<MetaRow> meta, List<T> data, Long rows, Long rows_before_limit_at_least, Statistics statistics) {
		this.meta = meta;
		this.data = data;
		this.rows = rows;
		this.rows_before_limit_at_least = rows_before_limit_at_least;
		this.statistics = statistics;
	}

	@Override
	public String toString() {
		return "[meta=" + meta + ", data=" + data + ", rows=" + rows + ", rows_before_limit_at_least=" + rows_before_limit_at_least + ", statistics=" + statistics + "]";
	}


	public static class MetaRow {
		public final String name;
		public final String type;
		
		private MetaRow(String name, String type) {
			this.name = name;
			this.type = type;
		}

		@Override
		public String toString() {
			return "[name=" + name + ", type=" + type + "]";
		}
	}

	/**
	 * sql文档远程执行的一些统计
	 */
	public static class Statistics {
		// 执行耗时
		public final Double elapsed;
		// 读取的数据行数
		public final Long rows_read;
		// 读取的数据大小字节
		public final Long bytes_read;
		
		private Statistics(Double elapsed, Long rows_read, Long bytes_read) {
			this.elapsed = elapsed;
			this.rows_read = rows_read;
			this.bytes_read = bytes_read;
		}

		@Override
		public String toString() {
			return "[elapsed=" + elapsed + ", rows_read=" + rows_read + ", bytes_read=" + bytes_read + "]";
		}
	}
}
