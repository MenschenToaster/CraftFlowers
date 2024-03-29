package cm.ptks.craftflowers.flower;

import cm.ptks.craftflowers.languages.Messages;
import com.google.gson.JsonObject;
import com.sk89q.worldedit.registry.state.Property;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockType;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class AgingFlower extends Flower {

    protected final int age;

    public AgingFlower(Material guiMaterial, String displayName, Material blockMaterial, int age) {
        super(guiMaterial, displayName, blockMaterial);
        this.age = age;
    }

    public AgingFlower(Material material, String displayName, int age) {
        super(material, displayName);
        this.age = age;
    }

    @Override
    public BaseBlock applyToBlock(BaseBlock block, BlockType type) {
        Property<Integer> ageProp = type.getProperty("age");
        if(ageProp != null)
            block = block.with(ageProp, age);

        return super.applyToBlock(block, type);
    }

    @Override
    public String getDisplayName(@Nullable Player player) {
        return super.getDisplayName(player) + Messages.getFlowerInfoAge(player, age);
    }

    @Override
    public JsonObject serialize() {
        JsonObject serialize = super.serialize();
        serialize.addProperty("age", age);
        return serialize;
    }

    public int getAge() {
        return age;
    }
}