package com.cmw.dao.inter.finance;


import com.cmw.core.base.annotation.Description;
import com.cmw.core.base.dao.GenericDaoInter;

import com.cmw.entity.finance.OrderEntity;


/**
 * 扣款优先级  DAO接口
 * @author pdt
 * @date 2012-12-22T00:00:00
 */
 @Description(remark="扣款优先级Dao接口",createDate="2012-12-22T00:00:00",author="pdt")
public interface OrderDaoInter  extends GenericDaoInter<OrderEntity, Long>{

}
