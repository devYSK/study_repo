package com.ys.actuator.custom;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.jmx.annotation.JmxEndpoint;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.stereotype.Component;

import org.springframework.lang.Nullable;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component // config에서 bean 등록 대신 component도 사용 가능
@Endpoint(id = "myLibraryInfo") // endpoint id 지정
public class MyLibraryInfoEndpoint {

	@WriteOperation
	public void changeSomething(String name, boolean enableSomething) {
		log.info("name: {}, enableSomething: {}", name, enableSomething);
	}

	@ReadOperation
	public String getPathVariable(@Selector String path1) {
		log.info("path1: {}", path1);
		return path1;
	}

	@ReadOperation
	public String getMultiPathVariable(@Selector(match = Selector.Match.ALL_REMAINING) String[] path) {
		log.info("path: {}", Arrays.asList(path));
		return Arrays.asList(path).toString();
	}

	@ReadOperation
	public List<LibraryInfo> getLibraryInfos(@Nullable String name, boolean includeVersion) {
		// TODO: 라이브러리 정보를 읽어서 name, version을 가져오는 코드가 있어야 하나 하드코딩으로 대체함.
		LibraryInfo libraryInfo1 = new LibraryInfo();
		libraryInfo1.setName("logback");
		libraryInfo1.setVersion("1.0.0");

		LibraryInfo libraryInfo2 = new LibraryInfo();
		libraryInfo2.setName("jackson");
		libraryInfo2.setVersion("2.0.0");

		List<LibraryInfo> resultList = Arrays.asList(libraryInfo1, libraryInfo2);

		if (name != null) {
			resultList = resultList.stream()
				.filter(libraryInfo -> libraryInfo.getName().equals(name))
				.toList();
		}

		if (!includeVersion) {
			resultList = resultList.stream()
				.map(libraryInfo -> {
					LibraryInfo simpleInfo = new LibraryInfo();
					simpleInfo.setName(libraryInfo.getName());
					// version 정보는 포함하지 않음.
					return simpleInfo;
				}).toList();
		}

		return resultList;
	}

	@Data
	public static class LibraryInfo { // 정보를 리턴할 dto 클래스
		private String name;
		private String version;
	}
}
