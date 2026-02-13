# 宝宝护理记录 App (Baby Care)

基于 Jetpack Compose 的 Android 宝宝护理记录应用，完整复刻了微信小程序的界面设计。

## 界面预览

### 已实现的页面

1. **首页 (HomeScreen)**
   - 今日概览卡片（喂奶次数、睡眠时长、换尿布次数等）
   - 进行中的记录提示
   - 今日记录时间轴

2. **日历/记录列表 (CalendarScreen)**
   - 按日期查看记录
   - 分类筛选（全部、喂养、换尿布、睡眠等）
   - 每日统计摘要

3. **换尿布 (DiaperScreen)**
   - 类型选择（嘘嘘/便便/两者）
   - 尿布重量（很轻/正常/很重）
   - 便便状态（多种选项）
   - 便便颜色选择器（含拍照功能）
   - 设置提醒

4. **睡眠计时器 (SleepTimerScreen)**
   - 大字体计时器显示
   - 开始/结束/放弃操作
   - 月亮主题界面

5. **母乳喂养/吸奶器**
   - 左右侧时长记录
   - 奶量记录
   - 计时功能

6. **奶瓶喂养 (BottleFeedingScreen)**
   - 喂养类型选择
   - 奶量选择器
   - 奶粉品牌标签
   - 喂养指南入口

7. **辅食记录 (FoodScreen)**
   - 辅食类型选择弹窗
   - 性状选择（稀滑/泥状/粘稠）
   - 食量记录
   - 宝宝反馈表情（喜欢/一般/不喜欢/过敏）

8. **体温记录 (TemperatureScreen)**
   - 体温参考弹窗
   - WHO标准对照表
   - 温度范围可视化

9. **生长发育 (GrowthRecordScreen)**
   - 身高体重头围脚长记录
   - 生长曲线图表
   - WHO标准百分位曲线

10. **疫苗管理 (VaccineScreen)**
    - 疫苗接种计划
    - 免费/自费标签
    - 接种状态追踪

11. **快速添加菜单 (QuickAddMenu)**
    - 16个功能入口网格
    - 底部弹出动画
    - 图标+文字按钮

## 技术栈

- **开发语言**: Kotlin
- **UI框架**: Jetpack Compose
- **架构模式**: MVVM + Repository
- **数据库**: Room (SQLite封装)
- **设计规范**: Material Design 3

## 项目结构

```
android-app/
├── app/src/main/java/com/babycare/
│   ├── MainActivity.kt                 # 应用入口
│   ├── ui/
│   │   ├── theme/                      # 主题配置
│   │   │   ├── Color.kt               # 颜色定义
│   │   │   ├── Theme.kt               # Material主题
│   │   │   └── Type.kt                # 字体样式
│   │   ├── components/                 # 通用组件
│   │   │   ├── Buttons.kt             # 按钮组件
│   │   │   ├── Inputs.kt              # 输入组件
│   │   │   └── SelectionComponents.kt # 选择组件
│   │   └── screens/                    # 页面
│   │       ├── HomeScreen.kt
│   │       ├── CalendarScreen.kt
│   │       ├── DiaperScreen.kt
│   │       ├── SleepTimerScreen.kt
│   │       ├── BottleFeedingScreen.kt
│   │       ├── FoodScreen.kt
│   │       ├── TemperatureScreen.kt
│   │       ├── GrowthRecordScreen.kt
│   │       ├── GrowthChartScreen.kt
│   │       ├── VaccineScreen.kt
│   │       ├── ActivityTimerScreen.kt
│   │       ├── BottomNavBar.kt
│   │       └── QuickAddMenu.kt
│   ├── data/
│   │   ├── BabyDatabase.kt            # Room数据库
│   │   ├── dao/
│   │   │   └── RecordDao.kt           # 数据访问对象
│   │   ├── model/
│   │   │   └── RecordEntities.kt      # 实体类定义
│   │   └── repository/
│   │       └── RecordRepository.kt    # 数据仓库
│   └── viewmodel/
│       └── RecordViewModel.kt         # 业务逻辑
└── README.md
```

## 配色方案

从截图提取的原始配色：

- **主色调**: 粉红色 `#FF6B7A`
- **背景色**:
  - 淡蓝色 `#F0F7FF` (奶瓶喂养、体温)
  - 淡绿色 `#F0FFFA` (吸奶器、生长发育)
  - 淡粉色 `#FFF5F5` (换尿布)
  - 淡紫色 `#F8F5FF` (睡眠)
  - 淡黄色 `#FFFFF9F0` (活动)

