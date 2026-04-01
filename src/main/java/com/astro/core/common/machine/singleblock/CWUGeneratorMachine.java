package com.astro.core.common.machine.singleblock;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IUIMachine;
import com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CWUGeneratorMachine extends MetaMachine implements IOpticalComputationProvider, IUIMachine {

    private static final int[] CWU_PER_TICK = { 0, 0, 1, 2, 4 };
    private static final int[] LUBE_TANK_MB = { 0, 0, 12000, 16000, 32000 };
    private static final int[] LUBE_PER_CYCLE = { 0, 0, 20, 40, 80 };

    private static final String[] TIER_COLOR = { "§8", "§7", "§b", "§6", "§5", "§9", "§d", "§c", "§3", "§4" };

    public static long getEUtForTier(int tier) {
        return GTValues.VA[tier];
    }

    public static int getCWUForTier(int tier) {
        return tier < CWU_PER_TICK.length ? CWU_PER_TICK[tier] : 0;
    }

    public static int getLubricantForTier(int tier) {
        int perCycle = tier < LUBE_PER_CYCLE.length ? LUBE_PER_CYCLE[tier] : 0;
        return perCycle / 20;
    }

    public static int getTankCapacityForTier(int tier) {
        return tier < LUBE_TANK_MB.length ? LUBE_TANK_MB[tier] : 8000;
    }

    @Override
    public int requestCWUt(int cwut, boolean simulate, @NotNull Collection<IOpticalComputationProvider> seen) {
        if (seen.contains(this)) return 0;
        seen.add(this);
        if (!providerActive) return 0;
        int provided = Math.min(cwut, tickBudget);
        if (!simulate) tickBudget -= provided;
        return provided;
    }

    @Override
    public int getMaxCWUt(@NotNull Collection<IOpticalComputationProvider> seen) {
        if (seen.contains(this)) return 0;
        seen.add(this);
        return providerActive ? cwuPerTick : 0;
    }

    @Override
    public boolean canBridge(@NotNull Collection<IOpticalComputationProvider> seen) {
        return false;
    }

    private static String tierColor(int tier) {
        return tier < TIER_COLOR.length ? TIER_COLOR[tier] : "";
    }

    private final int tier;
    private final int cwuPerTick;
    private final long euPerTick;

    private int tickBudget = 0;

    @Getter
    private boolean providerActive = false;

    @Nullable
    private com.gregtechceu.gtceu.api.machine.TickableSubscription tickSub;

    private final NotifiableEnergyContainer energyContainer;
    private final NotifiableFluidTank lubeTank;

    public CWUGeneratorMachine(IMachineBlockEntity holder, int tier) {
        super(holder);
        this.tier = tier;
        this.cwuPerTick = getCWUForTier(tier);
        this.euPerTick = GTValues.VA[tier];

        this.energyContainer = NotifiableEnergyContainer.receiverContainer(
                this,
                GTValues.V[tier] * 64L,
                GTValues.V[tier],
                2L);

        this.lubeTank = new NotifiableFluidTank(this, 1, getTankCapacityForTier(tier), IO.IN, IO.IN);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            tickSub = subscribeServerTick(this::tickMachine);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickSub != null) {
            tickSub.unsubscribe();
            tickSub = null;
        }
    }

    private void setActive(boolean active) {
        if (this.providerActive == active) return;
        this.providerActive = active;
        if (!isRemote()) {
            setRenderState(getRenderState().setValue(
                    GTMachineModelProperties.RECIPE_LOGIC_STATUS,
                    active ? RecipeLogic.Status.WORKING : RecipeLogic.Status.IDLE));
        }
    }

    private void tickMachine() {
        if (isRemote()) return;

        FluidStack inTank = lubeTank.getFluidInTank(0);
        int lubePerTick = getLubricantForTier(tier);
        boolean hasEnergy = energyContainer.getEnergyStored() >= euPerTick;
        boolean hasLube = !inTank.isEmpty() &&
                inTank.isFluidEqual(new FluidStack(GTMaterials.Lubricant.getFluid(), 1)) &&
                inTank.getAmount() >= lubePerTick;

        if (hasEnergy && hasLube) {
            energyContainer.removeEnergy(euPerTick);
            lubeTank.drainInternal(new FluidStack(inTank.getFluid(), lubePerTick), IFluidHandler.FluidAction.EXECUTE);
            setActive(true);
            tickBudget = cwuPerTick;
        } else {
            setActive(false);
            tickBudget = 0;
        }
    }

    @Override
    public int requestCWUt(int cwut, boolean simulate) {
        if (!providerActive) return 0;
        int provided = Math.min(cwut, tickBudget);
        if (!simulate) tickBudget -= provided;
        return provided;
    }

    @Override
    public int getMaxCWUt() {
        return cwuPerTick;
    }

    @Override
    public ModularUI createUI(Player player) {
        int tankCapacity = getTankCapacityForTier(tier);

        var screen = new DraggableScrollableWidgetGroup(7, 4, 162, 76)
                .setBackground(GuiTextures.DISPLAY);
        screen.addWidget(new LabelWidget(4, 5,
                "Research Computer (" + tierColor(tier) + GTValues.VN[tier] + "§r)"));
        screen.addWidget(new ComponentPanelWidget(4, 17, textList -> {
            int stored = lubeTank.getFluidInTank(0).getAmount();
            textList.add(Component.translatable(
                    "astrogreg.machine.cwu_generator.stored_lubricant", stored, tankCapacity));

            if (providerActive) {
                textList.add(Component.translatable(
                        "astrogreg.machine.cwu_generator.producing", cwuPerTick)
                        .withStyle(ChatFormatting.AQUA));
            } else {
                boolean noEnergy = energyContainer.getEnergyStored() < euPerTick;
                FluidStack tank = lubeTank.getFluidInTank(0);
                boolean noLube = tank.isEmpty() ||
                        !tank.isFluidEqual(new FluidStack(GTMaterials.Lubricant.getFluid(), 1)) || tank.getAmount() < 1;

                if (noEnergy && noLube)
                    textList.add(Component.translatable("astrogreg.machine.cwu_generator.inactive.no_power_lube")
                            .withStyle(ChatFormatting.RED));
                else if (noEnergy)
                    textList.add(Component.translatable("astrogreg.machine.cwu_generator.inactive.no_power")
                            .withStyle(ChatFormatting.RED));
                else if (noLube)
                    textList.add(Component.translatable("astrogreg.machine.cwu_generator.inactive.no_lube")
                            .withStyle(ChatFormatting.RED));
                else
                    textList.add(Component.translatable("gtceu.multiblock.idling")
                            .withStyle(ChatFormatting.GOLD));
            }

            textList.add(Component.translatable(
                    "astrogreg.machine.cwu_generator.lubricant", getLubricantForTier(tier))
                    .withStyle(ChatFormatting.GOLD));
        }).setMaxWidthLimit(154));

        return new ModularUI(176, 166, this, player)
                .background(GuiTextures.BACKGROUND)
                .widget(screen)
                .widget(UITemplate.bindPlayerInventory(player.getInventory(), GuiTextures.SLOT, 7, 84, true));
    }
}
