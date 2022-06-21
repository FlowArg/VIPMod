package fr.flowarg.vip3.client.ass;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record ASSConfigSound(String name, float volume) {}
