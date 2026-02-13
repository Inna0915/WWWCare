# Baby Care App - 设计文档

## 项目概述

基于 Jetpack Compose 的 Android 宝宝护理记录应用，复刻微信小程序界面设计，用于记录宝宝日常护理数据。

## 技术架构

### 技术栈
- **开发语言**: Kotlin
- **UI框架**: Jetpack Compose
- **架构模式**: MVVM + Repository Pattern
- **数据库**: Room (SQLite 封装)
- **依赖注入**: 无（使用原生 ViewModel 工厂）
- **异步处理**: Kotlin Coroutines + Flow

### 项目结构
```
com.babycare/
├── MainActivity.kt                 # 应用入口
├── ui/
│   ├── theme/                      # 主题配置
│   │   ├── Color.kt               # 颜色定义
│   │   ├── Theme.kt               # Material 主题
│   │   └── Type.kt                # 字体样式
│   ├── components/                 # 通用组件
│   │   ├── Buttons.kt             # 按钮组件
│   │   ├── Inputs.kt              # 输入组件
│   │   └── SelectionComponents.kt # 选择组件
│   └── screens/                    # 页面
│       ├── HomeScreen.kt          # 首页概览
│       ├── CalendarScreen.kt      # 日历/记录列表
│       ├── DiaperScreen.kt        # 换尿布记录
│       ├── SleepTimerScreen.kt    # 睡眠计时器
│       ├── BottleFeedingScreen.kt # 奶瓶喂养
│       ├── FoodScreen.kt          # 辅食记录
│       ├── TemperatureScreen.kt   # 体温记录
│       ├── GrowthRecordScreen.kt  # 生长发育
│       ├── VaccineScreen.kt       # 疫苗管理
│       ├── BottomNavBar.kt        # 底部导航
│       └── QuickAddMenu.kt        # 快速添加菜单
├── data/
│   ├── BabyDatabase.kt            # Room 数据库
│   ├── dao/
│   │   └── RecordDao.kt           # 数据访问对象
│   ├── model/
│   │   └── RecordEntities.kt      # 实体类定义
│   └── repository/
│       └── RecordRepository.kt    # 数据仓库
└── viewmodel/
    └── RecordViewModel.kt         # 业务逻辑
```

## 数据库设计

### 核心设计原则
采用**一对多关系**设计：基础记录表 + 详情表，通过 `record_id` 关联

### 表结构

#### 1. 基础记录表 (records)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | String (PK) | UUID 主键 |
| type | RecordType | 记录类型枚举 |
| start_time | Long | 开始时间戳 |
| end_time | Long? | 结束时间戳 |
| duration | Int? | 持续时间（分钟）|
| note | String? | 备注 |
| created_at | Long | 创建时间 |
| updated_at | Long | 更新时间 |
| sync_status | SyncStatus | 同步状态 |

#### 2. 详情表
- **breast_feeding_details**: 母乳喂养（左右侧时长、喂养位置）
- **bottle_feeding_details**: 奶瓶喂养（奶量、类型、品牌、段数）
- **diaper_details**: 换尿布（类型、重量、便便状态/颜色、照片）
- **sleep_details**: 睡眠（入睡方式、睡眠质量）
- **food_details**: 辅食（类型、性状、食量、反馈）
- **growth_details**: 生长发育（身高、体重、头围、脚长）
- **temperature_details**: 体温（温度值）
- **vaccine_details**: 疫苗（名称、类型、剂次、计划日期）
- **medication_details**: 用药（药品名、用量）
- **supplement_details**: 补剂（名称、用量）
- **pump_details**: 吸奶器（左右侧奶量/时长）
- **activity_details**: 活动（类型）
- **note_details**: 随手记（内容、照片）

### 枚举类型
```kotlin
enum class RecordType {
    BREAST_FEEDING, BOTTLE_FEEDING, DIAPER, SLEEP,
    FOOD, GROWTH, TEMPERATURE, VACCINE,
    MEDICATION, SUPPLEMENT, PUMP, ACTIVITY, NOTE
}
```

## UI 设计规范

### 配色方案
从截图提取的原始配色：

