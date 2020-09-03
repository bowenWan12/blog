package com.winds.bm.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.winds.bm.common.base.BaseController;
import com.winds.bm.common.base.MySysUser;
import com.winds.bm.entity.Role;
import com.winds.bm.entity.User;
import com.winds.bm.entity.vo.ShowMenu;
import com.winds.bm.util.Constants;
import com.winds.bm.util.LayerData;
import com.winds.common.constant.Result;
import com.winds.common.constant.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@RestController
@RequestMapping("admin/system/user/")
public class UserConteroller extends BaseController {
//    private static final Logger LOGGER = LoggerFactory.getLogger(com.mysiteforme.admin.controller.system.UserConteroller.class);

//    @GetMapping("list")
//    @SysLog("跳转系统用户列表页面")
//    public String list(){
//        return "admin/system/user/list";
//    }

//    @RequiresPermissions("sys_user_list")
    @GetMapping("getUser")
    public Result getUser(@RequestParam(value = "page",defaultValue = "1")Integer page,
                          @RequestParam(value = "limit",defaultValue = "10")Integer limit,
                          String name){
        Result r = new Result();
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (StringUtils.isNotBlank(name)){
            userEntityWrapper.eq("login_name",name).or().like("nick_name", name);
        }
        Page<User> userPage = userService.selectPage(new Page<>(page,limit),userEntityWrapper);
        r.setResultCode(ResultCode.SUCCESS);
        //TODO 修改返回信息---userPage.getTotal()、userPage.getRecords()
        r.setData(userPage);
        return  r;
    }

//    @RequiresPermissions("sys_user_add")
    @PostMapping("add")
    @ResponseBody
//    @SysLog("保存新增系统用户数据")
    public Result add(@RequestBody User user){
        System.out.println("----------------------"+user.toString());
        Result r = new Result();
        if(StringUtils.isBlank(user.getLoginName())){
            return r.error(ResultCode.PARAM_IS_BLANK,"登录名不能为空");
        }
        if(user.getRoleLists() == null || user.getRoleLists().size() == 0){
            return  r.error(ResultCode.PARAM_TYPE_BIND_ERROR,"用户角色至少选择一个");
        }
        if(userService.userCount(user.getLoginName())>0){
            return r.error(ResultCode.DATA_ALREADY_EXISTED,"登录名称已经存在");
        }
        if(StringUtils.isNotBlank(user.getEmail())){
            if(userService.userCount(user.getEmail())>0){
                return r.error(ResultCode.DATA_ALREADY_EXISTED,"该邮箱已被使用");
            }
        }
        if(StringUtils.isNoneBlank(user.getTel())){
            if(userService.userCount(user.getTel())>0){
                return r.error(ResultCode.DATA_ALREADY_EXISTED,"该手机号已被绑定");
            }
        }
        user.setPassword(Constants.DEFAULT_PASSWORD);
        userService.saveUser(user);
        if(user.getId() == null || user.getId() == 0){
            return r.error(ResultCode.ERROR,"保存用户信息出错");
        } else {
            r.setResultCode(ResultCode.SUCCESS);
        }
        return r;
    }

//    @GetMapping("edit")
//    public String edit(Long id, Model model){
//        User user = userService.findUserById(id);
//        List<Long> roleIdList = Lists.newArrayList();
//        if(user != null) {
//            Set<Role> roleSet = user.getRoleLists();
//            if (roleSet != null && roleSet.size() > 0) {
//                for (Role r : roleSet) {
//                    roleIdList.add(r.getId());
//                }
//            }
//        }
//        List<Role> roleList = roleService.selectAll();
//        model.addAttribute("localuser",user);
//        model.addAttribute("roleIds",roleIdList);
//        model.addAttribute("roleList",roleList);
//        return "admin/system/user/edit";
//    }

//    @RequiresPermissions("sys:user:edit")
    @PostMapping("edit")
    @ResponseBody
//    @SysLog("保存系统用户编辑数据")
    public Result edit(@RequestBody User user){
        System.out.println("============================================================================================== ");
        System.out.println(user.toString());
        Result r = new Result();
        if(user.getId() == 0 || user.getId() == null){
            return r.error(ResultCode.PARAM_IS_BLANK,"用户ID不能为空");
        }
        if(StringUtils.isBlank(user.getLoginName())){
            return r.error(ResultCode.PARAM_IS_BLANK,"登录名不能为空");
        }
        if(user.getRoleLists() == null || user.getRoleLists().size() == 0){
            return  r.error(ResultCode.PARAM_NOT_COMPLETE,"用户角色至少选择一个");
        }
        User oldUser = userService.findUserById(user.getId());
        if(StringUtils.isNotBlank(user.getEmail())){
            if(!user.getEmail().equals(oldUser.getEmail())){
                if(userService.userCount(user.getEmail())>0){
                    return r.error(ResultCode.DATA_ALREADY_EXISTED,"该邮箱已被使用");
                }
            }
        }
        if(StringUtils.isNotBlank(user.getLoginName())){
            if(!user.getLoginName().equals(oldUser.getLoginName())) {
                if (userService.userCount(user.getLoginName()) > 0) {
                    return r.error(ResultCode.DATA_ALREADY_EXISTED,"该登录名已存在");
                }
            }
        }
        if(StringUtils.isNotBlank(user.getTel())){
            if(!user.getTel().equals(oldUser.getTel())) {
                if (userService.userCount(user.getTel()) > 0) {
                    return r.error(ResultCode.DATA_ALREADY_EXISTED,"该手机号已经被绑定");
                }
            }
        }
        user.setIcon(oldUser.getIcon());
        userService.updateUser(user);

        Map<String,Object> m = new HashMap<>();
        User u = userService.findUserByLoginName(user.getLoginName());
        List<Map<String,String>> roleIdList = Lists.newArrayList();
        if(u != null) {
            Set<Role> roleSet = u.getRoleLists();
            if (roleSet != null && roleSet.size() > 0) {
                for (Role rr : roleSet) {
                    Map<String,String> mm = new HashMap<>();
                    mm.put("roleId", rr.getId().toString());
                    mm.put("roleNm", rr.getName());

                    roleIdList.add(mm);
                }
            }
        }

        m.put("userRole", roleIdList);
        r.setData(m);
        r.setResultCode(ResultCode.SUCCESS);
        return r;
    }

//    @RequiresPermissions("sys:user:delete")
    @PostMapping("delete")
    @ResponseBody
//    @SysLog("删除系统用户数据(单个)")
    public Result delete(@RequestParam(value = "id",required = false)String id){
        if(id == null || id.equals("0") || id.equals("1")){
            return Result.error(ResultCode.PARAM_IS_BLANK,"参数错误");
        }
        User user = userService.findUserById(Long.valueOf(id));
        if(user == null){
            return Result.error(ResultCode.DATA_IS_WRONG,"用户不存在");
        }
        userService.deleteUser(user);
        return Result.success();
    }

//    @RequiresPermissions("sys:user:delete")
//    @PostMapping("deleteSome")
//    @ResponseBody
//    @SysLog("删除系统用户数据(多个)")
//    public RestResponse deleteSome(@RequestBody List<User> users){
//        if(users == null || users.size()==0){
//            return RestResponse.failure("请选择需要删除的用户");
//        }
//        for (User u : users){
//            if(u.getId() == 1){
//                return RestResponse.failure("不能删除超级管理员");
//            }else{
//                userService.deleteUser(u);
//            }
//        }
//        return RestResponse.success();
//    }

