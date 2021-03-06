/*===============================  币别插入时，相关的 SQL START CODE  =======================================*/
select FUserID from t_Group where FUserID = 16394 and FGroupID = 1

select * from t_DataTypeInfo

Select FAccessType,FAccessColName,FAccessTable,FUsed  from t_AccessRowData where FType=3 and FClassID=120

exec sp_executesql N
'INSERT INTO t_Currency (FNumber,FName,FOperator,FExchangeRate,FFixRate,FScale) 
VALUES 
(@P1,@P2,@P3,@P4,@P5,@P6)',N'@P1 varchar(3),@P2 varchar(40),@P3 char(1),@P4 float,@P5 smallint,@P6 
smallint','GBP','英鎊','*',1,0,2

 Select * from t_DataTypeRight where FType=3  and FClassID=120
 
 Select * from t_BaseProperty Where FTypeID = 1 And FItemID = 1001
 /*===============================  币别插入时，相关的 SQL START END  =======================================*/
 
/*--------------------------------------------------------------------------------------------------------------*/ 
 /*===============================  客户数据插入时，相关的 SQL START CODE  =======================================*/
 exec sp_executesql N'INSERT INTO t_Item 
 (FItemClassID,FParentID,FLevel,FName,FNumber,FShortNumber,FFullNumber,FDetail,UUID,FDeleted) 
 VALUES 
 (@P1,@P2,@P3,@P4,@P5,@P6,@P7,@P8,@P9,@P10)',N'@P1 int,@P2 int,@P3 smallint,@P4 
varchar(80),@P5 varchar(80),@P6 varchar(80),@P7 varchar(80),@P8 bit,@P9 varchar(38),@P10 smallint',
1,0,1,'刘云山','C10008','C10008','C10008',1,'A919B115-B93B-48BE-A904-A5741B51A0E7',0

exec sp_executesql N'SELECT FItemID FROM t_Item WHERE FItemClassID=@P1 AND FNumber=@P2',N'@P1 int,@P2 varchar(80)',1,'C10008'

exec sp_executesql N'INSERT INTO t_Organization 
(FHelpCode,FShortName,FAddress,FStatus,FRegionID,FTrade,FContact,FPhone,FMobilePhone,FFax,FPostalCode,FEmail,FBank,FAccount,FTaxNum,FIsCreditMgr,FSaleMode,FValueAddRate,FCity,FProvince,FCountry,FHomePage,Fcorperate,FCarryingAOS,FTypeID,FSaleID,FStockIDKeep,FCoSupplierID,FCyID,FSetID,FARAccountID,FPreAcctID,FOtherARAcctID,FPayTaxAcctID,FAPAccountID,FPreAPAcctID,FOtherAPAcctID,FfavorPolicy,Fdepartment,Femployee,FlastTradeAmount,FlastRPAmount,FmaxDealAmount,FminForeReceiveRate,FminReserverate,FdebtLevel,FPayCondition,FShortNumber,FNumber,FName,FParentID,FItemID) 
VALUES 
(@P1,@P2,@P3,@P4,@P5,@P6,@P7,@P8,@P9,@P10,@P11,@P12,@P13,@P14,@P15,@P16,@P17,@P18,@P19,@P20,@P21,@P22,@P23,@P24,@P25,@P26,@P27,@P28,@P29,@P30,@P31,@P32,@P33,@P34,@P35,@P36,@P37,@P38,@P39,@P40,@P41,@P42,@P43,@P44,@P45,@P46,@P47,@P48,@P49,@P50,@P51,@P52)',N'@P1 
varchar(20),@P2 varchar(50),@P3 varchar(255),@P4 int,@P5 int,@P6 int,@P7 varchar(50),@P8 varchar(40),@P9 varchar(11),@P10 varchar(40),@P11 varchar(20),@P12 varchar(40),@P13 varchar(255),@P14 varchar(80),@P15 
varchar(50),@P16 bit,@P17 int,@P18 float,@P19 varchar(80),@P20 varchar(80),@P21 varchar(80),@P22 varchar(80),@P23 varchar(80),@P24 int,@P25 int,@P26 int,@P27 int,@P28 int,@P29 int,@P30 int,@P31 int,@P32 int,@P33 
int,@P34 int,@P35 int,@P36 int,@P37 int,@P38 varchar(255),@P39 int,@P40 int,@P41 float,@P42 float,@P43 float,@P44 float,@P45 float,@P46 int,@P47 int,@P48 varchar(80),@P49 varchar(80),@P50 varchar(80),@P51 int,@P52 
int',NULL,NULL,NULL,1072,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1057,17,NULL,NULL,NULL,NULL,NULL,0,0,0,0,0,0,0,0,0,0,0,0,0,0,NULL,0,0,0,0,0,1,1,0,0,'C10008','C10008','刘云山',0,126

 Insert Into t_ItemRight (FTypeID,FUserID,FItemID)  select fitemclassid,fuserid,126 from t_useritemclassright where (( FUserItemClassRight &  8)=8) and fitemclassid=1 and fuserid<>16394

