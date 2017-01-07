package net.logvv.raven.common.model;


public enum ErrorCode 
{
	INVALID_REQ_PARAMS("000400", "请求参数不合法"),
	PERMISSION_DENY("000403","禁止访问"),
	SERVER_EXCEPTION("000500", "服务器忙,请稍后重试"),
	
	// 验证码错误码 0100XX
	INVALID_VALIDATE_CODE("010030","验证码错误"),
	LIMIT_ONE_MINITUE("010040","验证码获取太频繁了"),
	PHONE_NUMBER_INVALIAD("010050","号码无效"),
	PHONE_REBIND_SAME("010080","手机号码与原手机号码重复"),
	SEND_VALIDATE_CODE_FAIL("010060","验证码发送失败"),
	UNSUPPORTED_VALIDATE_TYPE("010070","不支持的验证类型"),
	UNKNOWN_CHECK_STATUS("010071","不支持的审核结果"),

	FORMAT_UNSUPPORTED("020010","不支持的文件格式"),
	INVALID_INVITE_CODE("020011","无效的激活码"),
	
	// 用户错误码 03XXXX
	EXPIRED_ACCESSTOKEN("030010","登录失效，请重新登录"),
	USER_ALREADY_EXIST("030020","用户已存在"),
	USER_NOT_EXIST("030021","用户尚未注册"),
	USER_PASSWORD_UNMATCH("030022","用户名或密码错误"),
	PASSWORD_ERROR("030023","密码错误"),
	USER_IDENTIFY_EXIST("030024","当前学(工)号已绑定，请查证"),
	USER_IDENTIFY_EXIST_TWICE("030025","您已验证过信息，请不要二次验证"),
	USER_NOT_REGISTER_OR_CHECKED("031030","当前用户尚未注册或审核,请先注册通过审核"),
	USER_CHECKINFO_NOT_EXIST("031031","未找到用户信息,请确认已提交审核"),
	USER_ALREADY_CHECKED("031032","当前用户已通过通过认证,请不要重复审核"),
	USER_CHECK_ERROR("031033","验证失败,请核对个人信息是否有误"),
	USER_FEEDBACK_RECORD_NOT_EXIST("031034","爆料记录不存在"), 
	USER_FEEDBACK_HAD_BEEN_ADOPT("031035","爆料记录已被采用或驳回，请勿重复操作"),
	USER_FEEDBACK_ADOPTSTATUS_NOT_ALLOWED("031036","不允许设置爆料记录为待回复"),
	USER_FEEDBACK_ADOPTS_FAILED("031037","爆料记录回复失败"),
	USER_IDENTITY_NOT_CHECK("031038","您还没有验证身份,请先验证"),
	
	// 角色权限
	ROLE_EXIST("031040","已存在同名角色"),
	ROLE_NOT_ALLOCATE("031041","当前用户尚未赋予角色"),
	PERMISSION_EXIST("031042","已存在同名的权限"),
	
	MEMBER_NOT_IN_ORG("040010","成员尚未加入当前组织"),
	MEMBER_NO_PERMISSION("040011","您还没有加入组织或权限不足"),
	MEMBER_EXIST_IN_ORG("040012","您已加入组织,请不要重复加入"),
	MEMBER_CREATOR_QUIT_DENY("040013","创建者不能退出组织"),
	MEMBER_NOT_VERIFIER("040014","您选择的审核人身份不合法"),
	VERIFIER_NOT_IN_ORG("040015","你选择的审核人不存在"),
	MEMBER_ALREADY_APPLY("040016","您已经提交过申请了，请耐心等待"),
    MEMBER_OVERFLOW("040017","人数已满"),
    HX_SYNC_ERROR("040018","同步群组失败"),

	MANAGER_NO_PERMISSION("040050","操作拒绝,无权限或权限不足"),
	
