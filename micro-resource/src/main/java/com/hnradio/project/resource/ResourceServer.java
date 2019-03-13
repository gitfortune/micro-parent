package com.hnradio.project.resource;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.hnradio.project.api"})
@ComponentScan(basePackages = {"com.hnradio.project"})
@EnableHystrix
public class ResourceServer {

	public static void main(String[] args) {
		SpringApplication.run(ResourceServer.class, args);

	}

}
