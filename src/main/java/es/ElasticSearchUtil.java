package es;

import java.util.ResourceBundle;


import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ElasticSearchUtil {
	
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("elasticsearch");
	private static final String CLUSTER_NAME = "cluster.name";
	private static final String HOST = "host";
	private static final String PORT = "port";
		
	private static Settings settings = null;
	private static InetSocketTransportAddress inetSocketTransportAddress = null;

	private static String ClusterName = null;
	private static String Host = null;
	private static Integer Port = null;
		
	public static synchronized Settings getSettings()
	{
		if (settings == null) {
			settings = ImmutableSettings.settingsBuilder().put(CLUSTER_NAME, ElasticSearchUtil.getClusterName())
			.build();
		}
		return settings;
	}

	public static synchronized InetSocketTransportAddress getInetSocketTransportAddress()
	{
		if (inetSocketTransportAddress == null) {
			inetSocketTransportAddress = new InetSocketTransportAddress(ElasticSearchUtil.getHost(), ElasticSearchUtil.getPort());
		}
		return inetSocketTransportAddress;
	}
	
	public static synchronized Client getClient()
	{
		return new TransportClient(ElasticSearchUtil.getSettings())
			.addTransportAddress(ElasticSearchUtil.getInetSocketTransportAddress());
	}

	public static String getClusterName()
	{
		if (ClusterName == null) {
			ClusterName = RESOURCE_BUNDLE.getString(CLUSTER_NAME);
		}
		return ClusterName;
	}
	
	public static String getHost()
	{
		if (Host == null) {
			Host = RESOURCE_BUNDLE.getString(HOST);
		}
		return Host;
	}
	
	public static Integer getPort()
	{
		if (Port == null) {
			Port = Integer.parseInt(RESOURCE_BUNDLE.getString(PORT));
		}
		return Port;
	}
}
