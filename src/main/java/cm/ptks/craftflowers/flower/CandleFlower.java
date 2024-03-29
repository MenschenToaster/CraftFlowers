package cm.ptks.craftflowers.flower;

import cm.ptks.craftflowers.languages.I18n;
import cm.ptks.craftflowers.languages.Messages;
import com.google.gson.JsonObject;
import com.sk89q.worldedit.registry.state.Property;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class CandleFlower extends Flower {

    private final boolean lit;

    public CandleFlower(Material guiMaterial, String displayName, boolean lit) {
        super(guiMaterial, displayName);
        this.lit = lit;
    }

    @Override
    public String getDisplayName(@Nullable Player player) {
        return super.getDisplayName(player) + (isLit() ? I18n.translate(player, Messages.FLOWER.CANDLE_LIT_INDICATOR) : "");
    }

    @Override
    public BaseBlock applyToBlock(BaseBlock block, BlockType type) {
        Property<Boolean> litProp = block.getBlockType().getProperty("lit");
        return super.applyToBlock(block, type).with(litProp, this.lit);
    }

    @Override
    public JsonObject serialize() {
        JsonObject serialize = super.serialize();
        serialize.addProperty("lit", this.lit);
        return serialize;
    }

    public boolean isLit() {
        return lit;
    }
}