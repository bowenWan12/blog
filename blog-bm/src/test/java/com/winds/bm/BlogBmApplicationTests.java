package com.winds.bm;

import com.google.common.collect.Lists;
import com.winds.bm.dto.MenuTree;
import com.winds.bm.entity.Menu;
import com.winds.bm.entity.User;
import com.winds.bm.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogBmApplicationTests {

    @Autowired
    private UserService userService;
    @Test
    public void testDao(){
        User userById = userService.findUserById(1L);
        System.out.println(userById);
    }
    @Test
    public void contextLoads() {
    }

    @Test
    public void getMenu(){

        List<MenuTree> menuTree = Lists.newArrayList();
        Map map = userService.selectUserMenuCount();
        User user = userService.findUserById(1L);
        Set<Menu> menus = user.getMenus();
        System.out.println("==================================================================================");
        System.out.println(menus);
        List<Menu> baseMenu = Lists.newArrayList();
        if(menus != null && menus.size()>0){
            for (Menu menu : menus){
                if(StringUtils.isNotBlank(menu.getHref())){
                    Long result = (Long)map.get(menu.getPermission());
                    if(result != null){
                        menu.setDataCount(result.intValue());
                        baseMenu.add(menu);
                    }
                }
            }
        }
        System.out.println("==================================================================================");
        System.out.println(baseMenu);
        baseMenu.sort(new MenuComparator());
        for (Menu menu: baseMenu) {
            if (menu.getLevel()==1) {
                MenuTree mt = new MenuTree();
                mt.setIcon(menu.getIcon());
                mt.setLabel(menu.getDesc());
                mt.setName(menu.getName());
                mt.setPath(menu.getHref());
                mt.setUrl(menu.getTarget());

                menuTree.add(mt);
            } else if (menu.getLevel()==2) {
                MenuTree mt1 = new MenuTree();
                mt1.setIcon(menu.getIcon());
                mt1.setLabel(menu.getDesc());
                mt1.setName(menu.getName());
                mt1.setPath(menu.getHref());
                mt1.setUrl(menu.getTarget());
                if (menuTree.get(menuTree.size()-1).getChildren() == null){
                    List<MenuTree> mtl = new ArrayList<>();
                    mtl.add(mt1);
                    menuTree.get(menuTree.size()-1).setChildren(mtl);
                } else {
                    menuTree.get(menuTree.size()-1).getChildren().add(mt1);
                }

            }
        }


        System.out.println("==================================================================================");
        System.out.println(menuTree);
    }
}
class MenuComparator implements Comparator<Menu> {

    @Override
    public int compare(Menu o1, Menu o2) {
        if(o1.getSort()<o2.getSort()){
            return -1;
        }else {
            return 0;
        }

    }
}
