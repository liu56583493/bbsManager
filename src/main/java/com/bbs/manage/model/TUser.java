package com.bbs.manage.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.siweifu.utils.StringUtils;

/**
 * TODO 在此加入类描述
 * @title TUser.java
 * @description 
 * @company 北京思维夫网络科技有限公司
 * @author 卢春梦  
 * @version 1.0
 * @created 2015年06月22日 15:23:14
 */
@SuppressWarnings("serial")
public class TUser extends Model<TUser> {

	public static final String TABLE_NAME = "t_user";
	public static final String PRIMARY_KEY = "id";
	
	//用户是否已删除--是
	public static final Integer IS_DELETE_YES = 1;
	
	//用户是否已删除--否
	public static final Integer IS_DELETE_NO = 0;
	
	//传入的参数是账户名
	public static final Integer IS_ACCOUNNAME = 1;
	//传入的参数是邮箱
	public static final Integer IS_EMAIL = 2;
	//传入的参数是手机
	public static final Integer IS_PHONE = 3;
	//角色默认为0：普通用户，1：vip，2：admin
	public static final int IS_ADMIN = 2;

	// "id" -> 用户id
	// "accounName" -> 帐户名
	// "trueName" -> 真实姓名
	// "password" -> 密码
	// "email" -> 邮箱
	// "phone" -> 手机号
	// "sex" -> 0:男，1:女
	// "avatar" -> 头像
	// "signature" -> 个性签名
	// "address" -> 常住地
	// "balance" -> 余额，单位为分，避免小数四舍五入
	// "steps" -> 步骤，默认0：新注册，1：已完善
	// "role" -> 角色默认为0：普通用户，1：vip，2：admin
	// "createAt" -> 创建时间
	// "isDeleted" -> 是否已删除：0否，1是

	public static final TUser dao = new TUser();

	public Long getId(){
		return get("id");
	}
	public TUser setId(Long id){
		return set("id", id);
	}

	public String getAccounName(){
		return get("accounName");
	}
	public TUser setAccounName(String accounName){
		return set("accounName", accounName);
	}

	public String getTrueName(){
		return get("trueName");
	}
	public TUser setTrueName(String trueName){
		return set("trueName", trueName);
	}

	public String getPassword(){
		return get("password");
	}
	public TUser setPassword(String password){
		return set("password", password);
	}

	public String getEmail(){
		return get("email");
	}
	public TUser setEmail(String email){
		return set("email", email);
	}

	public String getPhone(){
		return get("phone");
	}
	public TUser setPhone(String phone){
		return set("phone", phone);
	}

	public Integer getSex(){
		return get("sex");
	}
	public TUser setSex(Integer sex){
		return set("sex", sex);
	}

	public String getAvatar(){
		return get("avatar");
	}
	public TUser setAvatar(String avatar){
		return set("avatar", avatar);
	}

	public String getSignature(){
		return get("signature");
	}
	public TUser setSignature(String signature){
		return set("signature", signature);
	}

	public String getAddress(){
		return get("address");
	}
	public TUser setAddress(String address){
		return set("address", address);
	}

	public Integer getBalance(){
		return get("balance");
	}
	public TUser setBalance(Integer balance){
		return set("balance", balance);
	}

	public Integer getSteps(){
		return get("steps");
	}
	public TUser setSteps(Integer steps){
		return set("steps", steps);
	}

	public Integer getRole(){
		return get("role");
	}
	public TUser setRole(Integer role){
		return set("role", role);
	}

	public Date getCreateAt(){
		return get("createAt");
	}
	public TUser setCreateAt(Date createAt){
		return set("createAt", createAt);
	}

	public Integer getIsDeleted(){
		return get("isDeleted");
	}
	public TUser setIsDeleted(Integer isDeleted){
		return set("isDeleted", isDeleted);
	}
	
	
	/**
	 * 保存用户并保存用户的默认字段
	 */
	@Override
	public boolean save() {
		this.setSex(0);
		this.setSteps(0);
		this.setRole(0);
		this.setBalance(0);
		this.setCreateAt(new Date());
		return super.save();
	}

	private static final String USER_CACHE_NAME = "session";

	/**
	 * 更新用户非必须字段
	 */
	@Override
	public boolean update() {
		// 清理掉缓存
		boolean temp = super.update();
		this.removeCache();
		return temp;
	}

	@Override
	public boolean delete() {
		// 清理掉缓存
		boolean temp =  super.delete();
		this.removeCache();
		return temp;
	}

	/**
	 * 清除缓存
	 */
	public void removeCache() {
		CacheKit.remove(USER_CACHE_NAME, this.getId());
	}

	/**
	 * 从缓存中加载用户信息
	 * @param userId
	 * @return
	 */
	public TUser loadInSession(final String userId){
		// 传入的userId为空或者不是数字的直接返回null
		if (StrKit.isBlank(userId) || !StringUtils.isNumeric(userId)) {
			return null;
		}
		return loadInSession(Long.parseLong(userId));
	}

