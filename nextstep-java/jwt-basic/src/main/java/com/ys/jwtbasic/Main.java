package com.ys.jwtbasic;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

public class Main {
	public static void main(String[] args) throws Exception {
		String webappDirLocation = "webapp/";
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
		Context context = tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());
		tomcat.start();
		tomcat.getServer().await();
	}
}