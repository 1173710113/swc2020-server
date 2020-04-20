
package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "graph.relation")
public class GraphRelationConfiguration {
	private String server;
	private int serverPort;
	private String client;
	private int clientPort;
}