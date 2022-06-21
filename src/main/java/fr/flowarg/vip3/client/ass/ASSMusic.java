package fr.flowarg.vip3.client.ass;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;

public class ASSMusic extends Music
{
    private final ResourceLocation soundPath;

    public ASSMusic(ResourceLocation soundPath, SoundEvent event, boolean replaceCurrentMusic)
    {
        super(event, 20, 160, replaceCurrentMusic);
        this.soundPath = soundPath;
    }

    public ResourceLocation getSoundPath()
    {
        return this.soundPath;
    }
}
