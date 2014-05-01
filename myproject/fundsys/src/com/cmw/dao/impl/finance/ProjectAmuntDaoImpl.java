package com.cmw.dao.impl.finance;


import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.cmw.constant.BussStateConstant;
import com.cmw.constant.SysConstant;
import com.cmw.core.base.annotation.Description;
import com.cmw.core.base.dao.GenericDaoAbs;
import com.cmw.core.base.exception.DaoException;
import com.cmw.core.util.DataTable;
import com.cmw.core.util.DateUtil;
import com.cmw.core.util.SHashMap;
import com.cmw.core.util.SqlUtil;
import com.cmw.core.util.StringHandler;
import com.cmw.dao.inter.finance.AmountRecordsDaoInter;
import com.cmw.dao.inter.finance.ProjectAmuntDaoInter;
import com.cmw.entity.finance.AmountRecordsEntity;
import com.cmw.entity.finance.ProjectAmuntEntity;


/**
 * 项目费用表 DAO实现类
 * @author liitng
 * @date 2013-01-15T00:00:00
 */
@Description(remark="项目费用表DAO实现类",createDate="2013-01-15T00:00:00",author="程明卫")
@Repository("projectAmuntDao")
public class ProjectAmuntDaoImpl extends GenericDaoAbs<ProjectAmuntEntity, Long> implements ProjectAmuntDaoInter{
	
	/**
	 * 获取逾期实收金额记录信息
	 * @param params
	 * @return 
	 * @throws DaoException 
	 */
	public DataTable getLateRecords(SHashMap<String, Object> params) throws DaoException{
		try{
			StringBuffer sbhql = new StringBuffer();
			params = SqlUtil.getSafeWhereMap(params);
			sbhql.append("select A.invoceId,cat,rat,mat,pat,dat,rectDate,bussTag from AmountRecordsEntity A ")
				.append(" where isenabled='"+SysConstant.OPTION_ENABLED+"' ");
			Object bussTag = params.get("bussTag");
			if(StringHandler.isValidObj(bussTag)){
				sbhql.append(" and A.bussTag in ("+bussTag+") ");
			}
			
			Object invoceIds = params.get("invoceIds");
			if(StringHandler.isValidObj(invoceIds)){
				sbhql.append(" and A.invoceId in ("+invoceIds+") ");
			}
			sbhql.append(" order by A.invoceId,A.rectDate ");
			return find(sbhql.toString(), "invoceId,cat,rat,mat,pat,dat,rectDate,bussTag");
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}
	
	@Override
	public <K, V> DataTable getDsByAmountLogId(SHashMap<K, V> params,
			int offset, int pageSize) throws DaoException {
		try{
			return super.getResultList(params, offset, pageSize);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}
	
//	
//	@Override
//	public <K, V> DataTable getResultList(SHashMap<K, V> params, int offset,
//			int pageSize) throws DaoException {try{
//			params = SqlUtil.getSafeWhereMap(params);
//			StringBuffer sqlSb = new StringBuffer();
//			sqlSb.append("select A.bussTag,A.cat,A.rat,A.mat,A.tat,A.rectDate,B.account as account ,B.bankName as bankName ")
//				.append(" from fc_AmountRecords A inner join ts_Account B on A.accountId = B.id ")
//				.append(" where  A.bussTag in ('"+BussStateConstant.AMOUNTRECORDS_BUSSTAG_0+"') ")
//				.append(" and A.isenabled = '"+SysConstant.OPTION_ENABLED+"' ");
//			
//			Long contractId = params.getvalAsLng("contractId");
//			if(StringHandler.isValidObj(contractId)){
//				sqlSb.append(" and A.contractId = '"+contractId+"' ");
//			}
//			String sqlStr = sqlSb.toString();
//			String colNames = "bussTag,cat,rat,mat,"
//					+ "tat,rectDate#yyyy-MM-dd,account,bankName";
//			DataTable dt = findBySqlPage(sqlStr, colNames, offset, pageSize);
//			return dt;
//		} catch (DataAccessException e) {
//			e.printStackTrace();
//			throw new DaoException(e);
//		}
//}
	/**
	 * 逾期还款详情页面的grid中取数据的方法
	 */
	@Override
public <K, V> DataTable getLoanRecordsList(SHashMap<K, V> params,
		int offset, int pageSize) throws DaoException {try{
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append(
				"select A.bussTag,A.cat,A.rat,A.mat,A.pat,A.dat,A.tat,A.rectDate")
		.append(" from fc_AmountRecords A")
		.append(" where  A.bussTag in ("
							+ BussStateConstant.AMOUNTRECORDS_BUSSTAG_4
							+ ","
							+ BussStateConstant.AMOUNTRECORDS_BUSSTAG_5
							+ ","
							+ BussStateConstant.AMOUNTRECORDS_BUSSTAG_6
							+ ")");
		params = SqlUtil.getSafeWhereMap(params);
		String ids = params.getvalAsStr("ids");
		if(StringHandler.isValidStr(ids)){//
		sqlSb.append(" and A.id in ("+ids+") ");
		}
		long totalCount = getTotalCountBySql(sqlSb.toString());	//
		String sqlStr = sqlSb.toString();
		String colNames = "bussTag,cat,rat,mat,pat,dat,"
		+ "tat,rectDate#yyyy-MM-dd";
			DataTable dt = findBySqlPage(sqlStr, colNames, offset, pageSize,totalCount);
			return dt;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
}

	@Override
	public DataTable getNomalPlans(SHashMap<String, Object> params, int offset,
			int pageSize) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}
}