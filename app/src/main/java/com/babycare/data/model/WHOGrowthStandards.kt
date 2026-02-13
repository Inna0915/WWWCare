package com.babycare.data.model

import com.babycare.ui.components.PercentileData
import com.babycare.data.model.Gender

/**
 * WHO 生长发育标准数据 (0-24个月)
 * 数据来源: WHO Child Growth Standards
 */
object WHOGrowthStandards {

    /**
     * 获取身高百分位数据 (厘米)
     * @param gender 性别
     * @return 男孩或女孩的身高百分位数据
     */
    fun getHeightPercentiles(gender: Gender): PercentileData {
        return if (gender == Gender.BOY) {
            boyHeightPercentiles
        } else {
            girlHeightPercentiles
        }
    }

    /**
     * 获取体重百分位数据 (公斤)
     */
    fun getWeightPercentiles(gender: Gender): PercentileData {
        return if (gender == Gender.BOY) {
            boyWeightPercentiles
        } else {
            girlWeightPercentiles
        }
    }

    /**
     * 获取头围百分位数据 (厘米)
     */
    fun getHeadCircumferencePercentiles(gender: Gender): PercentileData {
        return if (gender == Gender.BOY) {
            boyHeadCircumferencePercentiles
        } else {
            girlHeadCircumferencePercentiles
        }
    }

    // 男孩身高百分位 (月龄, 厘米)
    private val boyHeightPercentiles = PercentileData(
        p3 = listOf(
            0f to 46.3f, 1f to 50.8f, 2f to 54.4f, 3f to 57.3f,
            4f to 59.7f, 5f to 61.7f, 6f to 63.4f, 9f to 67.7f,
            12f to 71.0f, 15f to 74.0f, 18f to 76.6f, 21f to 79.1f, 24f to 81.4f
        ),
        p15 = listOf(
            0f to 48.0f, 1f to 52.3f, 2f to 55.8f, 3f to 58.6f,
            4f to 61.0f, 5f to 62.9f, 6f to 64.6f, 9f to 68.9f,
            12f to 72.3f, 15f to 75.2f, 18f to 77.8f, 21f to 80.3f, 24f to 82.7f
        ),
        p50 = listOf(
            0f to 49.9f, 1f to 54.0f, 2f to 57.3f, 3f to 60.0f,
            4f to 62.2f, 5f to 64.1f, 6f to 65.7f, 9f to 70.0f,
            12f to 73.4f, 15f to 76.3f, 18f to 79.0f, 21f to 81.5f, 24f to 83.9f
        ),
        p85 = listOf(
            0f to 51.8f, 1f to 55.7f, 2f to 58.9f, 3f to 61.4f,
            4f to 63.5f, 5f to 65.3f, 6f to 66.9f, 9f to 71.1f,
            12f to 74.5f, 15f to 77.4f, 18f to 80.1f, 21f to 82.7f, 24f to 85.1f
        ),
        p97 = listOf(
            0f to 53.4f, 1f to 57.2f, 2f to 60.3f, 3f to 62.7f,
            4f to 64.7f, 5f to 66.5f, 6f to 68.0f, 9f to 72.2f,
            12f to 75.6f, 15f to 78.6f, 18f to 81.3f, 21f to 83.9f, 24f to 86.4f
        )
    )

