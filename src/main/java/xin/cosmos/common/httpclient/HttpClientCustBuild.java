package xin.cosmos.common.httpclient;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientCustBuild extends HttpClientBuilder{
	
	/**记录是否设置了连接池*/
	private boolean isSetPool=false;
	/**记录是否设置了更新了ssl*/
	private boolean isNewSsl=false;
	
	/**
	 * 用于配置ssl
	 */
	private Ssls ssls = Ssls.getInstance();
	
	private HttpClientCustBuild(){}
	protected static HttpClientCustBuild getInstance(){
		return new HttpClientCustBuild();
	}
 
	/**
	 * 设置超时时间
	 * 
	 * @param timeout		超市时间，单位-毫秒
	 * @return
	 */
	protected HttpClientCustBuild timeout(int timeout){
		 // 配置请求的超时设置
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
		return (HttpClientCustBuild) this.setDefaultRequestConfig(config);
	}
	
	/**
	 * 设置ssl安全链接
	 * 
	 * @return
	 */
	protected HttpClientCustBuild ssl() {
		//如果已经设置过线程池，那肯定也就是https链接了
		if(isSetPool){
			if(isNewSsl){
				throw new RuntimeException("请先设置ssl，后设置pool");
			}
			return this;
		}
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
 				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", ssls.getSslConnsf()).build();
		//设置连接池大小
		PoolingHttpClientConnectionManager connManager = 
				new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		return (HttpClientCustBuild) this.setConnectionManager(connManager);
	}
	
 
	/**
	 * 设置自定义sslcontext
	 * 
	 * @param keyStorePath		密钥库路径
	 * @return
	 */
	protected HttpClientCustBuild ssl(String keyStorePath){
		return ssl(keyStorePath,"nopassword");
	}
	/**
	 * 设置自定义sslcontext
	 * 
	 * @param keyStorePath		密钥库路径
	 * @param keyStorepass		密钥库密码
	 * @return
	 */
	protected HttpClientCustBuild ssl(String keyStorePath, String keyStorepass){
		this.ssls = Ssls.getInstance().customSsl(keyStorePath, keyStorepass);
		this.isNewSsl=true;
		return ssl();
	}
	
	
	/**
	 * 设置连接池（默认开启https）
	 * 
	 * @param maxTotal					最大连接数
	 * @param defaultMaxPerRoute	每个路由默认连接数
	 * @return
	 */
	protected HttpClientCustBuild pool(int maxTotal, int defaultMaxPerRoute){
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", ssls.getSslConnsf()).build();
		//设置连接池大小
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		connManager.setMaxTotal(maxTotal);
		connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
		isSetPool=true;
		return (HttpClientCustBuild) this.setConnectionManager(connManager);
	}
	
	/**
	 * 设置代理
	 * 
	 * @param hostOrIp		代理host或者ip
	 * @param port			代理端口
	 * @return
	 */
	protected HttpClientCustBuild proxy(String hostOrIp, int port){
		// 依次是代理地址，代理端口号，协议类型  
		HttpHost proxy = new HttpHost(hostOrIp, port, "http");  
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		return (HttpClientCustBuild) this.setRoutePlanner(routePlanner);
	}
}