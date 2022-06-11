package fr.flowarg.vip3.client.ass;

import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;

public class ASSMusic extends Music
{
    public ASSMusic(SoundEvent event, int minDelay, int maxDelay, boolean replaceCurrentMusic)
    {
        super(event, minDelay, maxDelay, replaceCurrentMusic);
    }
}
