package com.winds.bm.service;

import com.baomidou.mybatisplus.service.IService;
import com.winds.bm.entity.Menu;
import com.winds.bm.entity.Role;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangl
 * @since 2017-10-31
 */
public interface RoleService extends IService<Role> {

    Role saveRole(Role role);

    Role getRoleById(Long id);

    void updateRole(Role role);

    void deleteRole(Role role);

    void saveRoleMenus(Long id, Set<Menu> menuSet);

    void dropRoleMenus(Long id);

    Integer getRoleNameCount(String name);

    List<Role> selectAll();
	
}