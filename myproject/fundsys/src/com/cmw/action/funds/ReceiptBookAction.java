package com.cmw.action.funds;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import com.alibaba.fastjson.JSONObject;
import com.cmw.constant.ResultMsg;
import com.cmw.constant.SysConstant;
import com.cmw.core.base.action.BaseAction;
import com.cmw.core.base.annotation.Description;
import com.cmw.core.base.exception.ServiceException;
import com.cmw.core.util.BeanUtil;
import com.cmw.core.util.CodeRule;
import com.cmw.core.util.DataTable;
import com.cmw.core.util.FastJsonUtil;
import com.cmw.core.util.FastJsonUtil.Callback;
import com.cmw.core.util.JsonUtil;
import com.cmw.core.util.SHashMap;
import com.cmw.core.util.StringHandler;
import com.cmw.entity.funds.ReceiptBookAttachmentEntity;
import com.cmw.entity.funds.ReceiptBookEntity;
import com.cmw.entity.funds.SettlementEntity;
import com.cmw.service.inter.funds.ReceiptBookService;


/**
 * 汇票转让承诺书表  ACTION类
 * @author 郑符明
 * @date 2014-02-20T00:00:00
 */
@Description(remark="汇票转让承诺书表ACTION",createDate="2014-02-20T00:00:00",author="郑符明",defaultVals="fuReceiptBook_")
@SuppressWarnings("serial")
public class ReceiptBookAction extends BaseAction {
	@Resource(name="receiptBookService")
	private ReceiptBookService receiptBookService;
	
	private String result = ResultMsg.GRID_NODATA;
	/**
	 * 获取 汇票转让承诺书表 列表
	 * @return
	 * @throws Exception
	 */
	public String list()throws Exception {
		try {
			String qCmns = "code,tamount#O,outDate,endDate,operational,name,rtacname,rtaccount";//根据条件进行查询
			SHashMap<String, Object> map = getQParams(qCmns);
			map.put(SysConstant.USER_KEY, this.getCurUser());
			DataTable dt = receiptBookService.getResultList(map,getStart(),getLimit());
			result = (null == dt || dt.getRowCount() == 0) ? ResultMsg.NODATA : dt.getJsonArr();
		} catch (ServiceException ex){
			result = ResultMsg.getFailureMsg(this,ex.getMessage());
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}catch (Exception ex){
			result = ResultMsg.getFailureMsg(this,ResultMsg.SYSTEM_ERROR);
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}
		outJsonString(result);
		return null;
	}
	
	/**
	 * 获取 汇票转让承诺书表 详情
	 * @return
	 * @throws Exception
	 */
	public String get()throws Exception {
		try {
			String id = getVal("id");
			if(!StringHandler.isValidStr(id)) throw new ServiceException(ServiceException.ID_IS_NULL);
			ReceiptBookEntity entity = receiptBookService.getEntity(Long.parseLong(id));
			result = FastJsonUtil.convertJsonToStr(entity,new Callback(){
				public void execute(JSONObject jsonObj) {
					
				}
			});
		} catch (ServiceException ex){
			result = ResultMsg.getFailureMsg(this,ex.getMessage());
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}catch (Exception ex){
			result = ResultMsg.getFailureMsg(this,ResultMsg.SYSTEM_ERROR);
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}
		outJsonString(result);
		return null;
	}
	
