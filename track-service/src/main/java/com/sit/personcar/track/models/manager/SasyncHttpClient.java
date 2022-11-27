package com.sit.personcar.track.models.manager;

import com.sit.personcar.track.analysis.entity.RequestHeads;
import com.sit.personcar.track.analysis.vo.RequestData;
import com.sit.personcar.track.analysis.vo.ResponseViewData;
import com.sit.personcar.track.models.manager.exceptions.HttpRequestException;
import org.asynchttpclient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @program: AsyncHttpClient
 * @description: 使用AsyncHttpClient协议直接与查询服务进行远程通信
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-14 19:20:
 */
public class SasyncHttpClient implements AutoCloseable {
	private static final Logger LOG = LoggerFactory.getLogger(SasyncHttpClient.class);

	/**
	 * 请求报文中的参数对象
	 */
	private List<Param> optParams;
	/**
	 * ck站点信息
	 */
	private String endpoint;
	/**
	 * 客户端异步请求对象AsyncHttpClient
	 */
	private AsyncHttpClient httpClient;

	public SasyncHttpClient(String endpoint) {
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
	public SasyncHttpClient(String endpoint, String username, String password) {
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
	 * 开始查询服务的底层请求封装
	 * @param requestData  查询服务请求报文对象
	 * @param requestHeads 查询服务请求头数据
	 * @return
	 */
	public CompletableFuture<Receive> postT(RequestData requestData,
										   RequestHeads requestHeads) {
		LOG.info("endpoint data:>{}", endpoint);
		Request request = httpClient.preparePost(endpoint)
				.setHeader("Content-Type","application/json")
				.setBody(requestData.toString())
				.build();

		request = requestHeads.getHttpHeaders(request);
		LOG.info("POST请求的数据报文 {}", request.toString());

		// 需要将响应报文在线程中完成数据重组，不然会阻塞主线程
		return sendRequest(request);
	}

	/**
	 * 开始查询服务的底层请求封装
	 * @param requestData  查询服务请求报文对象
	 * @param requestHeads 查询服务请求头数据
	 * @return
	 */
	public CompletableFuture<ResponseViewData> post(RequestData requestData, RequestHeads requestHeads) {
		LOG.info("endpoint data:>{}", endpoint);
		Request request = httpClient.preparePost(endpoint)
				.setHeader("Content-Type","application/json")
				.setBody(requestData.toString())
				.build();

		request = requestHeads.getHttpHeaders(request);
		LOG.info("POST请求的数据报文 {}", request.toString());

		// 需要将响应报文在线程中完成数据重组，不然会阻塞主线程
		return sendRequest(request).thenApply(MapperDoSerialize.transformationData());
	}

	/**
	 * 心跳检测
	 * @return
	 */
	public CompletableFuture<Receive> healthcheck() {
		Request request = httpClient.prepareGet(endpoint).build();
		return sendRequest(request);
	}

	/**
	 * 发起远程执行请求，并异步接收响应数据
	 * @param request
	 * @return
	 */
	private CompletableFuture<Receive> sendRequest(Request request) {
		LOG.info("发送请求地址 {}", request.getUrl());
		return httpClient.executeRequest(request).toCompletableFuture()
		.handle((response, t) -> {
			String service_id = request.getHeaders().get("service_id");
			Receive receive = new Receive();
			if (t != null) {
				LOG.error("找不到站点信息{},发送数据异常{}", endpoint, t.getMessage());
				receive.setBody(t.getMessage()).setServerId(service_id).setStatusCode(-1);
				return receive;
			} else {
				int statusCode = response.getStatusCode();
				String body = response.getResponseBody();
				LOG.info("请求服务:>{},响应结果:>{}", service_id, body);

				if (statusCode != 200) {
					String decodedUrl = request.getUrl();
					HttpRequestException e = new HttpRequestException(statusCode, body, decodedUrl);
					LOG.error("请求异常[{}] {} : {}", statusCode, decodedUrl, body);
					receive.setBody(e.getMessage()).setServerId(service_id).setStatusCode(statusCode);
					return receive;
				}
				receive.setBody(body).setServerId(service_id).setStatusCode(statusCode);
			}
			return receive;
		});
	}
}