INSERT INTO t_Log (FDate,FUserID,FFunctionID,FStatement,FDescription,FMachineName,FIPAddress) VALUES (getdate(),16394,'A00701',5,'新建核算项目:C10008 核算项目类别:客户','PC-PENDENGTAO','192.168.1.3')

Select * from t_BaseProperty Where FTypeID = 3 And FItemID = 126
Insert Into t_BaseProperty(FTypeID, FItemID, FCreateDate, FCreateUser)Values(3, 126, '2013-03-25 21:04:51', 'administrator')

Delete from Access_t_Organization where FItemID=126

 Insert into Access_t_Organization(FItemID,FParentIDX,FDataAccessView,FDataAccessEdit,FDataAccessDelete)
 Values(126,0,convert(varbinary(7200),REPLICATE(char(255),100)),convert(varbinary(7200),REPLICATE(char(255),100)),convert(varbinary(7200),REPLICATE(char(255),100)))


Delete From t_FuncControl Where FID=66 And FUserID=16394

exec sp_executesql N'Insert Into t_FuncControl(FYear,FPeriod,FFuncID,FUserID,FRowID,FBizType,FStation,FTime) 
 Values(2013,1,509,16394,0,''0'',@P1,@P2)',N'@P1 varchar(255),@P2 datetime','PC-PENDENGTAO[0]',''2013-03-25 21:04:51:000''
/*===============================  客户数据插入时，相关的 SQL END CODE  =======================================*/

/*--------------------------------------------------------------------------------------------------------------*/ 
 /*===============================  职员数据插入时，相关的 SQL START CODE  =======================================*/
	exec sp_executesql N'INSERT INTO t_Item (FItemClassID,FParentID,FLevel,FName,FNumber,FShortNumber,FFullNumber,FDetail,UUID,FDeleted) VALUES 
	(@P1,@P2,@P3,@P4,@P5,@P6,@P7,@P8,@P9,@P10)',N'@P1 int,@P2 int,@P3 smallint,@P4 
varchar(80),@P5 varchar(80),@P6 varchar(80),@P7 varchar(80),@P8 bit,@P9 varchar(38),@P10 smallint',3,0,1,'彭登浩','C10002','C10002','C10002',1,'599FF148-98FA-4BD9-A131-8BDCFFE42584',0 

exec sp_executesql N'INSERT INTO t_Emp 
(FEmpGroup,FDepartmentID,FGender,FDegree,FPhone,FMobilePhone,FID,FDuty,FBankAccount,FAddress,FEmail,FNote,FIsCreditMgr,FAllotPercent,FOperationGroup,FOtherARAcctID,FPreARAcctID,FOtherAPAcctID,FPreAPAcctID,FShortNumber,FNumber,FName,FParentID,FItemID) 
VALUES (@P1,@P2,@P3,@P4,@P5,@P6,@P7,@P8,@P9,@P10,@P11,@P12,@P13,@P14,@P15,@P16,@P17,@P18,@P19,@P20,@P21,@P22,@P23,@P24)',N'@P1 int,@P2 int,@P3 int,@P4 int,@P5 varchar(40),@P6 varchar(11),@P7 varchar(18),@P8 int,@P9 
varchar(40),@P10 varchar(255),@P11 varchar(40),@P12 varchar(255),@P13 bit,@P14 float,@P15 int,@P16 int,@P17 int,@P18 int,@P19 int,@P20 varchar(80),@P21 varchar(80),@P22 varchar(80),@P23 int,@P24 
int',20288,125,1068,0,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,0,0,0,0,0,0,0,'C10002','C10002','彭登浩',0,129

INSERT INTO t_Log (FDate,FUserID,FFunctionID,FStatement,FDescription,FMachineName,FIPAddress) VALUES (getdate(),16394,'A00701',5,'新建核算项目:C10002 核算项目类别:职员','PC-PENDENGTAO','192.168.1.3')
/*===============================  职员数据插入时，相关的 SQL END CODE  =======================================*/

