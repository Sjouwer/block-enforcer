package io.github.sjouwer.blockenforcer.handlers;

import io.github.sjouwer.blockenforcer.utils.BlockUtil;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Bamboo.Leaves;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlantHandler {
    private PlantHandler() {
    }

    public static void forcePlaceDoublePlant(BlockData blockData, NBTTagCompound blockStateTag, PlayerInteractEvent event) {
        if (!(blockData instanceof Bisected)) return;

        Bisected plantHalf = (Bisected) blockData;

        if (blockStateTag != null) {
            String half = blockStateTag.getString("half");
            if (!half.isEmpty()) plantHalf.setHalf(stringToHalf(half));
        }
        else {
            plantHalf.setHalf(Bisected.Half.BOTTOM);
        }

        Block placedBlock = BlockPlaceHandler.placeBlock(event, plantHalf);
        if (placedBlock == null) return;

        event.setCancelled(true);

        Block plantTopBlock = placedBlock.getRelative(BlockFace.UP, 1);
        if (blockStateTag == null && BlockUtil.isReplaceable(plantTopBlock)) {
            plantHalf.setHalf(Bisected.Half.TOP);
            BlockPlaceHandler.placeBlock(event, plantHalf, plantTopBlock);
        }

        plantTopBlock.getState().update(true, false);
    }

    public static Bisected.Half stringToHalf(String string) {
        if (string.equalsIgnoreCase("UPPER") || string.equalsIgnoreCase("TOP")) {
            return Bisected.Half.TOP;
        }
        return Bisected.Half.BOTTOM;
    }

    public static void forcePlaceAgingPlant(BlockData blockData, NBTTagCompound blockStateTag, PlayerInteractEvent event) {
        if (!(blockData instanceof Ageable) || event.getItem().getType() == Material.WHEAT) return;

        Ageable plant = (Ageable) blockData;

        if (blockStateTag != null){
            String age = blockStateTag.getString("age");
            if (!age.isEmpty()) plant.setAge(Integer.parseInt(age));
        }

        Block placedBlock = BlockPlaceHandler.placeBlock(event, plant);
        if (placedBlock == null) return;

        event.setCancelled(true);
    }

    public static void forcePlaceSapling(BlockData blockData, NBTTagCompound blockStateTag, PlayerInteractEvent event) {
        if (!(blockData instanceof Sapling)) return;

        Sapling sapling = (Sapling) blockData;

        if (blockStateTag != null) {
            String stage = blockStateTag.getString("stage");
            if (!stage.isEmpty()) sapling.setStage(Integer.parseInt(stage));
        }

        Block placedBlock = BlockPlaceHandler.placeBlock(event, sapling);
        if (placedBlock == null) return;

        event.setCancelled(true);
    }

    public static void forcePlaceBamboo(BlockData blockData, NBTTagCompound blockStateTag, PlayerInteractEvent event) {
        if (!(blockData instanceof Bamboo)) return;

        Bamboo bamboo = (Bamboo) blockData;

        if (blockStateTag != null) {
            String age = blockStateTag.getString("age");
            if (!age.isEmpty()) bamboo.setAge(Integer.parseInt(age));

            String stage = blockStateTag.getString("stage");
            if (!stage.isEmpty()) bamboo.setStage(Integer.parseInt(stage));

            String leaves = blockStateTag.getString("leaves").toUpperCase();
            if (!leaves.isEmpty()) bamboo.setLeaves(Leaves.valueOf(leaves));
        }

        Block placedBlock = BlockPlaceHandler.placeBlock(event, bamboo);
        if (placedBlock == null) return;

        event.setCancelled(true);
    }
}