	// 组织错误码 
	ORG_NOT_EXIST("041010","组织不存在"),
	ORG_WATCHWORD_ERROR("042010","口令错误"),
	ORG_ALREADY_FROZEN("042011","组织已是冻结状态了"),
	ORG_NAME_CONFLICT("042012","名称冲突,当前学校已有同名组织"),
	ORG_CREATE_FAILURE("042013","创建组织失败"),
	ORG_CREATOR_NOT_ALLOWED_APPLY("042014","您已是创建者，无需申请加入"),
	ORG_CREATOR_CONNONT_MODIFY_ROLE("042015","不能修改组织创建者的组织身份"),
	ORG_FIXED_CLASS_QUIT_DENY("042016","固定组织/班级不支持退出"),
	ORG_ICON_OWNED("042017","您已拥有该图标,无需重复购买"),
	
	// 新闻错误码
	NEWS_ALREADY_LIKED("050010","您已经关注过该新闻了"),
	NEWS_NOT_LIKED("050011","您还没有关注该条新闻"),
	NEWS_NOT_EXIST("050020","新闻不存在或已删除"),
	EXPANSION_CANNOT_EMPTY("050021","扩展新闻标题或内容不能为空"),
	NEWS_ALREADY_PUBLISH("050022","新闻已发布,请不要重复发布"),
	NEWS_VOTED("050023","您已经投过票了"),
	NEWS_VOTE_DENY("050024","当前新闻暂不支持投票"),
	
	COMMENT_EMPTY("060010","评论不能为空"),
	COMMENT_NOT_EXIST("060020","评论不存在"),
	COMMENT_ERROR("060021","评论失败，请稍后重试"),
	
	NOTICE_CHECK_DENY("070010","您无权审核当前通知"),
	NOTICE_ALREADY_CHECKED("070020","通知已被审核过了"),
	NOTICE_CHECK_FAILED("070021","审核通知失败"),
	NOTICE_FEEDBACK_EXIST("070022","您已经回复过本通知"),
	NOTICE_FEEDBACK_NOT_NEED("070023","当前通知不需要回执"),
	
	FEEDBACK_EMPTY("090010","反馈内容不能为空"),
	
	// 活动错误码
	ATC_NOT_EXIST("050010","暂无活动"),
	ATC_TIME_CONFLICT("050100","活动时间冲突"),
	ATC_TIME_OUT("050101","活动已开始或报名时间已过"),
	ACT_PERMISSION_DENY("050102","您无权进行当前操作"),
	ATC_ENROLL_CONDITION("050103","您还不是该组织成员，请先加入组织再报名活动"),
	ATC_CREATE_FAILED("050104","参数有误，新建活动失败"),
	ATC_START_TIME_INVALID("050105","活动开始时间无效！"),
	ACT_APPLY_DEADLINE_INVALID("050118","报名截止时间无效"),
	ATC_TOP_TIME_NOT_TIMEOUT("050106","活动置顶时间未过期，请勿重新设置每分钟积分消耗数"),
	ATC_TOP_TIME_NOT_ALLOW("050107","每分钟积分消耗数不能小于等于0"),
	ATC_TOP_DENY("050108","只有活动创建者才能发起积分置顶"),
	ATC_CREATOR_NOT_EXIST("050109","此活动没有创建者"),
	ATC_NOT_VARIFY("050110","此活动未审核，不允许查看"),
	ACT_APPLY_EXIST("050111","您已经提交过申请,请耐心等待审核结果"),
	ACT_NOT_ORG_CREATOR("050112","非组织创建者，无权限创建组织活动"),
	ATC_CREATOR_NO_ALLOW_ENROLL("050113","活动创建者不能报名参加活动"),
	ATC_CANNOT_REMOVE_MEMBER("050114","活动已经开始，不能踢出成员"),
	ATC_HAS_FOUCS("050115","活动已经收藏，请勿重复操作"),
	ACT_VOTE_UNSUPPOERTED("050116","当前活动暂不支持投票"),
	ACT_VOTED("050117","您已经投过票了"),
	ACT_MEMBER_NOT_IN("050118","您还不是活动成员,请先报名或等待报名通过"),
    ACT_VOTE_TARGET_NOT_EXIST("050119","无效的投票对象"),
    ACT_MEMBER_FULL("050120","活动人数已满"),
    ACT_VOTE_TIME_OUT("050121","投票已结束"),
	ACT_VOTE_NOT_START("050122","投票还未开始"),

	COMMENT_TYPE_NOT_SUPPORT("050118","不支持的评论类型"),
	