/*--------------------------------------------------------------------------------------------------------------*/ 
 /*===============================  用户数据插入时，相关的 SQL START CODE  =======================================*/
 exec sp_executesql N'INSERT INTO t_User (FUserID,FName,FSID,ID,FPrimaryGroup,FDescription,FPortUser,FForbidden,FEmpID,FSafeMode,FHRUser,FSSOUsername,FUInValidDate,FPwValidDay,FAuthRight,FPwCreateDate) VALUES 
(@P1,@P2,@P3,@P4,@P5,@P6,@P7,@P8,@P9,@P10,@P11,@P12,@P13,@P14,@P15,@P16)',N'@P1 int,@P2 varchar(100),@P3 varchar(255),@P4 varchar(40),@P5 int,@P6 varchar(80),@P7 nvarchar(200),@P8 int,@P9 int,@P10 int,@P11 int,@P12 
varchar(255),@P13 datetime,@P14 int,@P15 int,@P16 datetime',16400,'程明卫',')  F ", ,P T #8 *P!D &D 80!N &@ <0 C ''< : !M &4 )0!C &T =P ','C7FF5D82-980F-4EF9-8D50-B46F2749976A',0,NULL,N'',0,130,0,0,'',''1900-01-01 
00:00:00:000'',0,4,''2013-03-26 11:08:22:000''

SQL:BatchStarting	update t_User set PasswordHashValue= 0xBF29D00D0E7143015DBF43023AFCCB1C where FUserID=16400	Microsoft® Windows® Operating System	pdt	sa					2076	56	2013-03-26 11:08:22.450			

exec sp_executesql N'Insert into t_Group (FUserID, FGroupID) Values (@P1, @P2)',N'@P1 int,@P2 int',16400,0
  /*===============================  用户数据插入时，相关的 SQL END CODE  =======================================*/

/*--------------------------------------------------------------------------------------------------------------*/ 
 /*===============================  科目数据插入时，相关的 SQL START CODE  =======================================*/  
  exec sp_executesql N'INSERT INTO t_Account 
(FNumber,FName,FGroupID,FDC,FHelperCode,FCurrencyID,FAdjustRate,FIsCash,FIsBank,FJournal,FContact,FQuantities,FUnitGroupID,FMeasureUnitID,FDetailID,FIsCashFlow,FAcnt,FInterest,FIsAcnt,FAcctint,FLevel,FDetail,FParentID,FCFItemID,FSubCFItemID) 
VALUES (@P1,@P2,@P3,@P4,@P5,@P6,@P7,@P8,@P9,@P10,@P11,@P12,@P13,@P14,@P15,@P16,@P17,@P18,@P19,@P20,@P21,@P22,@P23,@P24,@P25)',N'@P1 varchar(40),@P2 varchar(80),@P3 int,@P4 int,@P5 varchar(40),@P6 int,@P7 bit,@P8 
bit,@P9 bit,@P10 bit,@P11 bit,@P12 bit,@P13 int,@P14 int,@P15 int,@P16 bit,@P17 bit,@P18 bit,@P19 bit,@P20 bit,@P21 smallint,@P22 bit,@P23 int,@P24 int,@P25 
int','1002','银行存款',101,1,NULL,1,0,0,1,1,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0

Insert Into t_BaseProperty(FTypeID, FItemID, FCreateDate, FCreateUser, FLastModDate, FLastModUser, FDeleteDate, FDeleteUser)
Values
(0, 1001, '2013-03-26 14:25:19', 'administrator', Null, Null, Null, Null)

如果有核算项目：
 Select i.FNumber FItemClassNumber, i.FName as FItemClassName, i.FItemClassID FItemClassID,
CASE ai.FItemID WHEN -1 THEN 1 ELSE 0 END FBalChecked 
 From t_Account a, t_ItemClass i,t_ItemDetailV ai
 Where  a.FDetailID = ai.FDetailID And  ai.FItemClassID = i.FItemClassID And 
 a.FAccountID=1002 And ai.FItemID In(-1,-2)

  /*===============================  科目数据插入时，相关的 SQL END CODE  =======================================*/

/*--------------------------------------------------------------------------------------------------------------*/ 
 /*===============================  凭证数据插入时，相关的 SQL START CODE  =======================================*/    
