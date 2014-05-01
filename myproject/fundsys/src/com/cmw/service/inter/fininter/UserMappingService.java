package com.cmw.service.inter.fininter;


import com.cmw.core.base.annotation.Description;
import com.cmw.core.base.service.IService;
import com.cmw.entity.fininter.UserMappingEntity;


/**
 * 用户帐号映射  Service接口
 * @author 程明卫
 * @date 2013-03-28T00:00:00
 */
@Description(remark="用户帐号映射业务接口",createDate="2013-03-28T00:00:00",author="程明卫")
public interface UserMappingService extends IService<UserMappingEntity, Long> {
}
