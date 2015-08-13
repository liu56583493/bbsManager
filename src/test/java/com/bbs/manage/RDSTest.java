package com.bbs.manage;

import com.bbs.manage.utils.RDSClient;
import com.bbs.manage.utils.RDSClient.PrivilegeType;
import com.bbs.utils.JsonUtils;


public class RDSTest {


	public static void main(String[] args) throws InterruptedException {
//		System.out.println(JsonUtils.toJson(RDSClient.listDbs()));
//		System.out.println();
//		String dbName = "swftest";
//		System.out.println(JsonUtils.toJson(RDSClient.createDb(dbName)));
//		System.out.println();
//
//		System.out.println(JsonUtils.toJson(RDSClient.createAccount("test0001", "123456")));
//		
//		Thread.sleep(4000);
		
//		System.out.println();
//		System.out.println(JsonUtils.toJson(RDSClient.listAccount()));
//		System.out.println();
//		System.out.println(JsonUtils.toJson(RDSClient.accountPrivilege("test0001", dbName, PrivilegeType.ReadOnly)));
//		System.out.println();
//		System.out.println(JsonUtils.toJson(RDSClient.resetAccount("test0001", "123456789")));
//		System.out.println();
//		System.out.println(JsonUtils.toJson(RDSClient.delAccount("test0001")));
//		System.out.println();
		System.out.println(JsonUtils.toJson(RDSClient.deleteDb("db_a7e0560468f140d2b98937302f8a57e4")));
//		System.out.println();

	}

}
