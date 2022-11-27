package com.sit.client.ckhelper.models.manager;

import com.sit.client.ckhelper.models.manager.exceptions.HttpRequestException;
import com.sit.client.ckhelper.models.manager.http.ClickHouseResponse;
import com.sit.client.ckhelper.models.utils.MapperDoSerialize;
import com.sit.client.ckhelper.models.utils.Tools;
import org.asynchttpclient.*;
import org.asynchttpclient.request.body.multipart.FilePart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @program: ckHelper
 * @description: 使用http协议直接与clickhouse进行远程通信
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-14 19:20:
 */
public class ClickHouseClient implements AutoCloseable {
	private static final Logger LOG = LoggerFactory.getLogger(ClickHouseClient.class);

	/**
	 * 查询模式
	 */
	private static final String SELECT_FORMAT = "JSON";

	/**
	 * 新增模式
	 */
	private static final String INSERT_FORMAT = "TabSeparated";

	/**
	 * 请求报文中的参数对象
	 */
	private final List<Param> optParams;
	/**
	 * ck站点信息
	 */
	private final String endpoint;
	/**
	 * 客户端异步请求对象AsyncHttpClient
	 */
	private final AsyncHttpClient httpClient;
	
	public ClickHouseClient(String endpoint) {
		this.endpoint = endpoint;
		this.optParams = new ArrayList<>();
		this.httpClient = new DefaultAsyncHttpClient();
	}

	/**
	 * 初始化站点信息与客户端请求配置信息，支持用户名密码模式
	 * @param endpoint ck站点信息
	 * @param username 用户名
	 * @param password 密码
	 */
	public ClickHouseClient(String endpoint, String username, String password) {
		this.endpoint = endpoint;

		this.optParams = new ArrayList<>();

		AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder()
				.setRealm(new Realm.Builder(username, password)
						.setUsePreemptiveAuth(true)
						.setScheme(Realm.AuthScheme.BASIC)
						.build())
				.build();

		this.httpClient = new DefaultAsyncHttpClient(config);
	}

	/**
	 * 可自动关闭http链接
	 */
	@Override
	public void close() {
		try {
			this.httpClient.close();
		} catch (Exception e) {
			LOG.error("关闭http客户端链接失败:>{}", e);
		}
	}

	/**
	 * 设置一些请求参数
	 * @param params
	 * @return
	 */
	public ClickHouseClient setOptionalParams(Map<String, String> params) {
		params.entrySet().forEach(e -> optParams.add(new Param(e.getKey(),
				e.getValue())));
		return this;
	}

	/**
	 * 根据sql文档查询数据
	 * @param query sql文档
	 * @param clazz select 返回的字段属性，这里需要映射为一个具体的业务对象
	 * @param <T> ClickHouseResponse
	 * @return
	 */
	public <T> CompletableFuture<ClickHouseResponse<T>> get(String query, Class<T> clazz) {
		String queryWithFormat = query + " FORMAT " + SELECT_FORMAT;
		Request request = httpClient.prepareGet(endpoint)
				.addQueryParams(new ArrayList<>())
				.addQueryParams(optParams)
				.addQueryParam("query", queryWithFormat)
				.build();
		
		LOG.debug("GET请求的数据报文 {}", queryWithFormat);
		return sendRequest(request).thenApply(MapperDoSerialize.toPOJO(clazz));
	}

	/**
	 * 根据sql文档获取数据
	 * @param query sql文档
	 * @param clazz select 返回的字段属性，这里需要映射为一个具体的业务对象
	 * @param <T> ClickHouseResponse
	 * @return
	 */
	public <T> CompletableFuture<ClickHouseResponse<T>> post(String query, Class<T> clazz) {
		String queryWithFormat = query + " FORMAT " + SELECT_FORMAT;

		Request request = httpClient.preparePost(endpoint)
				.addQueryParams(new ArrayList<>())
				.addQueryParams(optParams)
				.setBody(queryWithFormat)
				.build();

		LOG.debug("POST请求的数据报文 {}", queryWithFormat);

		return sendRequest(request).thenApply(MapperDoSerialize.toPOJO(clazz));
	}

	/**
	 * 用于数据新增或批量新增操作
	 * @param query sql文档
	 * @param data 这里需要映射为一个具体的业务对象，单个或集合
	 * @return
	 */
	public CompletableFuture<String> post(String query, List<Object[]> data) {
		String queryWithFormat = query + " FORMAT " + INSERT_FORMAT;

		Request request = httpClient.preparePost(endpoint)
				.addQueryParams(new ArrayList<>())
				.addQueryParam("query", queryWithFormat)
				.setBody(Tools.tabSeparatedString(data))
				.build();

		// ck那边未返回insert的结果数据，在链接正常的情况下默认执行成功
		return sendRequest(request).thenApply(rs -> Tools.SUCCESS);
	}

	public CompletableFuture<String> post(String query) {
		String queryWithFormat = query + " FORMAT " + SELECT_FORMAT;

		Request request = httpClient.preparePost(endpoint)
				.addQueryParams(new ArrayList<>())
				.addQueryParams(optParams)
				.setBody(queryWithFormat)
				.build();

		LOG.debug("querying POST {}", queryWithFormat);

		return sendRequest(request).thenApply(res -> Tools.SUCCESS);
	}

	/**
	 * 外部数据扩展查询，批量或者大规模数据写入或查询
	 * @param query
	 * @param structure
	 * @param data
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public <T> CompletableFuture<ClickHouseResponse<T>> queryWithExternalData(String query, String structure, List<Object[]> data, Class<T> clazz) {
		String queryWithFormat = query + " FORMAT " + SELECT_FORMAT;

		try {
			final File temp = File.createTempFile("temp", ".tsv");

			try (OutputStreamWriter fr = new OutputStreamWriter(Files.newOutputStream(temp.toPath()), StandardCharsets.UTF_8)) {
				fr.write(Tools.tabSeparatedString(data));
			}

			Request request = httpClient.preparePost(endpoint)
					.addQueryParams(new ArrayList<>())
					.addQueryParams(optParams)
					.addQueryParam("query", queryWithFormat)
					.addQueryParam("temp_structure", structure)
					.addHeader("Content-Type", "multipart/form-data")
					.addBodyPart(new FilePart("temp", temp))
					.build();

			LOG.debug("请求扩展数据报文 {}", queryWithFormat);

			return sendRequest(request).thenApply(MapperDoSerialize.toPOJO(clazz)).whenComplete((res, t) -> temp.delete());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * ck客户端心跳检测
	 * @return
	 */
	public CompletableFuture<String> healthcheck() {
		Request request = httpClient.prepareGet(endpoint).build();
		return sendRequest(request);
	}

	/**
	 * 发起远程执行请求，并异步接收响应数据
	 * @param request
	 * @return
	 */
	private CompletableFuture<String> sendRequest(Request request) {
		LOG.debug("发送请求地址 {}", request.getUrl());
		return httpClient.executeRequest(request).toCompletableFuture()
		.handle((response, t) -> {
			if (t != null) {
				LOG.error("找不到站点信息{},发送数据异常{}" + endpoint, t);
				throw new RuntimeException("找不到站点信息=" + endpoint);
			} else {
				final int statusCode = response.getStatusCode();
				final String body = response.getResponseBody();

				if (statusCode != 200) {
					final String decodedUrl = Tools.decodedUrl(request);
					HttpRequestException e = new HttpRequestException(statusCode, body, decodedUrl);
					LOG.error("请求异常[{}] {} : {}", statusCode, decodedUrl, body);
					throw e;
				}

				return body;
			}
		});
	}
}