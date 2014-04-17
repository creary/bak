/**
 * 
 */
package com.cmw.action.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmw.constant.ResultMsg;
import com.cmw.constant.SysConstant;
import com.cmw.core.base.action.BaseAction;
import com.cmw.core.base.annotation.Description;
import com.cmw.core.base.exception.ServiceException;
import com.cmw.core.kit.file.FileUtil;
import com.cmw.core.util.BeanUtil;
import com.cmw.core.util.CodeRule;
import com.cmw.core.util.DataTable;
import com.cmw.core.util.SHashMap;
import com.cmw.core.util.SqlUtil;
import com.cmw.core.util.StringHandler;
import com.cmw.core.util.TreeUtil;
import com.cmw.entity.sys.AccordionEntity;
import com.cmw.entity.sys.MenuEntity;
import com.cmw.entity.sys.ModuleEntity;
import com.cmw.entity.sys.RightEntity;
import com.cmw.entity.sys.UroleEntity;
import com.cmw.entity.sys.UserEntity;
import com.cmw.service.inter.sys.AccordionService;
import com.cmw.service.inter.sys.MenuService;
import com.cmw.service.inter.sys.ModuleService;
import com.cmw.service.inter.sys.RightService;
import com.cmw.service.inter.sys.UroleService;

/**
 * @author 程明卫 E-mail:chengmingwei_1984122@126.com
 * @version 创建时间：2010-6-15 下午12:14:31
 * 类说明 	菜单管理 ACTION
 */
@Description(remark="菜单 ACTION",createDate="2010-6-15",defaultVals="sysMenu_")
@SuppressWarnings("serial")
public class MenuAction extends BaseAction {
	/**
	 * 获取 session 中当前登录用户的 KEY
	 */
	public static final String USER_KEY = "user";
	//@Resource(name="menuService")
	@Autowired
	private MenuService menuService;
	@Resource(name="accordionService")
	private AccordionService accordionService;
	//@Resource(name="moduleService")
	@Autowired
	private ModuleService moduleService;
	@Autowired
	//@Resource(name="uroleService")
	private UroleService uroleService;
	@Autowired
	//@Resource(name="rightService")
	private RightService rightService;
	
	
	private String result = ResultMsg.GRID_NODATA;
	//树工具类
	private TreeUtil treeUtil = new TreeUtil();
	
