package com.babycare.ui.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.babycare.ui.theme.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * 图片选择器组件
 * 支持相册选择和相机拍照（预留）
 */
@Composable
fun ImagePicker(
    selectedImageUri: Uri?,
    onImageSelected: (Uri?) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "添加照片"
) {
    var showPickerDialog by remember { mutableStateOf(false) }

    // PhotoPicker 启动器 (Android 13+ 使用 PickVisualMedia)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onImageSelected(uri)
        }
    }

    // 传统图片选择器（兼容旧版本）
    val legacyPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        onImageSelected(uri)
    }

    Box(
        modifier = modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = if (selectedImageUri == null) Gray300 else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .background(if (selectedImageUri == null) Gray100 else Color.Transparent)
            .clickable { showPickerDialog = true },
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri != null) {
            // 显示已选图片
            Box {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "已选图片",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // 删除按钮
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Gray800.copy(alpha = 0.7f))
                        .align(Alignment.TopEnd)
                        .clickable { onImageSelected(null) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "删除",
                        tint = White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        } else {
            // 显示添加按钮
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "添加照片",
                    tint = Gray500,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = placeholderText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Gray500
                )
            }
        }
    }

    // 选择对话框
    if (showPickerDialog) {
        ImagePickerDialog(
            onDismiss = { showPickerDialog = false },
            onGalleryClick = {
                showPickerDialog = false
                // 使用新版 PhotoPicker（如果支持）
                try {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                } catch (e: Exception) {
                    // 回退到传统选择器
                    legacyPickerLauncher.launch("image/*")
                }
            },
            onCameraClick = {
                showPickerDialog = false
                // TODO: 实现相机拍照功能
            }
        )
    }
}

/**
 * 图片选择对话框
 */
@Composable
private fun ImagePickerDialog(
    onDismiss: () -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "选择图片来源",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 相册选项
                    PickerOption(
                        icon = Icons.Default.PhotoLibrary,
                        label = "相册",
                        onClick = onGalleryClick
                    )

                    // 相机选项
                    PickerOption(
                        icon = Icons.Default.CameraAlt,
                        label = "拍照",
                        onClick = onCameraClick
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 取消按钮
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("取消")
                }
            }
        }
    }
}

/**
 * 选择选项按钮
 */
@Composable
private fun PickerOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(LightOrange.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = PrimaryPink,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * 图片工具类
 */
object ImageUtils {

    /**
     * 保存图片到应用私有目录
     * @param context 上下文
     * @param sourceUri 源图片 URI
     * @param directory 子目录名（如 "diaper_photos"）
     * @return 保存后的文件 URI，失败返回 null
     */
    fun saveImageToInternalStorage(
        context: Context,
        sourceUri: Uri,
        directory: String
    ): Uri? {
        return try {
            // 创建目标目录
            val destDir = File(context.filesDir, directory).apply {
                if (!exists()) mkdirs()
            }

            // 生成文件名
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val destFile = File(destDir, "IMG_${timestamp}.jpg")

            // 复制文件
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }

            Uri.fromFile(destFile)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 删除图片
     * @param uri 图片 URI
     * @return 是否删除成功
     */
    fun deleteImage(uri: Uri?): Boolean {
        if (uri == null) return false
        return try {
            val file = File(uri.path ?: return false)
            if (file.exists()) {
                file.delete()
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 获取图片文件大小（MB）
     */
    fun getImageSizeMB(context: Context, uri: Uri?): Float {
        if (uri == null) return 0f
        return try {
            context.contentResolver.openFileDescriptor(uri, "r")?.use { parcelFileDescriptor ->
                parcelFileDescriptor.statSize / (1024f * 1024f)
            } ?: 0f
        } catch (e: Exception) {
            0f
        }
    }
}

/**
 * 多图片选择器（用于支持多图的场景）
 */
@Composable
fun MultiImagePicker(
    selectedImages: List<Uri>,
    onImagesChanged: (List<Uri>) -> Unit,
    maxImages: Int = 5,
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxImages)
    ) { uris ->
        if (uris.isNotEmpty()) {
            val newList = (selectedImages + uris).take(maxImages)
            onImagesChanged(newList)
        }
    }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 已选图片
        selectedImages.forEachIndexed { index, uri ->
            SelectedImageItem(
                uri = uri,
                onDelete = {
                    onImagesChanged(selectedImages.toMutableList().apply { removeAt(index) })
                }
            )
        }

        // 添加按钮
        if (selectedImages.size < maxImages) {
            AddImageButton(
                onClick = { showPicker = true },
                remaining = maxImages - selectedImages.size
            )
        }
    }

    if (showPicker) {
        try {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
            showPicker = false
        } catch (e: Exception) {
            // 处理异常
            showPicker = false
        }
    }
}

/**
 * 已选图片项
 */
@Composable
private fun SelectedImageItem(
    uri: Uri,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier.size(80.dp)
    ) {
        AsyncImage(
            model = uri,
            contentDescription = "图片",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(Gray800.copy(alpha = 0.7f))
                .align(Alignment.TopEnd)
                .clickable(onClick = onDelete),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "删除",
                tint = White,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

/**
 * 添加图片按钮
 */
@Composable
private fun AddImageButton(
    onClick: () -> Unit,
    remaining: Int
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Gray300, RoundedCornerShape(8.dp))
            .background(Gray100)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "添加",
                tint = Gray500
            )
            Text(
                text = "还可选$remaining",
                style = MaterialTheme.typography.bodySmall,
                color = Gray500
            )
        }
    }
}

/**
 * FlowRow 简单实现
 */
@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    // 使用 BoxWithConstraints 实现简单的流式布局
    // 实际项目中可以使用 accompanist-flowlayout
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        content()
    }
}