    /***
     * 获得用户所拥有的菜单列表
     * @return
     */
    @GetMapping("getUserMenu")
    @ResponseBody
    public List<ShowMenu> getUserMenu(){
        Long userId = MySysUser.id();
        List<ShowMenu> list = menuService.getShowMenuByUser(userId);
        return list;
    }
//
//    @GetMapping("userinfo")
//    public String toEditMyInfo(Model model){
//        Long userId = MySysUser.id();
//        User user = userService.findUserById(userId);
//        model.addAttribute("userinfo",user);
//        model.addAttribute("userRole",user.getRoleLists());
//        return "admin/system/user/userInfo";
//    }

    @PostMapping("saveUserinfo")
//    @SysLog("系统用户个人信息修改")
    @ResponseBody
    public Result saveUserInfo(User user){
        if(user.getId() == 0 || user.getId() == null){
            return Result.error(ResultCode.PARAM_NOT_COMPLETE,"用户ID不能为空");
        }
        if(StringUtils.isBlank(user.getLoginName())){
            return Result.error(ResultCode.PARAM_NOT_COMPLETE,"登录名不能为空");
        }
        User oldUser = userService.findUserById(user.getId());
        if(StringUtils.isNotBlank(user.getEmail())){
            if(!user.getEmail().equals(oldUser.getEmail())){
                if(userService.userCount(user.getEmail())>0){
                    return Result.error(ResultCode.DATA_ALREADY_EXISTED,"该邮箱已被使用");
                }
            }
        }
        if(StringUtils.isNotBlank(user.getTel())){
            if(!user.getTel().equals(oldUser.getTel())) {
                if (userService.userCount(user.getTel()) > 0) {
                    return Result.error(ResultCode.DATA_ALREADY_EXISTED,"该手机号已经被绑定");
                }
            }
        }
        user.setRoleLists(oldUser.getRoleLists());
        userService.updateUser(user);
        return Result.success();
    }

//    @GetMapping("changePassword")
//    public String changePassword(){
//        return "admin/system/user/changePassword";
//    }

//    @RequiresPermissions("sys:user:changePassword")
//    @PostMapping("changePassword")
//    @SysLog("用户修改密码")
//    @ResponseBody
//    public RestResponse changePassword(@RequestParam(value = "oldPwd",required = false)String oldPwd,
//                                       @RequestParam(value = "newPwd",required = false)String newPwd,
//                                       @RequestParam(value = "confirmPwd",required = false)String confirmPwd){
//        if(StringUtils.isBlank(oldPwd)){
//            return RestResponse.failure("旧密码不能为空");
//        }
//        if(StringUtils.isBlank(newPwd)){
//            return RestResponse.failure("新密码不能为空");
//        }
//        if(StringUtils.isBlank(confirmPwd)){
//            return RestResponse.failure("确认密码不能为空");
//        }
//        if(!confirmPwd.equals(newPwd)){
//            return RestResponse.failure("确认密码与新密码不一致");
//        }
//        User user = userService.findUserById(MySysUser.id());
//
//        //旧密码不能为空
//        String pw = ToolUtil.entryptPassword(oldPwd,user.getSalt()).split(",")[0];
//        if(!user.getPassword().equals(pw)){
//            return RestResponse.failure("旧密码错误");
//        }
//        user.setPassword(newPwd);
//        ToolUtil.entryptPassword(user);
//        userService.updateUser(user);
//        return RestResponse.success();
//    }

}