exec sp_executesql N'INSERT INTO t_Voucher 
(FDate,FTransDate,FYear,FPeriod,FGroupID,FNumber,FReference,FExplanation,FAttachments,FEntryCount,FDebitTotal,FCreditTotal,FInternalInd,FChecked,FPosted,FPreparerID,FCheckerID,FPosterID,FCashierID,FHandler,FObjectName,FParameter,FSerialNum,FTranType,FOwnerGroupID) 
VALUES (@P1,@P2,@P3,@P4,@P5,@P6,@P7,@P8,@P9,@P10,@P11,@P12,@P13,@P14,@P15,@P16,@P17,@P18,@P19,@P20,@P21,@P22,@P23,@P24,@P25)',N'@P1 datetime,@P2 datetime,@P3 int,@P4 int,@P5 int,@P6 int,@P7 varchar(255),@P8 
varchar(255),@P9 int,@P10 int,@P11 money,@P12 money,@P13 varchar(10),@P14 bit,@P15 bit,@P16 int,@P17 int,@P18 int,@P19 int,@P20 varchar(50),@P21 varchar(100),@P22 varchar(100),@P23 int,@P24 int,@P25 int',''2013-03-27 
00:00:00:000'',''2013-03-27 00:00:00:000'',2013,3,2,4,'程凭证录入测试参考','收客户利息',0,2,$100000.0000,$100000.0000,NULL,0,0,16398,-1,-1,-1,NULL,NULL,NULL,4,0,0

Insert Into t_ItemDetail(FDetailCount,F1) values(1,122)
Insert Into t_ItemDetail(FDetailCount,F1) values(1,123)

Insert Into t_ItemDetailV(FDetailID,FItemClassID,FItemID)Values(5,1,123)

exec sp_executesql N'INSERT INTO t_VoucherEntry 
(FVoucherID,FEntryID,FExplanation,FAccountID,FCurrencyID,FExchangeRate,FDC,FAmountFor,FAmount,FQuantity,FMeasureUnitID,FUnitPrice,FInternalInd,FAccountID2,FSettleTypeID,FSettleNo,FCashFlowItem,FTaskID,FResourceID,FTransNo,FDetailID) 
VALUES (@P1,@P2,@P3,@P4,@P5,@P6,@P7,@P8,@P9,@P10,@P11,@P12,@P13,@P14,@P15,@P16,@P17,@P18,@P19,@P20,@P21)',N'@P1 int,@P2 int,@P3 varchar(255),@P4 int,@P5 int,@P6 float,@P7 int,@P8 money,@P9 money,@P10 float,@P11 int,@P12 
float,@P13 varchar(10),@P14 int,@P15 int,@P16 varchar(40),@P17 int,@P18 int,@P19 int,@P20 varchar(255),@P21 int',4,0,'收客户利息',1004,1,1,1,$100000.0000,$100000.0000,0,0,0,NULL,1002,0,NULL,0,0,0,NULL,5
INSERT INTO t_Log (FDate,FUserID,FFunctionID,FStatement,FDescription,FMachineName,FIPAddress) VALUES (getdate(),16398,'A00201',5,'增加凭证:现付-3 会计年度:2013 会计期间:3','PC-PENDENGTAO','192.168.1.3')

exec sp_executesql N'INSERT INTO t_VoucherEntry 
(FVoucherID,FEntryID,FExplanation,FAccountID,FCurrencyID,FExchangeRate,FDC,FAmountFor,FAmount,FQuantity,FMeasureUnitID,FUnitPrice,FInternalInd,FAccountID2,FSettleTypeID,FSettleNo,FCashFlowItem,FTaskID,FResourceID,FTransNo,FDetailID) 
VALUES (@P1,@P2,@P3,@P4,@P5,@P6,@P7,@P8,@P9,@P10,@P11,@P12,@P13,@P14,@P15,@P16,@P17,@P18,@P19,@P20,@P21)',N'@P1 int,@P2 int,@P3 varchar(255),@P4 int,@P5 int,@P6 float,@P7 int,@P8 money,@P9 money,@P10 float,@P11 int,@P12 
float,@P13 varchar(10),@P14 int,@P15 int,@P16 varchar(40),@P17 int,@P18 int,@P19 int,@P20 varchar(255),@P21 int',4,1,'从公司建行程总帐户贷款',1002,1,1,0,$100000.0000,$100000.0000,0,0,0,NULL,1004,5,NULL,0,0,0,NULL,2

INSERT INTO t_Log (FDate,FUserID,FFunctionID,FStatement,FDescription,FMachineName,FIPAddress) VALUES (getdate(),16398,'A00201',5,'增加凭证:现付-4 会计年度:2013 会计期间:3','PC-PENDENGTAO','192.168.1.3')

exec sp_executesql N'Insert Into t_FuncControl(FYear,FPeriod,FFuncID,FUserID,FRowID,FBizType,FStation,FTime)  Values(2013,3,42,16398,4,''0'',@P1,@P2)',N'@P1 varchar(255),@P2 datetime','PC-PENDENGTAO[0]',''2013-03-27 
11:03:22:000''
