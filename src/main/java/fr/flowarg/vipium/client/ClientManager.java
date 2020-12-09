package fr.flowarg.vipium.client;

import net.minecraft.client.Minecraft;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class ClientManager
{
    private final RPCManager rpcManager;
    private final Predicate<Supplier<Supplier<Minecraft>>> startTask;

    public ClientManager()
    {
        this.rpcManager = new RPCManager();
        this.startTask = supplier -> this.rpcManager.startRpc(supplier.get());
    }

    public RPCManager getRpcManager()
    {
        return this.rpcManager;
    }

    public Predicate<Supplier<Supplier<Minecraft>>> getStartTask()
    {
        return this.startTask;
    }
}
