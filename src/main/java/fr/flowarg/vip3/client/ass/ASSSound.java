package fr.flowarg.vip3.client.ass;

import fr.flowarg.vip3.VIP3;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public final class ASSSound implements SoundInstance
{
    private final boolean shouldLoopAutomatically;
    private final boolean stream;
    private final SoundSource source;
    private final int x, y, z;
    private final boolean music;
    private final SoundEvent soundEvent;
    private final ResourceLocation soundPath;
    private final Sound sound;
    private float volume;

    public ASSSound(boolean music, SoundEvent soundEvent, String soundName, boolean shouldLoopAutomatically, boolean stream, SoundSource soundSource, int x, int y, int z)
    {
        this(music, soundEvent, new ResourceLocation(VIP3.MOD_ID, soundName), shouldLoopAutomatically, stream, soundSource, x, y, z);
    }

    public ASSSound(boolean music, SoundEvent soundEvent, ResourceLocation soundPath, boolean shouldLoopAutomatically, boolean stream, SoundSource soundSource, int x, int y, int z)
    {
        this.music = music;
        this.volume = this.music ? 1.0f : 0.7f;
        this.shouldLoopAutomatically = shouldLoopAutomatically;
        this.stream = stream;
        this.source = soundSource;
        this.x = x;
        this.y = y;
        this.z = z;
        this.soundPath = soundPath;
        this.soundEvent = soundEvent;
        this.sound = new Sound(this.soundPath.toString(), this.getVolume(), this.getPitch(), 1, Sound.Type.FILE, stream, false, 16);
    }

    @Override
    public @NotNull ResourceLocation getLocation()
    {
        return this.soundEvent.getLocation();
    }

    public SoundEvent getSoundEvent()
    {
        return this.soundEvent;
    }

    public ResourceLocation getSoundPath()
    {
        return this.soundPath;
    }

    @Nullable
    @Override
    public WeighedSoundEvents resolve(@NotNull SoundManager pManager)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Sound getSound()
    {
        return this.sound;
    }

    @Override
    public @NotNull SoundSource getSource()
    {
        return this.source;
    }

    @Override
    public boolean isLooping()
    {
        return this.shouldLoopAutomatically;
    }

    @Override
    public boolean isRelative()
    {
        return false;
    }

    @Override
    public int getDelay()
    {
        return 0;
    }

    @Override
    public float getVolume()
    {
        return this.volume;
    }

    public void setVolume(float volume)
    {
        this.volume = volume;
    }

    public boolean isMusic()
    {
        return this.music;
    }

    @Override
    public float getPitch()
    {
        return 1f;
    }

    @Override
    public double getX()
    {
        return this.x;
    }

    @Override
    public double getY()
    {
        return this.y;
    }

    @Override
    public double getZ()
    {
        return this.z;
    }

    @Override
    public @NotNull Attenuation getAttenuation()
    {
        return Attenuation.LINEAR;
    }

    public boolean shouldLoopAutomatically()
    {
        return this.shouldLoopAutomatically;
    }

    public boolean stream()
    {
        return this.stream;
    }
}
