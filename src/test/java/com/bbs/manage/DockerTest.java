package com.bbs.manage;
//package com.bbs.manage;
//
//import org.junit.Test;
//
//import com.github.dockerjava.api.DockerClient;
//import com.github.dockerjava.api.command.CreateContainerResponse;
//import com.github.dockerjava.api.command.InspectContainerResponse;
//import com.github.dockerjava.api.model.ExposedPort;
//import com.github.dockerjava.api.model.PortBinding;
//import com.github.dockerjava.api.model.Ports.Binding;
//import com.bbs.manage.plugin.docker.DockerKit;
//import com.bbs.manage.plugin.docker.DockerPlugin;
//import com.bbs.utils.JsonUtils;
//
//
///**
// * Docker test
// */
//public class DockerTest {
//
//	@Test
//	public void test1() {
//		new DockerPlugin().start();
//
//		DockerClient dockerClient = DockerKit.getClient();
//
//		// 列出所有的实例
//		String json = JsonUtils.toJson(dockerClient.listContainersCmd().exec());
//		System.out.println(json);
//
//		// 创建一个新的实例
//		CreateContainerResponse container = dockerClient.createContainerCmd("siweifu_manage")
//				.withPortBindings(new PortBinding(new Binding(8181), new ExposedPort(8080)))
//				.withPublishAllPorts(true)
//				// 内存控制
//				.withMemoryLimit(512 * 1024 * 1024)
////				.withLinks(new Link(name, alias))
//				.exec();
//
//		// 启动实例
//		dockerClient.startContainerCmd(container.getId()).exec();
//
//		// 检视实例
//		InspectContainerResponse cInfo = dockerClient.inspectContainerCmd(container.getId()).exec();
//
//		System.out.println("\n");
//		System.out.println(JsonUtils.toJson(cInfo));
//
//		dockerClient.stopContainerCmd(container.getId()).exec();
//
//		dockerClient.removeContainerCmd(container.getId()).exec();
//	}
//
//}