	/**
	 * 从缓存中加载用户信息
	 * @param userId
	 * @return
	 */
	public TUser loadInSession(final long userId) {
		return CacheKit.get(USER_CACHE_NAME, userId, new IDataLoader() {

			@Override
			public Object load() {
				return findById(userId);
			}
		});
	}

	/**
	 * 根据手机号或者邮箱查找
	 * @param loginName
	 * @return
	 */
	public TUser findByPhoneOREmail(String loginName) {
		String sql = "SELECT u.* FROM t_user u WHERE (u.phone = ? OR u.email = ?) LIMIT 1";
		return this.findFirst(sql, loginName, loginName);
	}

	/**
	 * 查找可以用的邀请码生成用户的时候生成邀请码
	 * @return 邀请码
	 */
	public String findInvitationCode() {
		String sql = "SELECT LPAD(IFNULL(MAX(t.`id`) + 1, 99999),5,0) AS `code` FROM t_user  t";
		return Db.queryStr(sql);
	}

	/**
	 * 
	 * @description  功能描述: 根据用户自己注册时候生成的邀请码来获取用户信息
	 * @author 		        作        者: liuwei
	 * @param	                    参        数: 
	 * @return        返回类型: TUser
	 * @createdate   建立日期: 2015年5月21日下午2:29:47
	 */
	public TUser findByInvitationCode(String code){
		String sql = "SELECT u.* FROM t_user u WHERE u.code = ? LIMIT 1";
		return this.findFirst(sql, code);
	}

	/**
	 * 
	 * @description  功能描述:  根据用户ID查询其所有的邀请注册的用户
	 * @author 		        作        者: liuwei
	 * @param	                    参        数: userId用户ID
	 * @return        返回类型: Page<TUser>
	 * @createdate   建立日期: 2015年5月21日下午3:24:46
	 */
	public Page<TUser> findInvUserPage(Integer pageNumber, Integer pageSize,Long userId){
		String select = "SELECT t.*  ";
		String sqlExceptSelect = " FROM t_user t where  t.invUserId = ? ORDER BY t.createAt DESC";
		return dao.paginate(pageNumber, pageSize, select, sqlExceptSelect, userId);
	}

	/**
	 * 
	 * @description  功能描述: 分页查询所有的用户
	 * @author 		        作        者: liuwei
	 * @return        返回类型: Page<TUser>
	 * @createdate   建立日期: 2015年5月24日下午3:16:34
	 */
	public Page<TUser> findAllUserPage(Integer pageNumber, Integer pageSize, Map<String, Object> params){
		String select = "SELECT t.*,DATE_FORMAT(t.createAt,'%Y-%m-%d %h:%i') as createAtr,FORMAT(balance/100,2) as balanceStr  ";
		
		List<Object> paras = new ArrayList<Object>();

		StringBuffer sqlExceptSelect = new StringBuffer(" FROM t_user t WHERE 1 = 1 ");
		//用户类型不为空
		if(null != params.get("role") && StrKit.notBlank(params.get("role").toString())){
			sqlExceptSelect.append(" AND t.role = ? ");
			paras.add(params.get("role"));
		}
		//用户的账户不为空
		if(null != params.get("accounName") && StrKit.notBlank(params.get("accounName").toString())){
			sqlExceptSelect.append(" AND ( t.accounName LIKE ? OR t.email LIKE ? OR t.phone LIKE ? ) ");
			paras.add("%" + params.get("accounName") + "%");
			paras.add("%" + params.get("accounName") + "%");
			paras.add("%" + params.get("accounName") + "%");
		}
		sqlExceptSelect.append(" AND t.isDeleted=0 ORDER BY t.createAt DESC");
		return dao.paginate(pageNumber, pageSize, select, sqlExceptSelect.toString(),paras.toArray());
	}

	/**
	 * 
	 * @description  功能描述: 根据用户ID查询用户
	 * @author 		        作        者: liuwei
	 * @param	                    参        数: userIds用户ID用，分隔
	 * @return        返回类型: List<TUser>
	 * @createdate   建立日期: 2015年5月25日上午10:01:42
	 */
	public List<TUser> findUsersByUserIds(String userIds){
		String sql = " SELECT t.* FROM t_user t WHERE t.id in (" + userIds + ")";
		return dao.find(sql);
	}

	/**
	 * @description  功能描述: 根据参数查询用户的信息
	 * @author 		        作        者: liuwei
	 * @param	                    参        数: param参数,type查询的类型
	 * @return        返回类型: List<TUser>
	 * @createdate   建立日期: 2015年5月25日下午7:33:49
	 */
	public List<TUser> findUserList(String param, Integer type){
		StringBuffer sql = new StringBuffer(" SELECT t.* FROM t_user t WHERE 1=1 ");
		if(type == TUser.IS_ACCOUNNAME){
			sql.append(" AND t.accounName = ? ");
		}else if(type == TUser.IS_EMAIL){
			sql.append(" AND t.email = ? ");
		}else{
			sql.append(" AND t.phone = ? ");
		}
		return dao.find(sql.toString(), param);
	}

}