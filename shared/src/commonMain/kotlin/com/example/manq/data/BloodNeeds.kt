package com.example.manq.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Повна група крові: AB0 разом з резус-фактором. */
@Serializable
enum class BloodType(val title: String, val group: BloodGroup, val rh: RhFactor) {
    @SerialName("O_POS")
    O_POS("0 (I) Rh+", BloodGroup.O, RhFactor.POSITIVE),

    @SerialName("O_NEG")
    O_NEG("0 (I) Rh−", BloodGroup.O, RhFactor.NEGATIVE),

    @SerialName("A_POS")
    A_POS("A (II) Rh+", BloodGroup.A, RhFactor.POSITIVE),

    @SerialName("A_NEG")
    A_NEG("A (II) Rh−", BloodGroup.A, RhFactor.NEGATIVE),

    @SerialName("B_POS")
    B_POS("B (III) Rh+", BloodGroup.B, RhFactor.POSITIVE),

    @SerialName("B_NEG")
    B_NEG("B (III) Rh−", BloodGroup.B, RhFactor.NEGATIVE),

    @SerialName("AB_POS")
    AB_POS("AB (IV) Rh+", BloodGroup.AB, RhFactor.POSITIVE),

    @SerialName("AB_NEG")
    AB_NEG("AB (IV) Rh−", BloodGroup.AB, RhFactor.NEGATIVE),
    ;

    companion object {
        fun of(group: BloodGroup, rh: RhFactor): BloodType? = entries.firstOrNull { it.group == group && it.rh == rh }
    }
}

@Serializable
enum class NeedLevel(val title: String) {
    @SerialName("HIGH")
    HIGH("Висока потреба"),

    @SerialName("MEDIUM")
    MEDIUM("Середній рівень"),

    @SerialName("LOW")
    LOW("Достатній рівень"),
}

@Serializable
data class CenterNeeds(
    val centerId: String,
    val needs: Map<BloodType, NeedLevel>,
)

/**
 * Зріз потреби у крові по центрах. [generatedAt] показується
 * користувачу: дані не живі, а зібрані в конкретний день.
 */
@Serializable
data class NeedsSnapshot(
    val version: Int = 1,
    val generatedAt: String = "",
    val source: String = "",
    val centers: List<CenterNeeds> = emptyList(),
) {
    private val byCenter: Map<String, CenterNeeds> by lazy { centers.associateBy { it.centerId } }

    fun forCenter(centerId: String): CenterNeeds? = byCenter[centerId]

    fun hasHighNeed(centerId: String): Boolean = forCenter(centerId)?.needs?.values?.any { it == NeedLevel.HIGH } == true

    /** Центри, де гостро потрібна саме ця група крові. */
    fun centersNeeding(type: BloodType): List<String> = centers.filter { it.needs[type] == NeedLevel.HIGH }.map { it.centerId }

    val date: SimpleDate? get() = SimpleDate.parse(generatedAt)

    /** Дані старші за два тижні показуємо з поміткою. */
    fun isStale(now: SimpleDate = today()): Boolean {
        val day = date ?: return true
        return day.daysUntil(now) > 14
    }
}