## 核心组件

### 按钮
- `PrimarySaveButton` - 主保存按钮（粉红色渐变）
- `StartTimerButton` - 计时开始/结束按钮
- `SecondaryButton` - 次要按钮（白色背景）

### 输入
- `FormInputItem` - 表单项（左侧标签，右侧值）
- `FormCard` - 白色卡片容器
- `NoteInput` - 备注输入区域
- `ReminderRow` - 设置提醒行（含开关）

### 选择组件
- `SelectableTag` - 可选择的标签
- `TagGroup` / `FlowTagGroup` - 标签组
- `ColorSelector` - 颜色选择圆点
- `MoodSelector` - 情绪选择表情

## 快速开始

1. 创建 Android Studio 项目
2. 复制 `ui/` 和 `MainActivity.kt` 到项目中
3. 添加 Compose 依赖到 `build.gradle`:

```gradle
android {
    buildFeatures {
        compose true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    implementation "androidx.core:core-ktx:1.12.0"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.7.0"
    implementation "androidx.activity:activity-compose:1.8.2"
    implementation platform("androidx.compose:compose-bom:2024.02.00")
    implementation "androidx.compose.ui:ui"
    implementation "androidx.compose.ui:ui-graphics"
    implementation "androidx.compose.ui:ui-tooling-preview"
    implementation "androidx.compose.material3:material3"

    // Room 数据库
    def room_version = "2.6.1"
    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"

    // ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0"

    // 协程
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"
}
```

4. **添加 Room kapt 插件**（项目级 `build.gradle`）：

```gradle
plugins {
    id 'com.android.application' version '8.2.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.20' apply false
    id 'org.jetbrains.kotlin.kapt' version '1.9.20' apply false  // 添加这行
}
```

5. 运行应用

## 数据库设计 (Room)

### 核心表结构

| 表名 | 说明 |
|------|------|
| `records` | 基础记录表（时间、类型、时长、备注） |
| `breast_feeding_details` | 母乳喂养详情 |
| `bottle_feeding_details` | 奶瓶喂养详情 |
| `diaper_details` | 换尿布详情 |
| `sleep_details` | 睡眠详情 |
| `food_details` | 辅食详情 |
| `growth_details` | 生长发育详情 |
| `temperature_details` | 体温详情 |
| `vaccine_details` | 疫苗详情 |
| `medication_details` | 用药详情 |
| `supplement_details` | 补剂详情 |
| `pump_details` | 吸奶器详情 |
| `activity_details` | 活动详情 |
| `note_details` | 随手记详情 |

### 数据库特点

1. **一对多关系**: 基础记录表 + 详情表，通过 `record_id` 关联
2. **类型安全**: 使用枚举类型转换器
3. **协程支持**: 所有数据库操作支持挂起函数
4. **Flow响应式**: 查询结果自动更新 UI
5. **级联删除**: 删除主记录自动删除关联详情

### 使用示例

```kotlin
// 在 ViewModel 中使用
class RecordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecordRepository

    init {
        val database = BabyDatabase.getDatabase(application)
        repository = RecordRepository(database.recordDao())
    }

    // 添加换尿布记录
    fun addDiaperRecord() {
        viewModelScope.launch {
            repository.addDiaperRecord(
                startTime = System.currentTimeMillis(),
                type = DiaperType.BOTH,
                weight = DiaperWeight.NORMAL,
                poopState = "普通软糊状",
                poopColor = "黄色",
                photoUri = null,
                note = null
            )
        }
    }

    // 获取今日概览
    fun loadTodayOverview() {
        viewModelScope.launch {
            val overview = repository.getTodayOverview(System.currentTimeMillis())
            // 更新 UI
        }
    }
}

// 在 Compose 中观察
@Composable
fun RecordList(viewModel: RecordViewModel = viewModel()) {
    val records by viewModel.allRecords.collectAsState()
    LazyColumn {
        items(records) { record ->
            RecordItem(record)
        }
    }
}
```

## 待完善功能

1. ✅ **数据持久化** - Room数据库已完成
2. 图表库集成（MPAndroidChart/Compose Charts）
3. 通知提醒（WorkManager）
4. 数据导入导出（Excel/JSON）
5. 多宝宝支持
6. 深色模式

## 设计特点

1. **柔和的配色** - 使用 pastel 色系，护眼舒适
2. **大按钮设计** - 方便单手操作
3. **表情反馈** - 使用 emoji 直观表达情绪
4. **卡片式布局** - 信息层次分明
5. **快速操作** - 底部导航 + 快捷菜单

## License

MIT License