    // 女孩身高百分位
    private val girlHeightPercentiles = PercentileData(
        p3 = listOf(
            0f to 45.6f, 1f to 49.8f, 2f to 53.2f, 3f to 55.9f,
            4f to 58.2f, 5f to 60.1f, 6f to 61.7f, 9f to 65.8f,
            12f to 69.0f, 15f to 71.8f, 18f to 74.4f, 21f to 76.7f, 24f to 79.1f
        ),
        p15 = listOf(
            0f to 47.2f, 1f to 51.3f, 2f to 54.5f, 3f to 57.2f,
            4f to 59.3f, 5f to 61.2f, 6f to 62.8f, 9f to 66.9f,
            12f to 70.1f, 15f to 72.9f, 18f to 75.5f, 21f to 77.9f, 24f to 80.2f
        ),
        p50 = listOf(
            0f to 49.1f, 1f to 53.0f, 2f to 56.1f, 3f to 58.7f,
            4f to 60.8f, 5f to 62.6f, 6f to 64.1f, 9f to 68.2f,
            12f to 71.3f, 15f to 74.1f, 18f to 76.7f, 21f to 79.1f, 24f to 81.4f
        ),
        p85 = listOf(
            0f to 51.0f, 1f to 54.7f, 2f to 57.7f, 3f to 60.2f,
            4f to 62.2f, 5f to 64.0f, 6f to 65.5f, 9f to 69.5f,
            12f to 72.6f, 15f to 75.4f, 18f to 78.0f, 21f to 80.4f, 24f to 82.7f
        ),
        p97 = listOf(
            0f to 52.7f, 1f to 56.2f, 2f to 59.1f, 3f to 61.5f,
            4f to 63.4f, 5f to 65.2f, 6f to 66.6f, 9f to 70.5f,
            12f to 73.6f, 15f to 76.4f, 18f to 79.0f, 21f to 81.4f, 24f to 83.7f
        )
    )

    // 男孩体重百分位 (公斤)
    private val boyWeightPercentiles = PercentileData(
        p3 = listOf(
            0f to 2.5f, 1f to 3.4f, 2f to 4.3f, 3f to 5.0f,
            4f to 5.6f, 5f to 6.0f, 6f to 6.4f, 9f to 7.4f,
            12f to 8.1f, 15f to 8.8f, 18f to 9.4f, 21f to 10.0f, 24f to 10.5f
        ),
        p15 = listOf(
            0f to 2.9f, 1f to 3.9f, 2f to 4.9f, 3f to 5.7f,
            4f to 6.2f, 5f to 6.7f, 6f to 7.1f, 9f to 8.2f,
            12f to 9.0f, 15f to 9.8f, 18f to 10.5f, 21f to 11.2f, 24f to 11.8f
        ),
        p50 = listOf(
            0f to 3.3f, 1f to 4.5f, 2f to 5.6f, 3f to 6.4f,
            4f to 7.0f, 5f to 7.5f, 6f to 7.9f, 9f to 9.2f,
            12f to 10.1f, 15f to 11.0f, 18f to 11.8f, 21f to 12.5f, 24f to 13.2f
        ),
        p85 = listOf(
            0f to 3.9f, 1f to 5.1f, 2f to 6.3f, 3f to 7.2f,
            4f to 7.8f, 5f to 8.4f, 6f to 8.9f, 9f to 10.3f,
            12f to 11.3f, 15f to 12.3f, 18f to 13.2f, 21f to 14.1f, 24f to 14.9f
        ),
        p97 = listOf(
            0f to 4.3f, 1f to 5.7f, 2f to 7.0f, 3f to 7.9f,
            4f to 8.6f, 5f to 9.2f, 6f to 9.7f, 9f to 11.2f,
            12f to 12.3f, 15f to 13.4f, 18f to 14.4f, 21f to 15.4f, 24f to 16.3f
        )
    )

