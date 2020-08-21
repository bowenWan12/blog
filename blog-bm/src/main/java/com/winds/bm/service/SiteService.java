package com.winds.bm.service;

import com.baomidou.mybatisplus.service.IService;
import com.winds.bm.entity.Site;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangl
 * @since 2017-12-30
 */
public interface SiteService extends IService<Site> {

    Site getCurrentSite();

    void updateSite(Site site);
	
}
