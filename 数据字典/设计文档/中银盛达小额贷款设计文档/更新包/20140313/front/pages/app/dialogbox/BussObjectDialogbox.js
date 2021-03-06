/**
 * 业务对象选择弹出窗口	BussObjectDialogbox
 * 财务系统 --> 核算项配置 --> 业务对象关联 按钮 用到
 * @author 程明卫
 * @date 2013-04-07
 */
define(function(require, exports) {
	exports.DialogBox = {
		parentCfg : null,
		params : null,
		appWin : null,
		appTree : null,
		selvals : null,
		seltxts : null,
		showClearCheck : false,	/*当弹出业务表单树时，是否清除上一次选中的节点。 false : 不清除； true : 清除*/
		createAppWindow : function(){
			this.appTree = this.createAppTree();
			this.appWin = new Ext.ux.window.MyWindow({width:300,height:350,items:this.appTree});
		},
		createAppTree : function(){
			var _this = this;
			var apptree = new Ext.ux.tree.MyTree({fieldLabel : '业务对象列表',url:'./fsFinBussObject_list.action',params:this.params,width:400,height:350});
			apptree.addListener('render',function(tree){
				_this.addButtons(apptree);
				_this.appTree.reload(this.params);
			});
			
			return apptree;
		},
		addButtons : function(apptree){
			var _this = this;
			var btnOk = new Ext.Button({text:Btn_Cfgs.CONFIRM_BTN_TXT,iconCls:Btn_Cfgs.CONFIRM_CLS,handler:function(){
				var flag = true;
				if(apptree.isCheck){
					_this.selvals = apptree.getCheckIds();
					_this.seltxts = apptree.getCheckTexts();
				}else{
					_this.selvals = apptree.getSelId();
					_this.seltxts = apptree.getSelText();
				}
				if(!_this.selvals){
					ExtUtil.alert({msg:Msg_SysTip.msg_noSelNode});
					return;
				}
				_this.parentCfg.callback(_this.selvals,_this.seltxts);
				_this.appWin.hide();
			}});
			apptree.tbars.add(btnOk);
		},
		/**
		 * 显示弹出窗口
		 * @param _parentCfg 弹出之前传入的参数
		 */
		show : function(_parentCfg){
			if(_parentCfg) this.setParentCfg(_parentCfg);
			if(!this.appWin){
				this.createAppWindow();
			}
			this.selvals=null;
			this.seltxts=null;
			if(_parentCfg.parent){
				this.appWin.show(_parentCfg.parent);
			}else{
				this.appWin.show();
			}
			if(_parentCfg.showClearCheck){
				this.showClearCheck = _parentCfg.showClearCheck;
			}
			if(this.appTree.isCheck && this.showClearCheck){
				this.appTree.uncheckAll();
			}
		},
		setParentCfg : function(_parentCfg){
			this.parentCfg = _parentCfg;
			this.params = this.parentCfg.params;
		},
		/**
		 * 销毁组件
		 */
		destroy : function(){
			if(null != this.appWin){
				this.appWin.close();	//关闭并销毁窗口
				this.appWin = null;		//释放当前窗口对象引用
			}
		}
	};
});