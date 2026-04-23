package com.astro.core.datagen;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;

import com.google.gson.*;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("all")
public class AstroOreWorldgenProvider implements DataProvider {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeSpecialFloatingPointValues()
            .create();

    private final PackOutput output;
    private final List<DimensionOreGen> dimensions = new ArrayList<>();

    public static void register(GatherDataEvent event) {
        AstroOreWorldgenProvider provider = new AstroOreWorldgenProvider(event.getGenerator().getPackOutput());

        provider.dimension(
                new DimensionOreGen("ad_astra", "kuiper", "kuiper_belt")
                        .heightRange(30, 160)
                        .biome("ad_astra:kuiper_asteroids")
                        .predicateBlock("astrogreg:asteroid_stone")
                        .predicateBlock("astrogreg:hard_asteroid_stone")
                        .ore("gtceu:iron", 10, 60)
                        .ore("gtceu:copper", 8, 50)
                        .ore("astrogreg:desh", 5, 30)
                        .ore("gtceu:redstone", 8, 30)
                        .ore("gtceu:sphalerite", 5, 25)
                        .ore("gtceu:nether_quartz", 8, 25)
                        .ore("gtceu:cassiterite", 6, 20)
                        .ore("gtceu:galena", 5, 20)
                        .ore("gtceu:sulfur", 10, 15)
                        .ore("gtceu:silver", 5, 15)
                        .ore("gtceu:realgar", 4, 15)
                        .ore("gtceu:garnierite", 7, 10)
                        .ore("gtceu:pyrolusite", 6, 10)
                        .ore("gtceu:ruby", 5, 10)
                        .ore("gtceu:sapphire", 6, 10)
                        .ore("gtceu:gold", 7, 5)
                        .ore("gtceu:diamond", 6, 3)
                        .ore("gtceu:apatite", 5, 2)
                        .ore("gtceu:emerald", 4, 2));

        event.getGenerator().addProvider(event.includeServer(), provider);
    }

    public AstroOreWorldgenProvider(PackOutput output) {
        this.output = output;
    }

