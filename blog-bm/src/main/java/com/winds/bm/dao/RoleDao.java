package com.winds.bm.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.winds.bm.entity.Menu;
import com.winds.bm.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author wangl
 * @since 2017-10-31
 */
public interface RoleDao extends BaseMapper<Role> {

    Role selectRoleById(@Param("id") Long id);

    void saveRoleMenus(@Param("roleId") Long id, @Param("menus") Set<Menu> menus);

    void dropRoleMenus(@Param("roleId")Long roleId);

    void dropRoleUsers(@Param("roleId")Long roleId);
}