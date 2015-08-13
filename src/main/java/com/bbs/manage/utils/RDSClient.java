package com.bbs.manage.utils;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.rds.model.v20140815.CreateAccountRequest;
import com.aliyuncs.rds.model.v20140815.CreateAccountResponse;
import com.aliyuncs.rds.model.v20140815.CreateDatabaseRequest;
import com.aliyuncs.rds.model.v20140815.CreateDatabaseResponse;
import com.aliyuncs.rds.model.v20140815.DeleteAccountRequest;
import com.aliyuncs.rds.model.v20140815.DeleteAccountResponse;
import com.aliyuncs.rds.model.v20140815.DeleteDatabaseRequest;
import com.aliyuncs.rds.model.v20140815.DeleteDatabaseResponse;
import com.aliyuncs.rds.model.v20140815.DescribeAccountsRequest;
import com.aliyuncs.rds.model.v20140815.DescribeAccountsResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesResponse;
import com.aliyuncs.rds.model.v20140815.GrantAccountPrivilegeRequest;
import com.aliyuncs.rds.model.v20140815.GrantAccountPrivilegeResponse;
import com.aliyuncs.rds.model.v20140815.ResetAccountPasswordRequest;
import com.aliyuncs.rds.model.v20140815.ResetAccountPasswordResponse;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;

/**
 * RDS 数据库客户端
 * @title RDSClient.java
 * @description 
 * @company 北京思维夫网络科技有限公司
 * @author 卢春梦
 * @version 1.0
 * @created 2015年5月14日下午4:31:51
 */
public class RDSClient {

	private static final Logger logger = Logger.getLogger(RDSClient.class);

	private static Prop prop = PropKit.use("rds.properties");
	// 实例名key
	private static final String INSTANCEID_KEY = "rds.instanceId";
	private static final IAcsClient client = init();

	/**
	 * 初始化
	 * @return
	 */
	private static final IAcsClient init() {
		// rds 配置信息
		String regionId    = prop.get("rds.regionId");
		String accessKeyId = prop.get("rds.accessKeyId");
		String secret      = prop.get("rds.secret");

		IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, secret);
		return new DefaultAcsClient(profile);
	}

	/**
	 * 获取响应信息，注册为json
	 * @param request
	 * @return
	 * @throws ClientException 
	 * @throws ServerException 
	 */
	public static <T extends AcsResponse> T execute(AcsRequest<T> request) {
		request.setAcceptFormat(FormatType.JSON);
		try {
			return client.getAcsResponse(request);
		} catch (ClientException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获取实例id
	 * @return
	 */
	public static String getInstanceId() {
		return prop.get(INSTANCEID_KEY);
	}

	/**
	 * 列出所有的数据库
	 * @return
	 */
	public static DescribeDatabasesResponse listDbs() {
		DescribeDatabasesRequest request = new DescribeDatabasesRequest();
		request.setDBInstanceId(getInstanceId());

		return execute(request);
	}

	/**
	 * 创建数据库
	 * @return
	 */
	public static CreateDatabaseResponse createDb(final String dbName) {
		CreateDatabaseRequest request = new CreateDatabaseRequest();
		request.setCharacterSetName("utf8");
		request.setDBName(dbName);
		request.setDBInstanceId(getInstanceId());

		return execute(request);
	}

	/**
	 * 删除数据库
	 * @param dbName
	 * @return 
	 */
	public static DeleteDatabaseResponse deleteDb(final String dBName) {
		DeleteDatabaseRequest request = new DeleteDatabaseRequest();
		request.setDBInstanceId(getInstanceId());
		request.setDBName(dBName);

		return execute(request);
	}

	/**
	 * 创建数据库用户
	 * @param name
	 * @param password
	 * @return 
	 */
	public static CreateAccountResponse createAccount(final String name, final String password) {
		CreateAccountRequest request = new CreateAccountRequest();
		request.setAccountName(name);
		request.setAccountPassword(password);
		request.setDBInstanceId(getInstanceId());

		return execute(request);
	}

	/**
	 * 删除数据库用户
	 * @param name
	 * @param password
	 * @return 
	 */
	public static DeleteAccountResponse delAccount(final String name) {
		DeleteAccountRequest request = new DeleteAccountRequest();
		request.setAccountName(name);
		request.setDBInstanceId(getInstanceId());

		return execute(request);
	}

	/**
	 * 重置数据库用户密码
	 * @param name
	 * @param password
	 * @return 
	 */
	public static ResetAccountPasswordResponse resetAccount(final String name, final String password) {
		ResetAccountPasswordRequest request = new ResetAccountPasswordRequest();
		request.setAccountName(name);
		request.setAccountPassword(password);
		request.setDBInstanceId(getInstanceId());

		return execute(request);
	}

	/**
	 * 帐号权限
	 */
	public enum PrivilegeType{
		ReadOnly, ReadWrite
	}
	
	/**
	 * 授权帐号权限
	 * @param name
	 * @param password
	 * @param privilege 帐号权限，ReadOnly 只读，ReadWrite读写
	 * @return GrantAccountPrivilegeResponse
	 */
	public static GrantAccountPrivilegeResponse accountPrivilege(final String name, final String dBName, final PrivilegeType privilegeType) {
		GrantAccountPrivilegeRequest request = new GrantAccountPrivilegeRequest();
		request.setAccountName(name);
		request.setAccountPrivilege(privilegeType.name());
		request.setDBName(dBName);
		request.setDBInstanceId(getInstanceId());

		return execute(request);
	}

	/**
	 * 查看帐号列表
	 * @param name
	 * @param password
	 * @return 
	 */
	public static DescribeAccountsResponse listAccount() {
		DescribeAccountsRequest request = new DescribeAccountsRequest();
		request.setDBInstanceId(getInstanceId());

		return execute(request);
	}

}