	/**
	 * 保存 汇票转让承诺书表 
	 * @return
	 * @throws Exception
	 */
	public String save()throws Exception {
		try {
			String id = getVal("id");
			String receiptBook = getVal("receiptBook");
			String attachmentList= getVal("attachmentList");//数组中含有数组，将[]去掉
			attachmentList=attachmentList.substring(1, attachmentList.length()-1);
			String settlement = getVal("settlement");
			//汇票承诺书集合
			List<ReceiptBookEntity> receiptBookEntities = FastJsonUtil.convertJsonToList(receiptBook,ReceiptBookEntity.class);
			//附件表集合
			List<ReceiptBookAttachmentEntity> receiptBookAttachmentEntities = FastJsonUtil.convertJsonToList(attachmentList, ReceiptBookAttachmentEntity.class);
			//汇票结算单集合
			List<SettlementEntity> settlementEntities = FastJsonUtil.convertJsonToList(settlement, SettlementEntity.class);
			Map<String, Object> complexData = new HashMap<String, Object>();
			complexData.put("receiptBookEntities", receiptBookEntities);
			complexData.put("receiptBookAttachmentEntities", receiptBookAttachmentEntities);
			complexData.put("settlementEntities", settlementEntities);
			if(null != id){
				complexData.put("id",Long.parseLong(id));
			}
			receiptBookService.doComplexBusss(complexData);
			result = ResultMsg.getSuccessMsg(this, ResultMsg.SAVE_SUCCESS);
		} catch (ServiceException ex){
			result = ResultMsg.getFailureMsg(this,ex.getMessage());
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}catch (Exception ex){
			result = ResultMsg.getFailureMsg(this,ResultMsg.SYSTEM_ERROR);
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}
		outJsonString(result);
		return null;
	}
	
	
	/**
	 * 新增  汇票转让承诺书表 
	 * @return
	 * @throws Exception
	 */
	public String add()throws Exception {
		try {
			Long num = receiptBookService.getMaxID();
			if(null == num) throw new ServiceException(ServiceException.OBJECT_MAXID_FAILURE);
			String code = CodeRule.getCode("R", num);
			result = JsonUtil.getJsonString("code",code);
		} catch (ServiceException ex){
			result = ResultMsg.getFailureMsg(this,ex.getMessage());
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}catch (Exception ex){
			result = ResultMsg.getFailureMsg(this,ResultMsg.SYSTEM_ERROR);
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}
		outJsonString(result);
		return null;
	}
	
	
	/**
	 * 删除  汇票转让承诺书表 
	 * @return
	 * @throws Exception
	 */
	public String delete()throws Exception {
		return enabled(ResultMsg.DELETE_SUCCESS);
	}
	
	/**
	 * 启用  汇票转让承诺书表 
	 * @return
	 * @throws Exception
	 */
	public String enabled()throws Exception {
		return enabled(ResultMsg.ENABLED_SUCCESS);
	}
	
	/**
	 * 禁用  汇票转让承诺书表 
	 * @return
	 * @throws Exception
	 */
	public String disabled()throws Exception {
		return enabled(ResultMsg.DISABLED_SUCCESS);
	}
	
	/**
	 * 删除/起用/禁用  汇票转让承诺书表 
	 * @return
	 * @throws Exception
	 */
	public String enabled(String sucessMsg)throws Exception {
		try {
			String ids = getVal("ids");
			Integer isenabled = getIVal("isenabled");
			receiptBookService.enabledEntitys(ids, isenabled);
			result = ResultMsg.getSuccessMsg(this,sucessMsg);
		} catch (ServiceException ex){
			result = ResultMsg.getFailureMsg(this,ex.getMessage());
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}catch (Exception ex){
			result = ResultMsg.getFailureMsg(this,ResultMsg.SYSTEM_ERROR);
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}
		outJsonString(result);
		return null;
	}
	
	/**
	 * 获取指定的 汇票转让承诺书表 上一个对象
	 * @return
	 * @throws Exception
	 */
	public String prev()throws Exception {
		try {
			SHashMap<String,Object> params = getQParams("id");
			ReceiptBookEntity entity = receiptBookService.navigationPrev(params);
			Map<String,Object> appendParams = new HashMap<String, Object>();
			if(null == entity){
				result = ResultMsg.getFirstMsg(appendParams);
			}else{
				result = ResultMsg.getSuccessMsg(entity, appendParams);
			}
		} catch (ServiceException ex){
			result = ResultMsg.getFailureMsg(this,ex.getMessage());
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}catch (Exception ex){
			result = ResultMsg.getFailureMsg(this,ResultMsg.SYSTEM_ERROR);
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}
		outJsonString(result);
		return null;
	}
	
	/**
	 * 获取指定的 汇票转让承诺书表 下一个对象
	 * @return
	 * @throws Exception
	 */
	public String next()throws Exception {
		try {
			SHashMap<String,Object> params = getQParams("id");
			ReceiptBookEntity entity = receiptBookService.navigationNext(params);
			/*------> 可通过  appendParams 加入附加参数<--------*/
			Map<String,Object> appendParams = new HashMap<String, Object>();
			if(null == entity){
				result = ResultMsg.getLastMsg(appendParams);
			}else{
				result = ResultMsg.getSuccessMsg(entity, appendParams);
			}
		} catch (ServiceException ex){
			result = ResultMsg.getFailureMsg(this,ex.getMessage());
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}catch (Exception ex){
			result = ResultMsg.getFailureMsg(this,ResultMsg.SYSTEM_ERROR);
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}
		outJsonString(result);
		return null;
	}
	
	
}