    // 女孩体重百分位
    private val girlWeightPercentiles = PercentileData(
        p3 = listOf(
            0f to 2.4f, 1f to 3.2f, 2f to 4.0f, 3f to 4.6f,
            4f to 5.1f, 5f to 5.5f, 6f to 5.9f, 9f to 6.8f,
            12f to 7.5f, 15f to 8.2f, 18f to 8.8f, 21f to 9.3f, 24f to 9.9f
        ),
        p15 = listOf(
            0f to 2.7f, 1f to 3.7f, 2f to 4.5f, 3f to 5.2f,
            4f to 5.7f, 5f to 6.1f, 6f to 6.5f, 9f to 7.5f,
            12f to 8.3f, 15f to 9.1f, 18f to 9.8f, 21f to 10.4f, 24f to 11.0f
        ),
        p50 = listOf(
            0f to 3.2f, 1f to 4.2f, 2f to 5.1f, 3f to 5.9f,
            4f to 6.4f, 5f to 6.9f, 6f to 7.3f, 9f to 8.5f,
            12f to 9.4f, 15f to 10.3f, 18f to 11.1f, 21f to 11.8f, 24f to 12.5f
        ),
        p85 = listOf(
            0f to 3.7f, 1f to 4.8f, 2f to 5.8f, 3f to 6.6f,
            4f to 7.2f, 5f to 7.7f, 6f to 8.2f, 9f to 9.5f,
            12f to 10.5f, 15f to 11.5f, 18f to 12.4f, 21f to 13.3f, 24f to 14.1f
        ),
        p97 = listOf(
            0f to 4.2f, 1f to 5.4f, 2f to 6.5f, 3f to 7.4f,
            4f to 8.0f, 5f to 8.6f, 6f to 9.1f, 9f to 10.5f,
            12f to 11.6f, 15f to 12.7f, 18f to 13.7f, 21f to 14.7f, 24f to 15.6f
        )
    )

    // 男孩头围百分位 (厘米)
    private val boyHeadCircumferencePercentiles = PercentileData(
        p3 = listOf(
            0f to 32.1f, 1f to 35.1f, 2f to 37.1f, 3f to 38.5f,
            6f to 41.0f, 9f to 42.8f, 12f to 44.1f, 18f to 45.8f, 24f to 47.0f
        ),
        p15 = listOf(
            0f to 33.1f, 1f to 36.0f, 2f to 37.9f, 3f to 39.3f,
            6f to 41.7f, 9f to 43.5f, 12f to 44.8f, 18f to 46.4f, 24f to 47.6f
        ),
        p50 = listOf(
            0f to 34.5f, 1f to 37.3f, 2f to 39.1f, 3f to 40.5f,
            6f to 42.8f, 9f to 44.5f, 12f to 45.7f, 18f to 47.2f, 24f to 48.3f
        ),
        p85 = listOf(
            0f to 35.8f, 1f to 38.5f, 2f to 40.3f, 3f to 41.6f,
            6f to 43.8f, 9f to 45.4f, 12f to 46.6f, 18f to 48.0f, 24f to 49.0f
        ),
        p97 = listOf(
            0f to 36.9f, 1f to 39.5f, 2f to 41.2f, 3f to 42.4f,
            6f to 44.5f, 9f to 46.0f, 12f to 47.1f, 18f to 48.4f, 24f to 49.3f
        )
    )

    // 女孩头围百分位
    private val girlHeadCircumferencePercentiles = PercentileData(
        p3 = listOf(
            0f to 31.7f, 1f to 34.6f, 2f to 36.5f, 3f to 37.9f,
            6f to 40.1f, 9f to 41.8f, 12f to 43.0f, 18f to 44.6f, 24f to 45.7f
        ),
        p15 = listOf(
            0f to 32.7f, 1f to 35.5f, 2f to 37.3f, 3f to 38.6f,
            6f to 40.8f, 9f to 42.4f, 12f to 43.6f, 18f to 45.1f, 24f to 46.2f
        ),
        p50 = listOf(
            0f to 33.9f, 1f to 36.6f, 2f to 38.3f, 3f to 39.6f,
            6f to 41.7f, 9f to 43.2f, 12f to 44.4f, 18f to 45.8f, 24f to 46.8f
        ),
        p85 = listOf(
            0f to 35.2f, 1f to 37.8f, 2f to 39.4f, 3f to 40.6f,
            6f to 42.6f, 9f to 44.0f, 12f to 45.1f, 18f to 46.5f, 24f to 47.4f
        ),
        p97 = listOf(
            0f to 36.2f, 1f to 38.7f, 2f to 40.3f, 3f to 41.4f,
            6f to 43.3f, 9f to 44.7f, 12f to 45.7f, 18f to 47.0f, 24f to 47.8f
        )
    )
}
