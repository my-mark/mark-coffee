package com.mark.markcoffee;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用户
 */
@Data
public class CorpUser implements java.io.Serializable{
	private static final long serialVersionUID = 5454155825314635342L;

	private static Lock userLock = new ReentrantLock();
	public static AtomicInteger count = new AtomicInteger(0);

	//alias
	public static final String TABLE_ALIAS = "CorpUser";
	public static final String ALIAS_ID = "主键ID";
	public static final String ALIAS_CORP_ID = "企业ID";
	public static final String ALIAS_LOGIN_NAME = "登录名";
	public static final String ALIAS_USER_NAME = "用户名";
	public static final String ALIAS_GENDER = "性别,1:男,2:女";
	public static final String ALIAS_POSITION = "用户的职位";
	public static final String ALIAS_EMAIL = "邮箱";
	public static final String ALIAS_EMAIL_STATUS = "邮箱验证状态:0:未认证/1:已认证";
	public static final String ALIAS_MOBILE = "手机号";
	public static final String ALIAS_MOBILE_STATUS = "手机号验证状态（0:未认证/1:已认证）";
	public static final String ALIAS_WECHAT_CODE = "微信唯一ID";
	public static final String ALIAS_WECHAT_OPEN_CODE = "微信OpenID";
	public static final String ALIAS_WECHAT_STATUS = "微信绑定状态（0:未认证/1:已认证）";
	public static final String ALIAS_PASSWORD = "密码";
	public static final String ALIAS_SALT = "密钥加密";
	public static final String ALIAS_REGISTER_DATE = "注册时间";
	public static final String ALIAS_REGISTER_IP = "注册ip";
	public static final String ALIAS_FIRST_LOGIN_DATE = "首次登录时间";
	public static final String ALIAS_LAST_LOGIN_DATE = "最后一次登录时间";
	public static final String ALIAS_LAST_LOGIN_IP = "最后登录IP";
	public static final String ALIAS_ROLE_CODE = "角色Code";
	public static final String ALIAS_STATUS_NO = "用户状态:-2:删除/0:冻结/1:正常;2:审核中;3:审核失败";
	public static final String ALIAS_SOURCE_CODE = "来源code（1、主账号。2、子账号。3.后台添加）";
	public static final String ALIAS_CORP_CLUE_NAME = "企业线索名称";
	public static final String ALIAS_REGISTER_TYPE = "账户注册/开通方式  1=企业注册，2=客服开通";
	public static final String ALIAS_CERTIFICATION_CREATE_DATE = "认证资料提交时间";
	public static final String ALIAS_CERTIFICATION_TYPE = "认证方式：1=营业执照认证";
	public static final String ALIAS_CERTIFICATION_STATUS = "认证状态：-1=暂时无需认证，1=待认证，2=认证中，3=认证通过，4=认证失败";
	public static final String ALIAS_CERTIFICATION_BY = "认证人id";
	public static final String ALIAS_CERTIFICATION_DATE = "认证人时间";
	public static final String ALIAS_CERTIFICATION_REMARK = "认证说明";
	public static final String ALIAS_CODE = "全局唯一code";
	public static final String ALIAS_BALANCE = "账户余额";

	//columns START
	private Long id;
	private Long corpId;
	private String loginName;
	private String userName;
	private Integer gender;
	private String position;
	private String email;
	private Integer emailStatus;
	private String mobile;
	private Integer mobileStatus;
	private String wechatCode;
	private String wechatOpenCode;
	private Integer wechatStatus;
	private String password;
	private String salt;
	private Date registerDate;
	private String registerIp;
	private Date firstLoginDate;
	private Date lastLoginDate;
	private String lastLoginIp;
	private String roleCode;
	private Integer statusNo;
	private String sourceCode;
	private String corpClueName;
	private Integer registerType;
	private Date certificationCreateDate;
	private Integer certificationType;
	private Integer certificationStatus;
	private Long certificationBy;
	private Date certificationDate;
	private String certificationRemark;
	private String code;
	private Long parentId;
	private String accounttype;
	private BigDecimal pointBalance;
	private String groupIds;
	private int corpCount;
	private String makeName;
	//头像图片
	private String headImg;
	//用户关联的企业信息
	private String  tempIds;
	private BigDecimal balance;
	private int version;
	private Date updateTime;
	//columns END


	/**
	 * 存款
	 * @param money 存入金额
	 */
	public void depositBalance(BigDecimal money) {
		userLock.lock();
		try {
			balance = balance.add(money);
			count.incrementAndGet();
		}
		finally {
			userLock.unlock();
		}
	}

	/**
	 * 取款
	 * @param money 取出金额
	 */
	public void drawBalance(BigDecimal money) {
		userLock.lock();
		try {
			balance = balance.subtract(money);
			count.incrementAndGet();
		}
		finally {
			userLock.unlock();
		}
	}

	public BigDecimal getBalance() {
		userLock.lock();
		try {
			return balance;
		}
		finally {
			userLock.unlock();
		}
	}

	public int getCount() {
		return count.get();
	}
}

