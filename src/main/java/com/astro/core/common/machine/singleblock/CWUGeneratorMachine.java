package com.astro.core.common.machine.singleblock;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CWUGeneratorMachine extends TieredEnergyMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            CWUGeneratorMachine.class, TieredEnergyMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private final NotifiableFluidTank lubricantTank;

    private final int cwuPerTick;
    private final long lubricantPerTick;
    private final long euPerTick;

    @Persisted
    private int currentCWU = 0;

    private TickableSubscription subscription;

    public CWUGeneratorMachine(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier);
        this.cwuPerTick = getCWUForTier(tier);
        this.lubricantPerTick = getLubricantForTier(tier);
        this.euPerTick = getEUtForTier(tier);
        this.lubricantTank = createLubricantTank();
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private NotifiableFluidTank createLubricantTank() {
        return new NotifiableFluidTank(this, 1, 16000, IO.IN);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            lubricantTank.setFilter(fluid -> fluid.getFluid().isSame(GTMaterials.Lubricant.getFluid()));
            subscription = subscribeServerTick(subscription, this::update);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    public void update() {
        if (getOffsetTimer() % 20 == 0) {
            if (energyContainer.getEnergyStored() >= euPerTick &&
                    lubricantTank.getFluidInTank(0).getAmount() >= lubricantPerTick) {

                energyContainer.removeEnergy(euPerTick);
                lubricantTank.drain((int) lubricantPerTick, IFluidHandler.FluidAction.EXECUTE);
                currentCWU = cwuPerTick;
            } else {
                currentCWU = 0;
            }
        }
    }

    public int requestCWU(int amount) {
        if (currentCWU >= amount) {
            currentCWU -= amount;
            return amount;
        }
        int available = currentCWU;
        currentCWU = 0;
        return available;
    }

    public int getAvailableCWU() {
        return currentCWU;
    }

    public static int getCWUForTier(int tier) {
        return (int) Math.pow(2, tier - 2);
    }

    public static long getLubricantForTier(int tier) {
        return (long) (1 * Math.pow(4, tier - 2));
    }

    public static long getEUtForTier(int tier) {
        return (long) (64 * Math.pow(4, tier - 2));
    }
}
