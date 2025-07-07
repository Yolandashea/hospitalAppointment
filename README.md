这个是一个基于 Android 的“医院预约”App，包含用户注册登录、预约挂号、医生/科室信息浏览、支付、个人中心等功能。数据以本地数据库为主，适合小型或离线场景。
系统的主要功能和模块如下：
1. 主要功能模块
1.1 用户相关
登录/注册/修改密码
LoginActivity.java：用户登录界面与逻辑
RegisterActivity.java：用户注册界面与逻辑
ChangePasswordActivity.java：修改密码界面与逻辑
1.2 预约相关
预约挂号
MakeAppointmentActivity.java：发起预约
SelectDoctorActivity.java：选择医生
AppointmentFragment.java：展示用户预约信息
AppointmentAdapter.java：预约列表适配器
TimeSlotAdapter.java：时间段选择适配器
1.3 医院信息展示
首页/科室/医生/新闻
HomeFragment.java：首页
DepartmentAdapter.java：科室列表适配器
DoctorAdapter.java：医生列表适配器
NewsDetailActivity.java：新闻详情页
BannerAdapter.java：首页轮播图适配器
1.4 支付相关
支付功能
PaymentActivity.java：支付界面
PaymentDialog.java：支付弹窗
dialog_payment.xml、dialog_payment_method.xml：支付相关弹窗布局
1.5 用户中心
个人信息/设置
ProfileFragment.java：个人中心
ChangePasswordActivity.java：修改密码
2. 数据与持久化
db/DBHelper.java、db/DBManager.java：本地数据库操作，可能用于存储用户、预约、医生等信息。
model/ 目录下有 User.java、Doctor.java、Department.java、Appointment.java、TimeSlot.java 等数据模型，分别对应用户、医生、科室、预约、时间段等实体。
3. UI 资源
res/layout/：包含所有页面和弹窗的布局文件。
res/drawable/：图片和矢量图标资源。
res/values/：颜色、字符串、主题等资源配置。
res/menu/：底部导航菜单。
4. 其他
AndroidManifest.xml：应用权限、组件声明等。
proguard-rules.pro：混淆规则。
build.gradle 等：项目构建配置。
5. 典型用户流程
注册/登录 → 进入首页
浏览科室/医生信息 → 选择医生 → 选择时间段 → 预约挂号
支付预约费用
在个人中心查看预约记录、修改个人信息或密码
6. 技术栈
前端：Android 原生（Java）
本地数据库：SQLite（推测，基于 DBHelper/DBManager 命名）
UI：原生布局 + 适配器模式
