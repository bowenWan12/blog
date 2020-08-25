//package com.winds.bm.common.base;
//
//import com.winds.bm.util.Constants;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.subject.Subject;
//import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
///**
// * Created by wangl on 2017/11/25.
// * todo:
// */
//public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter {
//    /**
//     * 覆盖默认实现，用sendRedirect直接跳出框架，以免造成js框架重复加载js出错。
//     * @param token
//     * @param subject
//     * @param request
//     * @param response
//     * @return
//     * @throws Exception
//     * @see FormAuthenticationFilter#onLoginSuccess(AuthenticationToken, Subject, ServletRequest, ServletResponse)
//     */
//    @Override
//    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
//                                     ServletRequest request, ServletResponse response) throws Exception {
//        //issueSuccessRedirect(request, response);
//        //we handled the success redirect directly, prevent the chain from continuing:
//        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
//        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
//        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + this.getSuccessUrl());
//
//        return true;
//    }
//
//    @Override
//    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        // 在这里进行验证码的校验
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        HttpSession session = httpServletRequest.getSession();
//
//        // 取出验证码
//        String validateCode = (String) session.getAttribute(Constants.VALIDATE_CODE);
//        // 取出页面的验证码
//        // 输入的验证和session中的验证进行对比
//        String randomcode = httpServletRequest.getParameter("verifycode");
//        System.out.println("--------------------------------"+validateCode+"_"+randomcode);
//        if (randomcode != null && validateCode != null && !randomcode.equals(validateCode)) {
//            // 如果校验失败，将验证码错误失败信息，通过shiroLoginFailure设置到request中
//            httpServletRequest.setAttribute("shiroLoginFailure", "kaptchaValidateFailed");//自定义登录异常
//            // 拒绝访问，不再校验账号和密码
//            return true;
//        }
//        return super.onAccessDenied(request, response);
//    }
//
//}
