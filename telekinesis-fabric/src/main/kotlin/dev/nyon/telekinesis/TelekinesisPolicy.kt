package dev.nyon.telekinesis

enum class TelekinesisPolicy {
    BlockDrops,
    ExpDrops,
    MobDrops,
    EntityDrops,
    ShearingDrops,
    FishingDrops;

    fun isEnabled(): Boolean = when (this) {
        BlockDrops -> config.blockDrops
        ExpDrops -> config.expDrops
        MobDrops -> config.mobDrops
        EntityDrops -> config.entityDrops
        ShearingDrops -> config.shearingDrops
        FishingDrops -> config.fishingDrops
    }
}