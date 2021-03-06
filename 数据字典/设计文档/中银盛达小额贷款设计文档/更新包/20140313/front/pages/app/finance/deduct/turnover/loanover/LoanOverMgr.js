Ext.namespace("cmw.skythink");
/**
 *贷款发放 
 *@author liting
 * 
 */
cmw.skythink.LoanOverMgr = function() {
	this.init(arguments[0]);
}

Ext.extend(cmw.skythink.LoanOverMgr, Ext.util.MyObservable, {
	initModule : function(tab, params) {
		this.module = new Cmw.app.widget.AbsGContainerView({
					tab : tab,
					params : params,
					hasTopSys : false,
					getAppGrid:{},
					getQueryFrm : this.getQueryFrm,
					getToolBar : this.getToolBar,
					getAppGrid : this.getAppGrid,
					globalMgr : this.globalMgr,
					myJson:null,
					refresh : this.refresh
				});
	},
	
	/**
	 * 获取查询Form 表单
	 */
	getQueryFrm : function(){
		var _this =this;
		var txt_name = FormUtil.getTxtField({fieldLabel : '客户姓名',name:'name',width : 150});
		var cbo_cardType = FormUtil.getRCboField({
		    fieldLabel: '证件类型',
		    name: 'cardType',
		    "width": 150,
		    allDispTxt : Lcbo_dataSource.allDispTxt,
		    register : REGISTER.GvlistDatas,
		    restypeId : '100002'
		});
		
		var txt_cardNum = FormUtil.getTxtField({
		    fieldLabel: '证件号码',
		    name : 'cardNum',
		    width : 150
		});
		
		var txt_payName = FormUtil.getTxtField({
		    fieldLabel: '收款人名称',
		    name: 'payName',
		    width: 150
		});
	
		var txt_regBank = FormUtil.getTxtField({
		    fieldLabel: '开户行',
		    name: 'regBank',
		     width: 150
		});
		
		var txt_account = FormUtil.getTxtField({
		    fieldLabel: '收款帐号',
		    name: 'account',
		     width: 150
		});	
	
		
		var cbo_eqopAmount = FormUtil.getEqOpLCbox({name:'eqopAmount'});
		var txt_payAmount = FormUtil.getMoneyField({
		    fieldLabel: '放款金额',
		    name:'payAmount',
		    width:70
		});
		
		var comp_payAmount = FormUtil.getMyCompositeField({
			 fieldLabel: '放款金额',width:150,sigins:null,
			 itemNames:"eqopAmount,payAmount",
			 name:'comp_payAmount',
			 items : [cbo_eqopAmount,txt_payAmount]
		});
		
		var txt_startDate = FormUtil.getDateField({name:'startDate',width:90/*,isdeaulft:true*/});
		var dt = new Date().add(Date.DAY, 6);
		var  txt_endDate = FormUtil.getDateField({name:'endDate',width:90/*,value : dt*/});
		var comp_payDate = FormUtil.getMyCompositeField({
			itemNames : 'startDate,endDate',
			sigins : null,
			 fieldLabel: '合约放款日',width:200,sigins:null,
			 name:'comp_payDate',
			 items : [txt_startDate,txt_endDate]
		});
		
		var layout_fields = [{cmns:'THREE',fields:[txt_name,cbo_cardType,txt_cardNum,txt_payName,txt_regBank,txt_account,comp_payAmount,comp_payDate]}]

		var queryFrm = FormUtil.createLayoutFrm(null,layout_fields);
		
		return queryFrm;
	},

	/**
	 * 查询工具栏
	 */
	getToolBar : function() {
		var _this = this;
		var toolBar = null;
		var barItems = [{/* 查询 */
			token : '查询',
			text : Btn_Cfgs.QUERY_BTN_TXT,
			iconCls : 'page_query',
			tooltip : Btn_Cfgs.QUERY_TIP_BTN_TXT,
			handler : function() {
				_this.globalMgr.query(_this);
			}
		}, {
			/* 重置 */
			token : '重置',
			text : Btn_Cfgs.RESET_BTN_TXT,
			iconCls : 'page_reset',
			tooltip : Btn_Cfgs.RESET_TIP_BTN_TXT,
			name : 'a',
			handler : function() {
				_this.queryFrm.reset();
			}
		},{type:"sp"},{/*打印放款单*/
			token : '打印放款单',
			text : Btn_Cfgs.LOANINVOCE_DATEI_BTN_TXT,
			iconCls:Btn_Cfgs.PRINT_CLS,
			tooltip:Btn_Cfgs.LOANINVOCE_DATEI_TIP_BTN_TXT,
			handler : function(){
				_this.globalMgr.showDetailWindow({self:_this,key:'打印放款单',menuId:_this.params.nodeId});
			}
		}, {
			token : '详情',
			text : Btn_Cfgs.MENU_details_BTN_TXT,
			iconCls : Btn_Cfgs.MENU_details_BTN_CLS,
			tooltip : Btn_Cfgs.MENU_details_TIP_BTN_TXT,
			handler : function() {
				_this.globalMgr.winEdit.show({self : _this,key : '详情'});
			}
		}
//		,
//
//		{
//			token : '可逆',
//			text : Btn_Cfgs.MENU_REVER_BTN_TXT,
//			iconCls : Btn_Cfgs.MENU_REVER_BTN_CLS,
//			tooltip : Btn_Cfgs.MENU_REVER_TIP_BTN_TXT,
//			handler : function() {
//				 _this.globalMgr.winEdit.reserv({
//				 self : _this
//				 });
//			}
//		}
		,{/*导出*/
			token : '导出',
			text : Btn_Cfgs.EXPORT_BTN_TXT,
			iconCls:Btn_Cfgs.EXPORT_CLS,
			tooltip:Btn_Cfgs.EXPORT_TIP_BTN_TXT,
			handler : function(){
				_this.globalMgr.doExport(_this);
			}
		}];
		toolBar = new Ext.ux.toolbar.MyCmwToolbar({
					aligin : 'right',
					controls : barItems,
					rightData : {
						saveRights : true,
						currNode : this.params[CURR_NODE_KEY]
					}
				});
		return toolBar;
	},
	/**
	 * 获取Grid 对象
	 */
	getAppGrid : function() {
		var _this = this;

		var structure_1 = [{
			    header: '放款单编号',
			    name: 'code',
			    width : 135
			},
			{
			    header: '合同编号',
			    name: 'ccode',
			     width : 135
			},
			{
			    header: '客户姓名',
			    name: 'name',
			    width:65
			},
			{
			    header: '证件类型',
			    name: 'cardType',
			    width: 65,
				 renderer : function(val){
		    		return Render_dataSource.cardTypeRender(val);
		    	}
			},
			{
			    header: '证件号码',
			    name: 'cardNum',
			    width:135
			},
			{
			    header: '贷款金额',
			    name: 'appAmount',
			     width : 135,
				 renderer: function(val) {
			       return (val && val>0) ? Cmw.getThousandths(val)+'元' : '';
			    }
			},
			{
				header: '管理费率(%)',
		    	width : 90,
		    	name: 'mrate',
		    	renderer : function(val,m){
		    		if(val || val==0) val += '%';
		    		return val ;
		    	}
		    	
			},
			{
			    header: '放款金额',
			    name: 'payAmount',
			    width:135,
				renderer: function(val,m) {
					m.css='x-grid-back-qs'; 
			       return (val && val>0) ? Cmw.getThousandths(val)+'元' : '';
			    }
			},{
				header: '贷款利率(%)',
		    	width : 90,
		    	name: 'rate',
		    	renderer : function(val,m){
		    		m.css='x-grid-back-gree'; 
		    		if(val || val==0) val += '%';
		    		return val ;
		    	}
			},
			{header: '内部利率类型',  name: 'inRateType',width: 100,
			 renderer: function(val,m) {
			 	m.css='x-grid-back-red'; 
		       return Render_dataSource.rateTypeRender(val);
		    }},
			{header: '公司内部利率',  name: 'inRate' ,width: 90,
			 	renderer : function(val,m){
		    		m.css='x-grid-back-red'; 
		    		if(val || val==0) val += '%';
		    		return val ;
		    	}
		    },
			 {header: '贷款期限(年)',  name: 'yearLoan',hidden: true ,hideable : true},
		    {header: '贷款期限(月)',  name: 'monthLoan',hidden: true ,hideable : true},
		    {header: '贷款期限(日)',  name: 'dayLoan',hidden: true ,hideable : true},
			{header: '贷款期限',  name: 'loanLimit' ,width: 65,
			 renderer: function(val,metaData,record) {
			 	var loanLimit = Render_dataSource.loanLimitRender(record.data);
		       return loanLimit;
		    }},
			{
			    header: '收款人名称',
			    name: 'payName',
			    width:100
			},
			{
			    header: '开户行',
			    name: 'regBank',
			    width:100
			},
			{
			    header: '收款帐号',
			    name: 'account',
			    width:135
			},
			{
			    header: '放款日期',
			    name: 'payDate',
			    width:100
			}];
		
			
			var continentGroupRow = [{header: '合同信息', colspan: 12, align: 'center'},
				{header: '收款帐号信息', colspan: 3, align: 'center'},{header: '放款信息', colspan: 5, align: 'center'}];
			 var group = new Ext.ux.grid.ColumnHeaderGroup({
		        rows: [continentGroupRow]
		    });
		var appgrid_1 = new Ext.ux.grid.AppGrid({
					tbar : _this.toolBar,
					structure : structure_1,
					url : './fcLoanInvoceQuery_list.action',
					needPage : true,
					keyField : 'id',
					selectType : "check",
					plugins : group,
					isLoad : false,
					gatherCfg : {
						gatherOffset : "code",
						gatherCmns : [{
									cmn : 'appAmount',
									dp : 2
								}, {
								cmn : 'payAmount',
								dp : 2
								}]
					},
					isLoad : false,
					listeners : {
						render : function(grid) {
							_this.globalMgr.query(_this);
						},
						rowdblclick : function() {
							//鼠标双击事件
							var selRow = appgrid_1 .getSelRow();//获取对象所在的行
							if(selRow!=null){_this.globalMgr.winEdit.show({self : _this,key : '详情'});}
						}
					}
				});
		return appgrid_1;
	},
	refresh : function(optionType, data) {
		this.globalMgr.query(this);
		this.globalMgr.activeKey = null;
	},
	globalMgr : {
		/**
		 * 当前激活的按钮文本
		 * @type
		 */
		sysId : this.params.sysid,
		payName:this.params.payName,
		activeKey : null,
		getQparams : function(_this) {
			var params = _this.queryFrm.getValues() || {};
			params.custType = 0;
			params.auditState = '2';/* 已审批通过 */
			params.state = '1';/* 未放款 */
			/*-- 附加桌面传递的参数  CODE START --*/
			if (_this.params && _this.params[DESK_MOD_TAG_QUERYPARAMS_KEY]) {
				var deskParams = _this.params[DESK_MOD_TAG_QUERYPARAMS_KEY];
				if (deskParams) {
					Ext.applyIf(params, deskParams);
					_this.params[DESK_MOD_TAG_QUERYPARAMS_KEY] = null;
				}
			}/*-- 附加桌面传递的参数  CODE END --*/
			return params;
		},
		 showDetailWindow:function(parentCfg){
		 		var _this = parentCfg.self;
				var winkey=parentCfg.key;
				var parent = _this.appgrid;
				parentCfg.parent = parent;
				var sysId=_this.params.sysid;
				var winModule=null;
		 			winModule = "LoanPrintDetail";
					if(!parent.getSelId())return;
					if(_this.appCmpts[winkey]){
						_this.appCmpts[winkey].show(parentCfg);
					}else{ 
						EventManager.get("./fcFuntempCfg_getTidByMid.action",{params:{menuId:parentCfg.menuId},sfn : function(jsonData){
							parentCfg.tempId=jsonData.list[0].tempId;
							Cmw.importPackage('pages/app/finance/deduct/loan/'+winModule,function(module) {
							 	_this.appCmpts[winkey] = module.WinEdit;
							 	_this.appCmpts[winkey].show(parentCfg);
					  		});
						}});
					}
				},
		winEdit : {
			show : function(parentCfg) {
				var _this = parentCfg.self;
				var key=parentCfg.key;
				var parent = _this.appgrid;
				var sysId=_this.params.sysid;
				var payName=_this.params.payName;
				parentCfg.parent=parent;
				parentCfg.sysId=sysId;
				parentCfg.payName=payName;
					if(_this.appCmpts[key]){
						if(!parent.getSelId())return;
						_this.appCmpts[key].show(parentCfg);
					}else{ 
						if(!parent.getSelId())return;
				Cmw.importPackage('pages/app/finance/deduct/turnover/loanover/LoanOverDetail',function(module) {
				 	_this.appCmpts[key] = module.WinEdit;
				 	_this.appCmpts[key].show(parentCfg);
						  		});
							}
					}
		},
		
		/**
		 * 查询方法
		 * 
		 * @param {}
		 *            _this
		 */
		query : function(_this) {
			var params = this.getQparams(_this);
			EventManager.query(_this.appgrid, params);
		},
		/**
		 * 导出Excel
		 * @param {} _this
		 */
		doExport : function(_this){
			var params = this.getQparams(_this);
			params.excelType = 2;
			var token = _this.params.nodeId;
			EventManager.doExport(token,params);
		}
	}
});
