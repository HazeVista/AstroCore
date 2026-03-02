package com.astro.core.common.machine.singleblock;

import com.astro.core.common.machine.trait.cwu.ILocalCWUProvider;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IUIMachine;
import com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import lombok.Getter;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CWUGeneratorMachine extends MetaMachine implements ILocalCWUProvider, IUIMachine {

    private static final int[] CWU_PER_TICK   = { 0, 0,  1, 2, 4 };
    private static final int[] LUBE_PER_CYCLE = { 0, 0, 20, 80, 160 };
    private static final int[] LUBE_TANK_MB   = { 0, 0, 12000, 16000, 32000 };

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

    private static String tierColor(int tier) {
        return tier < TIER_COLOR.length ? TIER_COLOR[tier] : "";
    }

    private final int  tier;
    private final int  cwuPerTick;
    private final int  lubePerCycle;
    private final long euPerTick;

    private int tickBudget = 0;

    @Getter
    private boolean providerActive = false;

    @Nullable
    private TickableSubscription tickSub;

    private final NotifiableEnergyContainer energyContainer;
    private final NotifiableFluidTank       lubeTank;

    public CWUGeneratorMachine(IMachineBlockEntity holder, int tier) {
        super(holder);
        this.tier         = tier;
        this.cwuPerTick   = getCWUForTier(tier);
        this.lubePerCycle = tier < LUBE_PER_CYCLE.length ? LUBE_PER_CYCLE[tier] : 0;
        this.euPerTick    = GTValues.VA[tier];

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

        tickBudget = providerActive ? cwuPerTick : 0;

        if (getOffsetTimer() % 20 == 0) {
            boolean hasEnergy = energyContainer.getEnergyStored() >= euPerTick * 20L;

            FluidStack inTank = lubeTank.getFluidInTank(0);
            boolean hasLube = !inTank.isEmpty()
                    && inTank.getFluid() == com.gregtechceu.gtceu.common.data.GTMaterials.Lubricant.getFluid()
                    && inTank.getAmount() >= lubePerCycle;

            if (hasEnergy && hasLube) {
                energyContainer.removeEnergy(euPerTick * 20L);
                lubeTank.drain(lubePerCycle, IFluidHandler.FluidAction.EXECUTE);
                setActive(true);
                tickBudget = cwuPerTick;
            } else {
                setActive(false);
                tickBudget = 0;
            }
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
        String color = tierColor(tier);

        return new ModularUI(176, 166, this, player)
                .background(GuiTextures.BACKGROUND)
                .widget(new LabelWidget(8, 8,
                        "Research Computer (" + color + GTValues.VN[tier] + "§r)"))
                .widget(new LabelWidget(8, 20, () -> {
                    int stored = lubeTank.getFluidInTank(0).getAmount();
                    return Component.translatable(
                            "astrogreg.machine.cwu_generator.stored_lubricant", stored, tankCapacity).getString();
                }))
                .widget(new LabelWidget(8, 32, () -> {
                    if (providerActive) {
                        return Component.translatable(
                                "astrogreg.machine.cwu_generator.producing", cwuPerTick).getString();
                    } else {
                        boolean noEnergy = energyContainer.getEnergyStored() < euPerTick * 20L;
                        FluidStack tank = lubeTank.getFluidInTank(0);
                        boolean noLube = tank.isEmpty()
                                || tank.getFluid() != com.gregtechceu.gtceu.common.data.GTMaterials.Lubricant.getFluid()
                                || tank.getAmount() < lubePerCycle;
                        if (noEnergy && noLube)
                            return Component.translatable("astrogreg.machine.cwu_generator.inactive.no_power_lube").getString();
                        if (noEnergy)
                            return Component.translatable("astrogreg.machine.cwu_generator.inactive.no_power").getString();
                        if (noLube)
                            return Component.translatable("astrogreg.machine.cwu_generator.inactive.no_lube").getString();
                        return Component.translatable("gtceu.multiblock.idling").getString();
                    }
                }))
                .widget(new LabelWidget(8, 44,
                        Component.translatable(
                                "astrogreg.machine.cwu_generator.lubricant",
                                getLubricantForTier(tier)).getString()))
                .widget(UITemplate.bindPlayerInventory(player.getInventory(), GuiTextures.SLOT, 8, 84, true));
    }
}