| 场景 | 颜色值 | 用途 |
|------|--------|------|
| 主色调 | `#FF6B7A` | 按钮、强调色 |
| 背景-淡蓝 | `#F0F7FF` | 奶瓶喂养、体温 |
| 背景-淡绿 | `#F0FFFA` | 吸奶器、生长发育 |
| 背景-淡粉 | `#FFF5F5` | 换尿布 |
| 背景-淡紫 | `#F8F5FF` | 睡眠 |
| 背景-淡黄 | `#FFFFF9F0` | 活动 |

### 核心组件

#### 按钮
- `PrimarySaveButton`: 主保存按钮（粉红色渐变）
- `StartTimerButton`: 计时开始/结束按钮（圆形）
- `SecondaryButton`: 次要按钮（白色背景）

#### 输入
- `FormInputItem`: 表单项（左侧标签，右侧值/输入）
- `FormCard`: 白色卡片容器
- `NoteInput`: 备注输入区域
- `ReminderRow`: 设置提醒行（含开关）

#### 选择组件
- `SelectableTag`: 可选择的标签
- `TagGroup` / `FlowTagGroup`: 标签组（横向/流式布局）
- `ColorSelector`: 颜色选择圆点
- `MoodSelector`: 情绪选择表情

## ViewModel 设计

### 状态管理
使用 `StateFlow` 实现响应式 UI：

```kotlin
private val _allRecords = MutableStateFlow<List<Record>>(emptyList())
val allRecords: StateFlow<List<Record>> = _allRecords.asStateFlow()

private val _todayOverview = MutableStateFlow<DailyOverview?>(null)
val todayOverview: StateFlow<DailyOverview?> = _todayOverview.asStateFlow()
```

### 数据流
```
UI (Compose)
  ↕
ViewModel (StateFlow)
  ↕
Repository (suspend/Flow)
  ↕
DAO (Room)
  ↕
Database (SQLite)
```

### 记录操作方法签名
```kotlin
fun addDiaperRecord(
    startTime: Long,
    type: DiaperType,
    weight: DiaperWeight,
    poopState: String?,
    poopColor: String?,
    photoUri: String?,
    note: String?,
    onComplete: (String) -> Unit = {}
)
```

## 关键实现细节

### 1. 时间处理
- 存储：使用 `System.currentTimeMillis()`（Long 时间戳）
- 显示：使用 `SimpleDateFormat` 格式化为本地时间字符串
- 时区：使用设备默认时区

### 2. 枚举类型转换
使用 Room TypeConverter 存储枚举：
```kotlin
@TypeConverter
fun fromDiaperType(value: DiaperType): String = value.name

@TypeConverter
fun toDiaperType(value: String): DiaperType = enumValueOf(value)
```

### 3. 级联删除
详情表设置外键级联删除：
```kotlin
foreignKeys = [ForeignKey(
    entity = Record::class,
    parentColumns = ["id"],
    childColumns = ["record_id"],
    onDelete = ForeignKey.CASCADE
)]
```

### 4. 页面间通信
使用回调模式：
```kotlin
@Composable
fun DiaperScreen(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: RecordViewModel = viewModel()
)
```

## 待完成功能

1. ⏳ 时间选择器组件（Material DatePicker/TimePicker）
2. ⏳ 图片选择与保存（PhotoPicker + 文件存储）
3. ⏳ 图表库集成（MPAndroidChart/Compose Charts）
4. ⏳ 通知提醒系统（WorkManager）
5. ⏳ 设置页面
6. ⏳ 数据导入导出（Excel/JSON）
7. ⏳ 多宝宝支持
8. ⏳ 深色模式

## 构建配置

### 依赖版本
```kotlin
composeBom = "2024.02.00"
roomVersion = "2.6.1"
kotlinCompilerExtensionVersion = "1.5.8"
minSdk = 24
targetSdk = 34
```

### 关键依赖
- `androidx.compose.material3:material3`
- `androidx.navigation:navigation-compose:2.7.7`
- `androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0`
- `androidx.room:room-runtime` / `room-ktx`
- `org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3`

---

*文档版本: 1.0*
*最后更新: 2026-02-13*
