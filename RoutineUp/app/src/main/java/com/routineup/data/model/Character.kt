package com.routineup.data.model

data class Character(
    val id: Long = 0,
    val level: Int = 1,
    val exp: Int = 0,
    val maxExp: Int = 100,
    val characterType: CharacterType = CharacterType.BEGINNER,
    val consecutiveDays: Int = 0,
    val totalCompletedRoutines: Int = 0,
    val badges: List<Badge> = emptyList()
)

enum class CharacterType(val displayName: String) {
    BEGINNER("초보자"),
    SAVER("알뜰족"),
    ATHLETE("운동가"),
    READER("독서가"),
    MEDITATOR("명상가"),
    EARLY_BIRD("얼리버드"),
    NIGHT_OWL("나이트 오울"),
    SUPER_RICH("슈퍼리치")
}
