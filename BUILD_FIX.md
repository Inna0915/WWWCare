# 构建问题修复指南

## 问题："Unexpected lock protocol found in lock file"

这是 Gradle Wrapper JAR 文件损坏或不完整导致的。

## 解决方案

### 方法一：使用 Android Studio 自动修复（推荐）

1. **在 Android Studio 中打开项目**
   - 打开 Android Studio
   - 选择 `File` → `Open`
   - 选择 `baby-care/android-app` 目录

2. **等待自动同步**
   - Android Studio 会检测到 Gradle Wrapper 缺失
   - 点击提示中的 `Fix Gradle wrapper and re-import project`
   - 或者点击工具栏的 `Sync Project with Gradle Files` 按钮（大象图标）

3. **等待下载完成**
   - 首次同步可能需要 5-10 分钟下载 Gradle 和依赖项
   - 确保网络连接正常

### 方法二：手动下载 Gradle Wrapper

如果方法一无效，执行以下步骤：

1. **删除损坏的 wrapper 文件**
   ```bash
   cd baby-care/android-app
   rm -rf gradle/wrapper/gradle-wrapper.jar
   rm -rf .gradle
   rm -rf gradle/wrapper/*.lock
   ```

2. **使用系统安装的 Gradle 生成 wrapper**
   ```bash
   # 如果系统安装了 Gradle 8.2+
   gradle wrapper --gradle-version 8.2

   # 或者使用 Android Studio 内置的 Gradle
   # 在 Android Studio 的 Terminal 中执行：
   ./gradlew wrapper --gradle-version 8.2
   ```

3. **重新同步项目**
   - 在 Android Studio 中点击 `Sync Now`

### 方法三：使用本地 Gradle 分发

如果网络下载慢，可以配置本地 Gradle：

1. **编辑 `gradle/wrapper/gradle-wrapper.properties`**
   ```properties
   # 改为使用本地 Gradle（如果已安装）
   # 注释掉 distributionUrl
   # distributionUrl=https\://services.gradle.org/distributions/gradle-8.2-all.zip
   ```

2. **在 `settings.gradle.kts` 同级创建 `local.properties`**
   ```properties
   sdk.dir=C\:\\Users\\YOUR_NAME\\AppData\\Local\\Android\\Sdk
   ```

3. **使用本地 Gradle 构建**
   ```bash
   # 设置环境变量指向本地 Gradle
   set GRADLE_HOME=C:\\gradle-8.2
   set PATH=%GRADLE_HOME%\\bin;%PATH%
   gradle build
   ```

## 构建成功后

同步成功后，可以尝试：

1. **构建项目**
   - 菜单栏：`Build` → `Make Project` (Ctrl+F9)

2. **运行应用**
   - 连接 Android 设备或启动模拟器
   - 点击 `Run` → `Run 'app'` (Shift+F10)

## 常见问题

### Q: 提示 "JAVA_HOME not found"
A: 需要安装 JDK 17 并设置 JAVA_HOME 环境变量

### Q: 同步很慢或超时
A: 在 `gradle.properties` 中添加镜像配置：
```properties
systemProp.http.proxyHost=proxy.company.com
systemProp.http.proxyPort=8080
```

### Q: 依赖下载失败
A: 检查网络连接，确保可以访问 `https://maven.google.com` 和 `https://jitpack.io`

## 验证步骤

成功构建后应该看到：
```
BUILD SUCCESSFUL in 1m 23s
56 actionable tasks: 56 executed
```

如果遇到其他错误，请查看 Build 窗口的详细错误信息。
