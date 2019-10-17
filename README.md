# control
 利用drools做风控处理
 **接口描述**
     根据请求传递的参数对用户操作进行风险评分
 
 **请求说明**
 
 > method：GET    
 > url:  /control/riskControl
 
 **请求参数**
 
 参数分为两部分，一部分为风控上限值设定、另一部分是用户数据
 
  1. 风控上限（该部分数据可不换使用默认值，如需变动请传递、传递后将永久覆盖默认值）
 
 | 字段 | 字段类型 | 字段说明 |
 | :--- | :------- | :------- |
 | distance | int | 地点间隔过远距离，默认：200000m |
 | oftensite | int | 常用城市，默认为去过城市的操作次数总和前几个，默认：3 |
 | loginfailtime | int | 登陆失败间隔时间，默认：3600s |
 | ipcnum | int | 单位时间ip更换次数，默认：3 |
 | dltime | int | 大额提币统计时间，默认：3600s |
 | loginfailnum | int | 登陆失败次数，默认：5 |
 | ipctime | int | IP更换间隔时间，默认：3600s |
 | ipntime | int | 新IP操作间隔时间，默认：60s |
 | oftenip | int | 头几条为常用ip，默认：5 |
 | dollerlimit | double | 单次提币金额限制，大额界定，默认：0不限制 |
 | withdrawdollerlimit | double | 单次提现限制金额，默认：0不限制 |
 | coinnumlimit | int | 一天内限制提币次数，默认：0不限制 |
 | withdrawnumlimit | int | 一天内限制提现次数，默认：0不限制 |
 | refer | string | 交易所源站点refer，默认：null，不限制 |
 
 2. 用户数据（除必要参数外，其他可不传递即不验证相关规则）
 
 | 字段 | 字段类型 | 是否必传 | 字段说明 |
 | :--- | :------- | :------- | :------- |
 | accountId | int | Y | 用户id |
 | ip | string | Y | 用户ip |
 | operationType | string | Y | 操作类型 |
 | deviceType | int | Y | 设备类型1、PC 2、MOBILE |
 | dollar | double | N | 操作金额（若操作类型是提币或提现，该参数必传） |
 | operateTime | long | N | 操作时间(时间戳) |
 | userAgent | string | N | http请求所带的userAgent |
 | refer | string | N | http请求所带的refer，算是起始网址 |
 | mac | string | N | mac地址 |
 | operateSource | string | N | 操作来源1、PC 2、H5 3、App |
 | appVersion | string | N | app版本 |
 | userNameType | string | N | 用户名类型 |
 | registerTime | long | N | 注册时间(时间戳) |
 | loginType | string | N | 登录类型 |
 | registerIp | string | N | 注册ip |
 | loginResult | string | N | 登录结果 |
 | hashedPassword | string | N | hash后的密码 |
 | failReason | string | N | 失败原因 |
 | imsi | string | N | imsi |
 | imei | string | N | imei |
 | wifiMac | string | N | wifi mac |
 
 **返回结果**
 
     {
         data: "150分",
         errCode: 0,
         errMsg: "successful"
     }