	// 积分错误码
	SCORE_GOODS_NOT_EXIST("100100","积分商品不存在"),
	USER_SCORE_RECORD_NOT_EXIST("100101","当前用户没有积分记录"),
	SCORE_NOT_ENOUGH("100101","您的积分总数不足"),
	BONUS_FAILDE("100102","积分抽奖失败！"),
	ACTION_TYPE_NOT_EXIST("100103","不支持的活跃行为统计类型"),
	SCORE_RANGE_NOT_ALLOW("100104","兑换积分下限不能大于上限"),
	SCORE_GOODS_ALL_HAS_EXCHANGED("100105","该积分商品已全部兑换完"),
	SCORE_EXCHANGE_RECORD_NOT_EXIST("100106","商品兑换记录不存在"),
	SOCRE_PROBABILITY_NOT_CORRECT("100107","请校准所有概率和为100"),
    SCORE_LEVEL_NOT_ENOUGH("100108","您的等级不满足当前商品兑换等级"),
    SCORE_GOODS_NUM_ERROR("100109","商品数量不得小于已兑换数量"),
	
	MISSION_NOT_EXIST("200100","不支持的任务类型"),
	
	MANAGER_NAME_HAS_EXIST("300100","用户账户已存在"),
	MANAGER_NOT_EXIST("300101","管理员账号不存在"),
	
	// 学教学错误码
	COURSE_RECENTLY_NONE("400100","暂无课程"),
	COURSE_CODE_HAS_EXIST("400101","课程编号已存在"),
	COURSE_NOT_EXIST("400102","课程不存在"),
	CLASS_CODE_CANNOT_MODIFY("400103","班级编号不能修改"),
	COURSE_CODE_CANNOT_MODIFY("400104","课程编号不能修改"),
	
	CAMPUS_DPTCODE_EMPTY("500100","请指定院系编号"),
	CAMPUS_STUDENT_HAS_EXIST("500101","当前学生已存在"),
	CAMPUS_STUDENT_SHOULD_BIND_CAMPUS("500102","请指定学生所属学校"),
	CAMPUS_NOT_EXIST("500103","学校不存在"),
	CLASS_NOT_EXIST("500104","班级不存在"),
	CLASS_HAS_EXIST("500105","班级已存在"),
	TEACHER_NOT_EXIST("500106","教师不存在"),
	TEACHER_HAS_EXIST("500107","教师已存在"),
	CAMPUS_DPTCODE_INVALID("500108","院系编号无效"),
	NO_AUTHORITY("500108","非本学校记录，无权修改"),
	CAMPUS_DEPARTMENT_MAJOR_NOT_EXIST("500109","院系专业不存在"),
	CLASS_NOT_ALLOW_DELETE("500110","此班级已有学生，不能删除！"),
	STUDENT_NOT_EXIST("500111","学生不存在!"),
	STUDNET_HAS_BINDED("500112","学生已被绑定"),
	STUDENT_NOT_IN_CLASS("500113","此班级内不存在该学生！"),
	CAMPUS_NOT_ASSIGN("500114","请指定学校"),
	
	EMAIL_IS_EMPTY("600100","邮箱为空，无法发送重置的密码，请联系平台管理员！"),
	
	IMPORT_DATA_FAILED("700100","导入数据失败！"),
	IMPORT_DATA_TYPE_NOT_SUPPORTED("700101","导入数据类型不支持"),
	
	CHAT_ROOM_NOT_EXIST("800100","聊天室不存在"),
	CHAT_ROOM_OWNER_DENY("800101","你不是聊天室房主"),
    CHAT_USER_OVERFLOW("800102","人数已超出上限,最多2000"),
    CHAT_USER_RANGE_ERROR("800103","人数不合理,范围2~2000"),
	
	VERSION_NOT_EXIST("900100","当前版本已删除"),
	VERSION_IS_LATEST("900101","当前版本已是最新版本,最新版本必须是维护状态");
	
	
    private String code;
    
    private String msg;
    
    /** 产品错误码前缀 */
    private ErrorCode(String errCode, String errMsg)
    {
        this.code = errCode;
        this.msg = errMsg;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
    
}
