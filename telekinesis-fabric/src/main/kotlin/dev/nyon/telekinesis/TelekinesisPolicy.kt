package dev.nyon.telekinesis

enum class TelekinesisPolicy {
    BlockDrops,
    ExpDrops,
    MobDrops,
    VehicleDrops,
    ShearingDrops,
    FishingDrops;

    fun isEnabled(): Boolean =
        when (this) {
            BlockDrops -> config.blockDrops
            ExpDrops -> config.expDrops
            MobDrops -> config.mobDrops
            VehicleDrops -> config.vehicleDrops
            ShearingDrops -> config.shearingDrops
            FishingDrops -> config.fishingDrops
        }
}
