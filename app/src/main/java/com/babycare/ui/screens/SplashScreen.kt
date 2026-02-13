package com.babycare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.babycare.ui.theme.PrimaryPink
import com.babycare.ui.theme.SecondaryPink
import com.babycare.ui.theme.White
import kotlinx.coroutines.delay

/**
 * 启动页
 */
@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    var showLogo by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }

    // 动画序列
    LaunchedEffect(Unit) {
        showLogo = true
        delay(300)
        showText = true
        delay(2000)
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PrimaryPink,
                        SecondaryPink
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo 区域
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                if (showLogo) {
                    // 使用表情符号作为临时 Logo
                    Text(
                        text = "👶",
                        fontSize = 80.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 应用名称
            if (showText) {
                Text(
                    text = "宝宝护理",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "记录宝宝成长的每一刻",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = White.copy(alpha = 0.9f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // 加载指示器
            if (showText) {
                androidx.compose.material3.CircularProgressIndicator(
                    color = White,
                    modifier = Modifier.size(32.dp),
                    strokeWidth = 2.dp
                )
            }
        }

        // 底部版本号
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "v1.0.0",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = White.copy(alpha = 0.7f)
                )
            )
        }
    }
}

/**
 * 引导页数据
 */
data class OnboardingPage(
    val title: String,
    val description: String,
    val emoji: String
)

/**
 * 引导页
 */
@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            title = "全面记录",
            description = "记录宝宝的喂奶、睡眠、换尿布、\n生长发育等全方位数据",
            emoji = "📝"
        ),
        OnboardingPage(
            title = "智能提醒",
            description = "定时提醒换尿布、喂奶等重要事项，\n不再错过宝宝的每一个需求",
            emoji = "⏰"
        ),
        OnboardingPage(
            title = "生长曲线",
            description = "追踪宝宝身高体重变化，\n对比WHO国际标准",
            emoji = "📈"
        ),
        OnboardingPage(
            title = "数据安全",
            description = "支持数据备份和导出，\n珍贵记录永不丢失",
            emoji = "🔒"
        )
    )

    var currentPage by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF5F5))
    ) {
        // 顶部跳过按钮
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (currentPage < pages.size - 1) {
                Text(
                    text = "跳过",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = PrimaryPink
                    ),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable { onOnboardingComplete() }
                        .padding(8.dp)
                )
            }
        }

        // 页面内容
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            OnboardingPageContent(
                page = pages[currentPage],
                pageIndex = currentPage,
                totalPages = pages.size
            )
        }

        // 底部按钮区域
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            if (currentPage < pages.size - 1) {
                // 下一页按钮
                Button(
                    onClick = { currentPage++ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPink
                    )
                ) {
                    Text(
                        text = "下一步",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                // 开始使用按钮
                Button(
                    onClick = onOnboardingComplete,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPink
                    )
                ) {
                    Text(
                        text = "开始使用",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    pageIndex: Int,
    totalPages: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 32.dp)
    ) {
        // 图标
        Text(
            text = page.emoji,
            fontSize = 120.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // 标题
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 描述
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF666666),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // 页面指示器
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(totalPages) { index ->
                Box(
                    modifier = Modifier
                        .width(if (index == pageIndex) 24.dp else 8.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (index == pageIndex) PrimaryPink
                            else Color(0xFFE0E0E0)
                        )
                )
            }
        }
    }
}

