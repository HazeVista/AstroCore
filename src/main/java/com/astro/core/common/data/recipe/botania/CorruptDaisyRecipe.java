package com.astro.core.common.data.recipe.botania;

import com.google.gson.JsonObject;
import net.minecraft.commands.CommandFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.api.recipe.PureDaisyRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.StateIngredientHelper;

import com.astro.core.AstroCore;
import com.astro.core.common.data.recipe.AstroRecipeTypes;

@SuppressWarnings("all")
public class CorruptDaisyRecipe implements PureDaisyRecipe {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AstroCore.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CorruptDaisyRecipe>> SERIALIZER =
            SERIALIZERS.register("corrupt_daisy", Serializer::new);

    private final ResourceLocation id;
    private final StateIngredient input;
    private final BlockState outputState;
    private final int time;

    public CorruptDaisyRecipe(ResourceLocation id, StateIngredient input,
                              BlockState outputState, int time) {
        this.id = id;
        this.input = input;
        this.outputState = outputState;
        this.time = time;
    }

    @Override
    public boolean matches(@NotNull Level world, @NotNull BlockPos pos,
                           @NotNull SpecialFlowerBlockEntity flower, @NotNull BlockState state) {
        return input.test(state);
    }

    @Override
    public boolean set(@NotNull Level world, @NotNull BlockPos pos,
                       @NotNull SpecialFlowerBlockEntity flower) {
        if (!world.isClientSide) {
            world.setBlockAndUpdate(pos, outputState);
        }
        return true;
    }

    @Override
    @NotNull
    public StateIngredient getInput() { return input; }

    @Override
    @NotNull
    public BlockState getOutputState() { return outputState; }

    @Override
    @NotNull
    public CommandFunction.CacheableFunction getSuccessFunction() {
        return CommandFunction.CacheableFunction.NONE;
    }

    @Override
    public int getTime() { return time; }

    @Override
    @NotNull
    public ResourceLocation getId() { return id; }

    @Override
    @NotNull
    public RecipeSerializer<?> getSerializer() { return SERIALIZER.get(); }

    @Override
    @NotNull
    public RecipeType<?> getType() {
        return AstroRecipeTypes.CORRUPT_DAISY_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<CorruptDaisyRecipe> {

        @Override
        @NotNull
        public CorruptDaisyRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            StateIngredient input = StateIngredientHelper.deserialize(json.getAsJsonObject("input"));
            BlockState output = StateIngredientHelper.readBlockState(json.getAsJsonObject("output"));
            int time = json.has("time") ? json.get("time").getAsInt() : 200;
            return new CorruptDaisyRecipe(id, input, output, time);
        }

        @Override
        public CorruptDaisyRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
            StateIngredient input = StateIngredientHelper.read(buf);
            BlockState output = Block.stateById(buf.readVarInt());
            int time = buf.readVarInt();
            return new CorruptDaisyRecipe(id, input, output, time);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull CorruptDaisyRecipe recipe) {
            recipe.input.write(buf);
            buf.writeVarInt(Block.getId(recipe.outputState));
            buf.writeVarInt(recipe.time);
        }
    }

    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
    }
}