package com.cmw.dao.inter.finance;


import com.cmw.core.base.annotation.Description;
import com.cmw.core.base.dao.GenericDaoInter;
import com.cmw.core.base.exception.DaoException;
import com.cmw.core.util.DataTable;
import com.cmw.core.util.SHashMap;

import com.cmw.entity.finance.PrintTempEntity;
import com.cmw.entity.finance.TdsCfgEntity;


/**
 * 合同模板表  DAO接口
 * @author 赵世龙
 * @date 2013-11-19T00:00:00
 */
 @Description(remark="合同模板表Dao接口",createDate="2013-11-19T00:00:00",author="赵世龙")
public interface PrintTempDaoInter  extends GenericDaoInter<PrintTempEntity, Long>{

	 /**
	  * 根据模板数据配置，查找模板详情
	  * @param entity 模板数据配置实体
	  * @param cmns 查找到的数据列排列顺序
	  * @return
	  * @throws DaoException
	  */
	 <K, V> DataTable getTempDetail(TdsCfgEntity entity,String cmns,SHashMap<String, Object> map) throws DaoException;
}
