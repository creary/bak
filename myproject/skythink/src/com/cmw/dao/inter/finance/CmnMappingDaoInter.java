package com.cmw.dao.inter.finance;


import com.cmw.core.base.annotation.Description;
import com.cmw.core.base.dao.GenericDaoInter;
import com.cmw.core.base.exception.DaoException;
import com.cmw.core.util.DataTable;
import com.cmw.core.util.SHashMap;

import com.cmw.entity.finance.CmnMappingEntity;


/**
 * 列映射关系表  DAO接口
 * @author 赵世龙
 * @date 2013-11-22T00:00:00
 */
 @Description(remark="列映射关系表Dao接口",createDate="2013-11-22T00:00:00",author="赵世龙")
public interface CmnMappingDaoInter  extends GenericDaoInter<CmnMappingEntity, Long>{

	 <K, V> DataTable getCmnList(SHashMap<K, V> map) throws DaoException;
}
