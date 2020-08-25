package com.winds.bm.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.winds.bm.common.annotation.LogAnnotation;
import com.winds.bm.common.base.BaseController;
import com.winds.bm.common.base.MySysUser;
import com.winds.bm.dto.LoginUser;
import com.winds.bm.entity.Menu;
import com.winds.bm.entity.User;
import com.winds.bm.oauth.OAuthSessionManager;
import com.winds.bm.util.Constants;
import com.winds.common.constant.Result;
import com.winds.common.constant.ResultCode;
import com.winds.common.util.VerifyCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;


@RestController
public class LoginController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(com.winds.bm.controller.LoginController.class);

	@Value("${server.port}")
	private String port;

	@GetMapping("login")
	public Result login(HttpServletRequest request) {
		logger.info("跳到这边的路径为:"+request.getRequestURI());
		Subject s = SecurityUtils.getSubject();
		logger.info("是否记住登录--->"+s.isRemembered()+"<-----是否有权限登录----->"+s.isAuthenticated()+"<----");
		if(s.isAuthenticated()){
			logger.info("-----------有权限-----------");
			return Result.success();
		}else {
			logger.info("-----------无权限-----------");
			return Result.error(ResultCode.PERMISSION_NO_ACCESS);
		}
	}
	
	@PostMapping("login/main")
	@LogAnnotation(module = "用户登录", operation = "用户登录")
	public Result loginMain(HttpServletRequest request, @RequestBody LoginUser loginUser) {
		String username = loginUser.getUsername();
		String password = loginUser.getPassword();
		String rememberMe = loginUser.getRememberMe();
		String verifycode = loginUser.getVerifycode();
		logger.info(username+"_"+password+"_"+verifycode+"_"+rememberMe);
		Result r = new Result();
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
			return r.error(ResultCode.PARAM_IS_BLANK,"用户名或者密码不能为空");
		}
		if(StringUtils.isBlank(rememberMe)){
			rememberMe = "false";
//			return Result.error(ResultCode.PARAM_IS_BLANK,"记住我不能为空");
		}
		if(StringUtils.isBlank(verifycode)){
			return  r.error(ResultCode.PARAM_IS_BLANK,"验证码不能为空");
		}
		Map<String,Object> map = Maps.newHashMap();
		String error = null;
		HttpSession session = request.getSession();
		if(session == null){
			return r.error(ResultCode.SESSION_TIME_OUT,"session超时");
		}
		System.out.println("------------------------------------"+session.getId());
		String trueCode =  (String)session.getAttribute(Constants.VALIDATE_CODE);
		if(StringUtils.isBlank(trueCode)){
			return r.error(ResultCode.INTERFACE_REQUEST_TIMEOUT,"验证码超时");
		}
		if(StringUtils.isBlank(verifycode) || !trueCode.toLowerCase().equals(verifycode.toLowerCase())){
			error = "验证码错误";
		}else {
			/*就是代表当前的用户。*/
			Subject user = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username,password,Boolean.valueOf(rememberMe));

			try {
				user.login(token);
				if (user.isAuthenticated()) {
//					map.put("url","index");
//					r.simple().put("url","index");
					r.simple().put(OAuthSessionManager.OAUTH_TOKEN, user.getSession().getId());
				}
			}catch (IncorrectCredentialsException e) {
				error = "登录密码错误.";
			} catch (ExcessiveAttemptsException e) {
				error = "登录失败次数过多";
			} catch (LockedAccountException e) {
				error = "帐号已被锁定.";
			} catch (DisabledAccountException e) {
				error = "帐号已被禁用.";
			} catch (ExpiredCredentialsException e) {
				error = "帐号已过期.";
			} catch (UnknownAccountException e) {
				error = "帐号不存在";
			} catch (UnauthorizedException e) {
				error = "您没有得到相应的授权！";
			}
		}
		if(StringUtils.isBlank(error)){
			r.setResultCode(ResultCode.SUCCESS);

			return r;
		}else{
			return r.error(ResultCode.ERROR, error);
		}
	}
	
	@GetMapping("index")
	public String showView(Model model){
		return "index";
	}


	/**
	 * 获取验证码图片和文本(验证码文本会保存在HttpSession中)
	 */
	@GetMapping("genCaptcha")
	public @ResponseBody Result genCaptcha(HttpServletRequest request) throws IOException {
		//设置页面不缓存
//		response.setHeader("Pragma", "no-cache");
//		response.setHeader("Cache-Control", "no-cache");
//		response.setDateHeader("Expires", 0);
		String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_ALL_MIXED, 4, null);
		//将验证码放到HttpSession里面
		request.getSession().setAttribute(Constants.VALIDATE_CODE, verifyCode);
		System.out.println("------------------------------------"+request.getSession().getId());
		logger.info("本次生成的验证码为[" + verifyCode + "],已存放到HttpSession中");
		//设置输出的内容的类型为JPEG图像
//		response.setContentType("image/jpeg");
//		BufferedImage bufferedImage = VerifyCodeUtil.generateImageCode(verifyCode, 116, 36, 5, true, new Color(249,205,173), null, null);
		//写给浏览器
//		ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
		return Result.success(verifyCode);
	}

	@GetMapping("main")
	public Result main(Model model){
		System.out.println("获取当前用户菜单树");
		Map map = userService.selectUserMenuCount();
		User user = userService.findUserById(MySysUser.id());
//		Set<Menu> menus = user.getMenus();
//		java.util.List<Menu> showMenus = Lists.newArrayList();
//		if(menus != null && menus.size()>0){
//			for (Menu menu : menus){
//				if(StringUtils.isNotBlank(menu.getHref())){
//					Long result = (Long)map.get(menu.getPermission());
//					if(result != null){
//						menu.setDataCount(result.intValue());
//						showMenus.add(menu);
//					}
//				}
//			}
//		}
//		showMenus.sort(new com.winds.bm.controller.MenuComparator());
//		model.addAttribute("userMenu",showMenus);
//		System.out.println(showMenus);

		return Result.success();
	}

	/**
	 *  空地址请求
	 * @return
	 */
	@GetMapping(value = "")
	public String index() {
		logger.info("这事空地址在请求路径");
		Subject s = SecurityUtils.getSubject();
		return s.isAuthenticated() ? "redirect:index" : "login";
	}

	@GetMapping("systemLogout")
	@LogAnnotation(module = "用户登出", operation = "退出系统")
	public String logOut(){
		SecurityUtils.getSubject().logout();
		return "redirect:/login";
	}
}

class MenuComparator implements Comparator<Menu> {

	@Override
	public int compare(Menu o1, Menu o2) {
		if(o1.getSort()>o2.getSort()){
			return -1;
		}else {
			return 0;
		}

	}
}