	/**
	 * 获取菜单列表	
	 * @return
	 * @throws Exception
	 */
	public String list()throws Exception {
		try {
			//获取卡片项的ID，并将其作为 父ID
			SHashMap<String, Object> map = new SHashMap<String, Object>();
			String accordionId = getVal("accordionId");
			map.put("accordionId", accordionId);
			map.put("tabId", getVal("tabId"));
			UserEntity user = this.getCurUser();
			map.put(SysConstant.USER_KEY, user);
			Integer action = getIVal("action");
			map.put("action", action);
			DataTable dt = null;
			Integer isSystem = user.getIsSystem();
			if(null != isSystem && isSystem.intValue() == UserEntity.ISSYSTEM_1){
				dt = menuService.getResultList(map);
				result = getNavMenuList(map, accordionId, dt);
				outJsonString(result);
				return null;
			}
			/*---> 下面处理普通用户的情况 <---*/
			 List<ModuleEntity>	modules = null;
			if(null != action && action.intValue() == SysConstant.MENU_ACTION_NAV){
				Long userId = user.getUserId();
				String roleIds = getRoleIds(userId);
				if(!StringHandler.isValidStr(roleIds)){
					outJsonString(ResultMsg.NODATA);
					return null;
				} 
				
				String menuIds = getRightDatas(RightEntity.TYPE_1,roleIds);
				if(!StringHandler.isValidStr(menuIds)){
					outJsonString(ResultMsg.NODATA);
					return null;
				} 
				map.put("menuIds", menuIds);
				modules = getModRightDt(roleIds);
			}
			dt = menuService.getResultList(map);
			if(null == dt || dt.getSize() == 0){
				result = ResultMsg.NODATA;
			}else{
				addmods2Menu(dt,"id",modules);
				result = getNavMenuList(map, accordionId, dt);
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

	private String getNavMenuList(SHashMap<String, Object> map,
			String accordionId, DataTable dt) {
		treeUtil.setDt(dt);
		String pid = (String)map.get("pid");
		//如果没有提供 PID，就取所有菜单列表
		if(!StringHandler.isValidStr(pid)){	
			pid = SysConstant.MENU_ROOT_ID+"";
		}
		return treeUtil.getJsonArr("C"+accordionId);
	}
	
	/**
	 * 将模块添加到菜单上
	 * @param dt
	 * @param moduleDt
	 */
	private void addmods2Menu(DataTable dt,String pkIdName, List<ModuleEntity>	modules){
		if(null == modules || modules.size() == 0) return;
		int count = modules.size();
		for(int i=0,len=dt.getRowCount(); i<len; i++){
			Long id = dt.getLong(i, pkIdName);
			StringBuffer sb = new StringBuffer();
			for(int j=0; j<count; j++){
				ModuleEntity module = modules.get(j);
				Long menuId = module.getMenuId();
				if(!id.equals(menuId)) continue;
				String name = module.getName();
				sb.append(name+",");
			}
			if(null != sb && sb.length() > 0){
				dt.appendCmns("modDatas");
				dt.setCellData(i, "modDatas", StringHandler.RemoveStr(sb));
			}
		}
	}
	
	private List<ModuleEntity> getModRightDt(String roleIds) throws ServiceException{
		String  moduleIds = getRightDatas(RightEntity.TYPE_2,roleIds);
		if(!StringHandler.isValidStr(moduleIds)) return null;
		SHashMap<String, Object> map = new SHashMap<String, Object>();
		map.put("id", SqlUtil.LOGIC_IN + SqlUtil.LOGIC + moduleIds);
		List<ModuleEntity> modules = moduleService.getEntityList(map);
		return modules;
	}
	
	private String getRoleIds(Long userId) throws ServiceException{
		SHashMap<String, Object> map = new SHashMap<String, Object>();
		map.put("userId", userId);
		List<UroleEntity> uroles = uroleService.getEntityList(map);
		if(null == uroles || uroles.size() == 0) return null;
		StringBuffer sb = new StringBuffer();
		for(UroleEntity urole : uroles){
			sb.append(urole.getRoleId()+",");
		}
		return StringHandler.RemoveStr(sb);
	}
	
	/**
	 * 获取权限数据
	 * @param type	权限类型
	 * @param roleIds	角色ID列表
	 * @return	返回菜单和模块ID列表
	 * @throws ServiceException
	 */
	private String getRightDatas(Integer type,String roleIds) throws ServiceException{
		SHashMap<String, Object> map = new SHashMap<String, Object>();
		map.put("objtype", RightEntity.OBJTYPE_0);
		map.put("type", type);
		map.put("roleId", SqlUtil.LOGIC_IN + SqlUtil.LOGIC + roleIds);
		List<RightEntity> rights = rightService.getEntityList(map);
		if(null == rights || rights.size() == 0) return null;
		StringBuffer sb = new StringBuffer();
		for(RightEntity right : rights){
			sb.append(right.getMmId()+",");
		}
		return StringHandler.RemoveStr(sb);
	}
	
	/**
	 * 获取单个菜单数据	
	 * @return
	 * @throws Exception
	 */
	public String single()throws Exception {
		try {
			//获取卡片项的ID，并将其作为 父ID
			SHashMap<String, Object> map = new SHashMap<String, Object>();
			map.put("tabId", getVal("tabId"));
			map.put(SysConstant.USER_KEY, this.getCurUser());
			DataTable dt = menuService.getResultList(map);
			if(null == dt || dt.getRowCount() == 0){
				result = ResultMsg.NODATA;
			}else{
				result = dt.getJsonObjStr();
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
	 * 获取 系统菜单 列表
	 * @return
	 * @throws Exception
	 */
	public String sysmenus()throws Exception {
		try {
			SHashMap<String, Object> map = new SHashMap<String, Object>();
//			map.put(SysConstant.USER_KEY, this.getCurUser());
			UserEntity user = getCurUser();
			Integer isSystem = user.getIsSystem();
			List<ModuleEntity>	modules = null;
			if(null == isSystem || isSystem.intValue() == 0){/*非系统管理员帐号，要加权限过滤*/
				Long userId = user.getUserId();
				String roleIds = getRoleIds(userId);
				if(!StringHandler.isValidStr(roleIds)){
					outJsonString(ResultMsg.NODATA);
					return null;
				} 
				
				String menuIds = getRightDatas(RightEntity.TYPE_1,roleIds);
				if(!StringHandler.isValidStr(menuIds)){
					outJsonString(ResultMsg.NODATA);
					return null;
				} 
				map.put("menuId", SqlUtil.LOGIC_IN + SqlUtil.LOGIC + menuIds);
				modules = getModRightDt(roleIds);
			}
			map.put("isenabled", getIVal("isenabled"));
			String accordionId = getVal("accordionId");
			map.put("accordionId", SqlUtil.LOGIC_EQ+SqlUtil.LOGIC+accordionId);
			map.put(SqlUtil.ORDERBY_KEY,"desc:orderNo");
			DataTable dt = menuService.getResultList(map,getStart(),getLimit());
			addmods2Menu(dt,"menuId",modules);
			result = (null == dt || dt.getRowCount() == 0) ? ResultMsg.NODATA : dt.getJsonArr();
		} catch (ServiceException ex){
			result = ResultMsg.getFailureMsg(this,ex.getMessage());
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}
		outJsonString(result);
		return null;
	}
	
	/**
	 * 获取卡片菜单列表
	 * @return
	 * @throws Exception
	 */
	public String accordions()throws Exception {
		try {
			//获取卡片项的ID，并将其作为 父ID
			SHashMap<String, Object> map = new SHashMap<String, Object>();
			map.put("type", SysConstant.MENU_TYPE_CARD);
			String accordionId = getVal("accordionId");
			map.put("accordionId", accordionId);
			map.put(SysConstant.USER_KEY, this.getCurUser());
			DataTable dt = menuService.getResultList(map);
			treeUtil.setDt(dt);
			result = treeUtil.getJsonArrByNull();
		} catch (ServiceException ex){
			result = ResultMsg.getFailureMsg(this,ex.getMessage());
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}
		outJsonString(result);
		return null;
	}
	
	/**
	 * 保存菜单
	 * @return
	 * @throws Exception
	 */
	/**
	 * @return
	 * @throws Exception
	 */
	public String save()throws Exception {
		try {
			MenuEntity entity = BeanUtil.copyValue(MenuEntity.class,getRequest());
			menuService.saveOrUpdateEntity(entity);
			Map<String,Object> params = new HashMap<String, Object>();
			Integer type = entity.getType();
			if(type.intValue()== MenuEntity.MENU_TYPE_1 ){
				params.put("id", "m"+entity.getMenuId().toString());
			}else{
				params.put("id", "C"+entity.getMenuId().toString());
			}
			result = ResultMsg.getSuccessMsg(this,ResultMsg.SAVE_SUCCESS,params);
		} catch (ServiceException ex){
			result = ResultMsg.getFailureMsg(this,ex.getMessage());
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}
		outJsonString(result);
		return null;
	}
	
	/**
	 * 新增 菜单
	 * @return
	 * @throws Exception
	 */
	public String add()throws Exception {
		try {
			SHashMap<String,Object> attachParams = new SHashMap<String, Object>();
			String pid = getVal("pid");
			if(pid.indexOf("C") != -1){
				pid = pid.substring(1);
				AccordionEntity entity = accordionService.getEntity(Long.parseLong(pid));
				entity.setType(MenuEntity.MENU_TYPE_2);
				attachParams.put("accordionId", entity.getId());
				attachParams.put("parentName", entity.getName());
				attachParams.put("type", MenuEntity.MENU_TYPE_1);
				attachParams.put("pid", entity.getId());
			}else{
				if(pid.indexOf("m")!=-1){
					pid = pid.substring(1);
				}
				MenuEntity entity = menuService.getEntity(Long.parseLong(pid));
				entity.setType(MenuEntity.MENU_TYPE_2);
				attachParams = BeanUtil.copyToMap("accordionId,type,pid,parentName", "accordionId,type,menuId,name", entity);
			}
			String token = getVal("token");
			result = CodeRule.getCode(token,attachParams);
		} catch (Exception ex){
			result = ResultMsg.getFailureMsg(this,ex.getMessage());
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}
		outJsonString(result);
		return null;
	}
	/**
	 * 获取指定的菜单对象
	 * @return
	 * @throws Exception
	 */
	public String get()throws Exception {
		try {
			String menuId = getVal("menuId");
			if(menuId.indexOf("m") != -1){
				menuId = menuId.substring(1);
			}
			MenuEntity entity = menuService.getEntity(Long.parseLong(menuId));
			Long pid = entity.getPid();
			Map<String,Object> appendParams = new HashMap<String, Object>();
			String parentName = null;
			if(entity.getType().intValue() == MenuEntity.MENU_TYPE_1){
				AccordionEntity parentEntity = accordionService.getEntity(pid);
				parentName = parentEntity.getName();
			}else{
				MenuEntity parentEntity = menuService.getEntity(pid);
				parentName = parentEntity.getName();
			}
			appendParams.put("parentName", parentName);
			result = ResultMsg.getSuccessMsg(entity, appendParams);
		} catch (ServiceException ex){
			result = ResultMsg.getFailureMsg(this,ex.getMessage());
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}
		outJsonString(result);
		return null;
	}
	
	/**
	 * 获取指定的菜单上一个对象
	 * @return
	 * @throws Exception
	 */
	public String prev()throws Exception {
		try {
			SHashMap<String,Object> params = new SHashMap<String, Object>();
			params.put("id", getVal("menuId"));
			MenuEntity entity = menuService.navigationPrev(params);
			Map<String,Object> appendParams = new HashMap<String, Object>();
			if(null == entity){
				result = ResultMsg.getFirstMsg(appendParams);
			}else{
				Long pid = entity.getPid();
				String parentName = null;
				if(entity.getType().intValue() == MenuEntity.MENU_TYPE_1){
					AccordionEntity parentEntity = accordionService.getEntity(pid);
					if(null != parentEntity) parentName = parentEntity.getName();
				}else{
					MenuEntity parentEntity = menuService.getEntity(pid);
					if(null != parentEntity) parentName = parentEntity.getName();
				}
				if(null == parentName) parentName = "";
				appendParams.put("parentName", parentName);
				result = ResultMsg.getSuccessMsg(entity, appendParams);
			}
		} catch (ServiceException ex){
			result = ResultMsg.getFailureMsg(this,ex.getMessage());
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}
		outJsonString(result);
		return null;
	}
	
	/**
	 * 获取指定的菜单下一个对象
	 * @return
	 * @throws Exception
	 */
	public String next()throws Exception {
		try {
			SHashMap<String,Object> params = new SHashMap<String, Object>();
			params.put("id", getVal("menuId"));
			MenuEntity entity = menuService.navigationNext(params);
			Map<String,Object> appendParams = new HashMap<String, Object>();
			if(null == entity)
				result = ResultMsg.getLastMsg(appendParams);
			else{
				Long pid = entity.getPid();
				String parentName = null;
				if(entity.getType().intValue() == MenuEntity.MENU_TYPE_1){
					AccordionEntity parentEntity = accordionService.getEntity(pid);
					if(null != parentEntity) parentName = parentEntity.getName();
				}else{
					MenuEntity parentEntity = menuService.getEntity(pid);
					if(null != parentEntity) parentName = parentEntity.getName();
				}
				appendParams.put("parentName", parentName);
				result = ResultMsg.getSuccessMsg(entity, appendParams);
			}
		} catch (ServiceException ex){
			result = ResultMsg.getFailureMsg(this,ex.getMessage());
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}
		outJsonString(result);
		return null;
	}
	
	/**
	 * 删除菜单 
	 * @return
	 * @throws Exception
	 */
	public String delete()throws Exception {
		return enabled();
	}
	
	/**
	 * 禁用菜单
	 * @return
	 * @throws Exception
	 */
	public String disabled()throws Exception {
		return enabled();
	}
	
	/**
	 * 起用菜单
	 * @return
	 * @throws Exception
	 */
	public String enabled()throws Exception {
		try {
			String ids = getVal("ids");
			if(ids.indexOf("m")!=-1){
				ids = ids.substring(1);
			}
			Integer isenabled = getIVal("isenabled");
			menuService.enabledEntitys(ids, isenabled);
			result = ResultMsg.getSuccessMsg(this,ResultMsg.SAVE_SUCCESS);
		} catch (ServiceException ex){
			result = ResultMsg.getFailureMsg(this,ex.getMessage());
			if(null == result) result = ex.getMessage();
			ex.printStackTrace();
		}
		outJsonString(result);
		return null;
	}
}