    public AstroOreWorldgenProvider dimension(DimensionOreGen dim) {
        dimensions.add(dim);
        return this;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (DimensionOreGen dim : dimensions) {
            dim.validate();
            for (OreEntry ore : dim.ores) {
                futures.add(DataProvider.saveStable(cache, dim.buildConfiguredFeature(ore),
                        dim.configuredFeaturePath(output, ore)));
                futures.add(DataProvider.saveStable(cache, dim.buildPlacedFeature(ore),
                        dim.placedFeaturePath(output, ore)));
            }
            if (!dim.biomes.isEmpty()) {
                futures.add(DataProvider.saveStable(cache, dim.buildBiomeModifier(), dim.biomeModifierPath(output)));
            }
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public String getName() {
        return "AstroOreWorldgenProvider";
    }

    public static class DimensionOreGen {

        final String modId;
        final String prefix;
        final String dimensionId;

        int minHeight = 0;
        int maxHeight = 256;

        final List<String> biomes = new ArrayList<>();
        final List<PredicateBlock> predicateBlocks = new ArrayList<>();
        final List<OreEntry> ores = new ArrayList<>();

        public DimensionOreGen(String modId, String prefix, String dimensionId) {
            this.modId = modId;
            this.prefix = prefix;
            this.dimensionId = dimensionId;
        }

        public DimensionOreGen heightRange(int min, int max) {
            this.minHeight = min;
            this.maxHeight = max;
            return this;
        }

        public DimensionOreGen biome(String biomeId) {
            biomes.add(biomeId);
            return this;
        }

        public DimensionOreGen predicateBlock(String blockId) {
            predicateBlocks.add(new PredicateBlock(blockId));
            return this;
        }

        public DimensionOreGen ore(String fullname, int size, int count) {
            String[] splitname = fullname.split(":");
            ores.add(new OreEntry(splitname[0], splitname[1], size, count));
            return this;
        }

        void validate() {
            if (predicateBlocks.isEmpty())
                throw new IllegalStateException(
                        "[AstroOreWorldgenProvider] No predicate blocks for dimension: " + dimensionId);
            if (ores.isEmpty())
                throw new IllegalStateException(
                        "[AstroOreWorldgenProvider] No ores registered for dimension: " + dimensionId);
        }

        private String featureId(OreEntry ore) {
            return prefix + "_" + ore.name + "_ore";
        }

        private String featureRef(OreEntry ore) {
            return modId + ":" + featureId(ore);
        }

        Path configuredFeaturePath(PackOutput output, OreEntry ore) {
            return output.getOutputFolder()
                    .resolve("data")
                    .resolve(modId)
                    .resolve("worldgen")
                    .resolve("configured_feature")
                    .resolve(featureId(ore) + ".json");
        }

        Path placedFeaturePath(PackOutput output, OreEntry ore) {
            return output.getOutputFolder()
                    .resolve("data")
                    .resolve(modId)
                    .resolve("worldgen")
                    .resolve("placed_feature")
                    .resolve(featureId(ore) + ".json");
        }

        Path biomeModifierPath(PackOutput output) {
            return output.getOutputFolder()
                    .resolve("data")
                    .resolve(modId)
                    .resolve("forge")
                    .resolve("biome_modifier")
                    .resolve(prefix + "_ores.json");
        }

        JsonObject buildConfiguredFeature(OreEntry ore) {
            JsonObject root = new JsonObject();
            root.addProperty("type", "minecraft:ore");

            JsonObject config = new JsonObject();
            config.addProperty("size", ore.size);
            config.addProperty("discard_chance_on_air_exposure", 0.0);

            JsonArray targets = new JsonArray();
            for (PredicateBlock pb : predicateBlocks) {
                JsonObject entry = new JsonObject();

                JsonObject predicate = new JsonObject();
                predicate.addProperty("predicate_type", "minecraft:block_match");
                predicate.addProperty("block", pb.blockId);
                entry.add("target", predicate);

                String blockPath = pb.blockId.substring(pb.blockId.indexOf(':') + 1);
                JsonObject state = new JsonObject();

                state.addProperty("Name", buildStateName(ore.stateNamespace, blockPath, ore.name));
                entry.add("state", state);

                targets.add(entry);
            }

            config.add("targets", targets);
            root.add("config", config);
            return root;
        }

        JsonObject buildPlacedFeature(OreEntry ore) {
            JsonObject root = new JsonObject();
            root.addProperty("feature", featureRef(ore));

            JsonArray placement = new JsonArray();

            JsonObject count = new JsonObject();
            count.addProperty("type", "minecraft:count");
            count.addProperty("count", ore.count);
            placement.add(count);

            JsonObject inSquare = new JsonObject();
            inSquare.addProperty("type", "minecraft:in_square");
            placement.add(inSquare);

            JsonObject heightRange = new JsonObject();
            heightRange.addProperty("type", "minecraft:height_range");
            JsonObject height = new JsonObject();
            height.addProperty("type", "minecraft:uniform");
            JsonObject minIncl = new JsonObject();
            minIncl.addProperty("absolute", minHeight);
            JsonObject maxIncl = new JsonObject();
            maxIncl.addProperty("absolute", maxHeight);
            height.add("min_inclusive", minIncl);
            height.add("max_inclusive", maxIncl);
            heightRange.add("height", height);
            placement.add(heightRange);

            JsonObject biome = new JsonObject();
            biome.addProperty("type", "minecraft:biome");
            placement.add(biome);

            root.add("placement", placement);
            return root;
        }

        JsonObject buildBiomeModifier() {
            JsonObject root = new JsonObject();
            root.addProperty("type", "forge:add_features");

            if (biomes.size() == 1) {
                root.addProperty("biomes", biomes.get(0));
            } else {
                JsonObject biomeTag = new JsonObject();
                biomeTag.addProperty("type", "minecraft:any_of");
                JsonArray biomeValues = new JsonArray();
                for (String b : biomes) {
                    JsonObject entry = new JsonObject();
                    entry.addProperty("type", "minecraft:resource_location");
                    entry.addProperty("id", b);
                    biomeValues.add(entry);
                }
                biomeTag.add("values", biomeValues);
                root.add("biomes", biomeTag);
            }

            JsonArray features = new JsonArray();
            for (OreEntry ore : ores) {
                features.add(featureRef(ore));
            }
            root.add("features", features);

            root.addProperty("step", "underground_ores");

            return root;
        }

        protected String buildStateName(String stateNamespace, String blockPath, String oreName) {
            return stateNamespace + ":" + blockPath + "_" + oreName + "_ore";
        }
    }

    record PredicateBlock(String blockId) {}

    record OreEntry(String stateNamespace, String name, int size, int count) {}
}
