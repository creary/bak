/**
 * 九洲科技公司产品 命名空间 联系电话：18064179050 程明卫[系统设计师] 联系电话：18026386909 陈瑾[销售总经理]
 */
Ext.namespace("cmw.skythink");
/**
 * 暂存的息费豁免申请单 UI smartplatform_auto 2013-09-21 12:20:16
 */ 
cmw.skythink.TempExemptApplyMgr = function(){
	this.init(arguments[0]);
}

Ext.extend(cmw.skythink.TempExemptApplyMgr,Ext.util.MyObservable,{
	initModule : function(tab,params){
		this.module = new Cmw.app.widget.AbsGContainerView({
			id : ComptIdMgr.tempExemptApplyMgrId,
			tab : tab,
			params : params,
			hasTopSys : false,
			getQueryFrm : this.getQueryFrm,
			getToolBar : this.getToolBar,
			getAppGrid : this.getAppGrid,
			globalMgr : this.globalMgr,
			refresh : this.refresh
		});
	},
	
	/**
	 * 获取查询Form 表单
	 */
	getQueryFrm : function(){
		var self =this;
	
		var cbo_custType = FormUtil.getLCboField({
		    fieldLabel: '客户类型',
		    name: 'custType',
		    width : 150,
		    data : Lcbo_dataSource.getAllDs(Lcbo_dataSource.custType_datas)
		});
		
		var txt_custName = FormUtil.getTxtField({fieldLabel : '客户姓名',name:'custName',width : 150});
		
		var cbo_etype = FormUtil.getLCboField({
		    fieldLabel: '豁免类别',
		    name: 'etype',
		    width : 150,
		    data : Lcbo_dataSource.getAllDs(Lcbo_dataSource.exempt_etype_datas)
		});
		
		var rad_isBackAmount = FormUtil.getRadioGroup({fieldLabel : '是否返还息费', name:'isBackAmount', items : [
			{boxLabel : '全部', name:'isBackAmount',inputValue:-1},
			{boxLabel : '是', name:'isBackAmount',inputValue:1},
			{boxLabel : '否', name:'isBackAmount',inputValue:2}
			]});

		var cbo_eqAmount = FormUtil.getEqOpLCbox({name:'eqAmount'});
		var txt_totalAmount = FormUtil.getMoneyField({
		    fieldLabel: '豁免金额',
		    name:'totalAmount',
		    width:70
		});
		var comp_totalAmount = FormUtil.getMyCompositeField({
			 fieldLabel: '豁免金额',width:150,sigins:null,itemNames:'eqAmount,totalAmount',
			 name:'comp_appAmount',
			 items : [cbo_eqAmount,txt_totalAmount]
		});
		
		var txt_startDate1 = FormUtil.getDateField({name:'startDate1',width:90});
		var txt_endDate1 = FormUtil.getDateField({name:'endDate1',width:90});
		var comp_appDate = FormUtil.getMyCompositeField({
			itemNames : 'startDate1,endDate1',
			sigins : null,
			 fieldLabel: '申请日期',width:210,sigins:null,
			 name:'comp_estartDate',
			 items : [txt_startDate1,{xtype:'displayfield',value:'至'},txt_endDate1]
		});
		
		var layout_fields = [{cmns:'THREE',fields:[cbo_custType,txt_custName,cbo_etype,rad_isBackAmount,comp_totalAmount,comp_appDate]}]
		var queryFrm = FormUtil.createLayoutFrm(null,layout_fields);
		return queryFrm;
	},
	
	/**
	 * 查询工具栏
	 */
	getToolBar : function(){
		var _this = this;
		var toolBar = null;
		var barItems = [{
			token : '查询',
			text : Btn_Cfgs.QUERY_BTN_TXT,
			iconCls:'page_query',
			tooltip:Btn_Cfgs.QUERY_TIP_BTN_TXT,
			handler : function(){
				_this.globalMgr.query(_this);
			}
		},{
			token : '重置',
			text : Btn_Cfgs.RESET_BTN_TXT,
			iconCls:'page_reset',
			tooltip:Btn_Cfgs.RESET_TIP_BTN_TXT,
			handler : function(){
				_this.queryFrm.reset();
			}
		},{type:"sp"},{/*添加*/
			token : '添加',
			text : Btn_Cfgs.INSERT_BTN_TXT,
			iconCls:Btn_Cfgs.INSERT_CLS,
			tooltip:Btn_Cfgs.INSERT_TIP_BTN_TXT,
			handler : function(){
				_this.globalMgr.doApplyByOp(_this,OPTION_TYPE.ADD);
			}
		},{/*编辑*/
			token : '编辑',
			text : Btn_Cfgs.MODIFY_BTN_TXT,
			iconCls:Btn_Cfgs.MODIFY_CLS,
			tooltip:Btn_Cfgs.MODIFY_TIP_BTN_TXT,
			handler : function(){
				_this.globalMgr.doApplyByOp(_this,OPTION_TYPE.EDIT);
			}
		}/*,{
			token : '详情',
			text : Btn_Cfgs.DETAIL_BTN_TXT,
			iconCls:Btn_Cfgs.DETAIL_CLS,
			tooltip:Btn_Cfgs.DETAIL_TIP_BTN_TXT,
			handler : function(){
				_this.globalMgr.detailApplyForm(_this);
			}
		}*/,{type:"sp"},{/*提交*/
			token : '提交',
			text : Btn_Cfgs.SUBMIT_BTN_TXT,
			iconCls:Btn_Cfgs.SUBMIT_CLS,
			tooltip:Btn_Cfgs.SUBMIT_TIP_BTN_TXT,
			handler : function(){
				_this.globalMgr.submitApplyForm(_this);
			}
		},{
			token : '删除',
			text : Btn_Cfgs.DELETE_BTN_TXT,
			iconCls:'page_delete',
			tooltip:Btn_Cfgs.DELETE_TIP_BTN_TXT,
			handler : function(){
				EventManager.deleteData('./fcExempt_delete.action',{type:'grid',cmpt:_this.appgrid,optionType:OPTION_TYPE.DEL,self:_this});
			}
		}/*,{
			token : '导出',
			text : Btn_Cfgs.EXPORT_BTN_TXT,
			iconCls:Btn_Cfgs.EXPORT_CLS,
			tooltip:Btn_Cfgs.EXPORT_TIP_BTN_TXT,
			handler : function(){
				_this.globalMgr.doExport(_this);
			}
		}*/];
		toolBar = new Ext.ux.toolbar.MyCmwToolbar({aligin:'right',controls:barItems,rightData : {saveRights : true,currNode : this.params[CURR_NODE_KEY]}});
		return toolBar;
	},
	/**
	 * 获取Grid 对象
	 */
	getAppGrid : function(){
		var _this = this;
			var structure_1 = [{
			    header: '借款合同Id',
			    name: 'contractId',
			    hidden: true
			},
			
			{
			    header: '息费豁免申请单编号',
			    name: 'code',
			    width: 135
			},
			{
			    header: '客户类型',
			    name: 'custType',
			    width: 65,
			    renderer: function(val) {
			     return	Render_dataSource.custTypeRender(val);
			    }
			},
			{
			    header: '客户名称',
			    name: 'custName'
			},
			{
			    header: '证件类型',
			    name: 'cardType',
			     width: 65
			},
			{
			    header: '证件号码',
			    name: 'cardNum',
			     width: 145
			},
			{
			    header: '借款合同号',
			    name: 'loanCode',
			    width: 135
			},
			{
			    header: '豁免类别',
			    name: 'etype',
			    width: 135,
			    renderer: Render_dataSource.exempt_etypeRender
			},
			{
			    header: '豁免项目',
			    name: 'exeItems',
			    width: 145,
			    renderer: Render_dataSource.exempt_exeItemsRender
			},
			{
			    header: '是否返还息费',
			    name: 'isBackAmount',
			    width:75,
			     renderer: Render_dataSource.exempt_isBackAmountRender
			},
			{
			    header: '豁免起始日期',
			    name: 'startDate',
			    hidden : true,
			    hideable : true
			},
			{
			    header: '豁免起止日期',
			    name: 'endDate',
			    width: 160,
			    renderer: function(val,metadata ,record) {
			    if(!val) return '';
			     var startDate = record.get("startDate");
			     return startDate+"&nbsp;至&nbsp;"+val;
			    }
			},
			{
			    header: '豁免息费合计',
			    name: 'totalAmount',
			    width:85,
			    renderer: function(val) {
			     return Cmw.getThousandths(val)+"元";
			    }
			},{
			    header: '经办人',
			     width:135,
			    name: 'managerName'
			},
			{
			    header: '申请日期',
			     width:75,
			    name: 'appDate'
			}];
			
			var appgrid_1 = new Ext.ux.grid.AppGrid({
				tbar :this.toolBar,
			    structure: structure_1,
			    url: './fcExempt_list.action',
			    needPage: true,
			    keyField: 'id',
			    width:600,
			    isLoad: false,
			    listeners : {
				   	render : function(grid){
				   	 	_this.globalMgr.query(_this);
				   	}
			    }
			});
		return appgrid_1;
	},
	refresh:function(optionType,data){
		var activeKey = this.globalMgr.activeKey;
		if(!this.appgrid) return;
		if(this.appgrid && this.appgrid.rendered){
			this.globalMgr.query(this);
			this.globalMgr.activeKey = null;
		}else{
			var _this = this;
			this.appgrid.addListener('render',function(){_this.globalMgr.query(_this);});
		}
	},
	globalMgr : {
		getQparams : function(_this){
			var params = _this.queryFrm.getValues() || {};
			/*-- 附加桌面传递的参数  CODE START --*/
			if(_this.params && _this.params[DESK_MOD_TAG_QUERYPARAMS_KEY]){
			 	var deskParams = _this.params[DESK_MOD_TAG_QUERYPARAMS_KEY];
			 	if(deskParams){
			 		Ext.applyIf(params,deskParams);
			 		 _this.params[DESK_MOD_TAG_QUERYPARAMS_KEY] = null;
			 	}
			}/*-- 附加桌面传递的参数  CODE END --*/
			Ext.apply(params,{status:0});/*未提交*/
			return params;
		},
		/**
		 * 查询方法
		 * @param {} _this
		 */
		query : function(_this){
			var params = this.getQparams(_this);
			EventManager.query(_this.appgrid,params);
		},
		/**
		 * 导出Excel
		 * @param {} _this
		 */
		doExport : function(_this){
			var params = this.getQparams(_this);
			var token = _this.params.nodeId;
			EventManager.doExport(token,params);
		},
		/**
		 * 添加/编辑申请单	
		 * @param {} _this
		 */
		doApplyByOp : function(_this,op){
			var params = {applyId:null,optionType:op};
			if(op == OPTION_TYPE.EDIT){//编辑 时传入申请单ID，客户ID，客户类型
				var applyId = _this.appgrid.getSelId();
				if(!applyId) return;
				var data = _this.appgrid.getCmnVals(["contractId"]);
				data["applyId"] = applyId;
				params = Ext.apply(params,data);
			}
			var tabId = CUSTTAB_ID.exemptApplyTabId;
			var apptabtreewinId = _this.params["apptabtreewinId"];
			Cmw.activeTab(apptabtreewinId,tabId,params);
		},
		/**
		 * 提交申请单	
		 * @param {} _this
		 */
		submitApplyForm : function(_this){
			var applyId = _this.appgrid.getSelId();
			var sysId = _this.params.sysid;
			if(!applyId) return;
			var sysId = _this.params.sysid;
			var codeObj = _this.appgrid.getCmnVals("code,contractId");
			var contractId = codeObj.contractId;
			 ExtUtil.confirm({title:'提示',msg:'确定提交编号为："'+codeObj.code+'"的息费豁免申请单?',fn:function(){
		 	  		if(arguments && arguments[0] != 'yes') return;
		 	  		var contractId = _this.appgrid.getCmnVals(["contractId"]).contractId;
		 	  		var params = {sysId:sysId,applyId:applyId,contractId:contractId,procId:null,bussProccCode:'B0003'};
					var params = {sysId:sysId,applyId:applyId,contractId:contractId,procId:null,bussProccCode:'B0003'};
					var tabId = CUSTTAB_ID.bussProcc_auditMainUITab.id;
					var url = CUSTTAB_ID.bussProcc_auditMainUITab.url;
					var title =  '息费豁免业务审批';
					var apptabtreewinId = _this.params["apptabtreewinId"];
					Cmw.activeTab(apptabtreewinId,tabId,params,url,title);
			 }});
		},
		/**
		 * 申请单详情
		 * @type 
		 */
		detailApplyForm : function(_this){
//			var applyId = _this.appgrid.getSelId();
//			if(!applyId) {
//				return;
//			}
//			var data = _this.appgrid.getCmnVals(["contractId"]);
//			data["applyId"] = applyId;
//			params = data;
//			params.dispaly  = false;
//			var tabId = CUSTTAB_ID.detailApplyFormTabId.id;
//			var url =  CUSTTAB_ID.detailApplyFormTabId.url;
//			var title =  '个人客户申请单详情';
//			var apptabtreewinId = _this.params["apptabtreewinId"];
//			Cmw.activeTab(apptabtreewinId,tabId,params,url,title);
		},
		/**
		 * 当前激活的按钮文本	
		 * @type 
		 */
		activeKey : null
	}
});

