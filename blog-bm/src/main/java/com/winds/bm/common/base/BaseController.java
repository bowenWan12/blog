package com.winds.bm.common.base;


import com.winds.bm.entity.User;
import com.winds.bm.oauth.AuthRealm.ShiroUser;
import com.winds.bm.service.LogService;
import com.winds.bm.service.MenuService;
import com.winds.bm.service.RoleService;
import com.winds.bm.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
	
	public User getCurrentUser() {
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(shiroUser == null) {
			return null;
		}
		User loginUser = userService.selectById(shiroUser.getId());
		return loginUser;
	}

	@Autowired
	protected UserService userService;

	@Autowired
	protected MenuService menuService;

	@Autowired
	protected RoleService roleService;

//	@Autowired
//	protected DictService dictService;
//
//	@Autowired
//	protected RescourceService rescourceService;
//
//	@Autowired
//	protected TableService tableService;
//
//	@Autowired
//	protected SiteService siteService;
//
	@Autowired
	protected LogService logService;
//
//	@Autowired
//	protected BlogArticleService blogArticleService;
//
//	@Autowired
//	protected BlogChannelService blogChannelService;
//
//	@Autowired
//	protected BlogCommentService blogCommentService;
//
//	@Autowired
//	protected BlogTagsService blogTagsService;
//
//	@Autowired
//	protected QuartzTaskService quartzTaskService;
//
//	@Autowired
//	protected QuartzTaskLogService quartzTaskLogService;
//
//	@Autowired
//	protected UploadInfoService uploadInfoService;
}
