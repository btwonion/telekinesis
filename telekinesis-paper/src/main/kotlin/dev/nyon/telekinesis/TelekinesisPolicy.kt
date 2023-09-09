package dev.nyon.telekinesis

enum class TelekinesisPolicy {
    BlockDrops,
    ExpDrops,
    EntityDrops;

    fun isEnabled(): Boolean = when (this) {
        BlockDrops -> config.blockDrops
        ExpDrops -> config.expDrops
        EntityDrops -> config.entityDrops
    }

    fun associatedPermission(): String? = when (this) {
        BlockDrops -> config.blockDropsPermissionRequirement
        ExpDrops -> config.expDropsPermissionRequirement
        EntityDrops -> config.entityDropsPermissionRequirement
    }
}