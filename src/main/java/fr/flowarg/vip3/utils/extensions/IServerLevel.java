package fr.flowarg.vip3.utils.extensions;

import fr.flowarg.vip3.utils.CalledAtRuntime;
import fr.flowarg.vip3.utils.wire.WireHandler;

@CalledAtRuntime
public interface IServerLevel {
	@CalledAtRuntime
	WireHandler getWireHandler();
}
