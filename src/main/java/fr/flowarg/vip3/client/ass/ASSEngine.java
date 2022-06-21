package fr.flowarg.vip3.client.ass;

import com.mojang.blaze3d.audio.Library;
import com.mojang.logging.LogUtils;
import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.client.ClientEventHandler;
import fr.flowarg.vip3.utils.CalledAtRuntime;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ASSEngine extends SoundEngine
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Random RANDOM = new Random();
    public static final Map<String, ASSMusic> MUSIC_MAP = new HashMap<>();
    public static final Map<String, ASSSound> SOUND_MUSIC_MAP = new HashMap<>();
    private final ASSSoundBufferLibrary soundBuffers;

    static ASSEngine unsafeInstance;

    @CalledAtRuntime
    public ASSEngine(SoundManager soundManager, Options options, ResourceManager resourceManager)
    {
        super(soundManager, options, resourceManager);
        this.soundBuffers = new ASSSoundBufferLibrary();
        LOGGER.info("Loaded ASS Engine!");
        unsafeInstance = this;
    }

    @Override
    public void play(@NotNull SoundInstance soundInstance)
    {
        if(!(soundInstance instanceof ASSSound assSound))
        {
            super.play(soundInstance);
            return;
        }

        if (!this.loaded) return;

        if(assSound.isMusic() && ClientEventHandler.musicState)
            return;

        this.calculateVolume(assSound);

        if(assSound.getVolume() == 0)
        {
            LOGGER.debug("Skipped playing soundEvent: {}, volume was zero", assSound.getSoundPath().getPath());
            return;
        }

        if (this.listener.getGain() <= 0.0F)
        {
            LOGGER.debug("Skipped playing soundEvent: {}, master volume was zero", assSound.getSoundPath().getPath());
            return;
        }

        final var shouldStream = assSound.stream();
        final var channelHandle = this.channelAccess.createHandle(shouldStream ? Library.Pool.STREAMING : Library.Pool.STATIC).join();

        if (channelHandle == null)
        {
            if (SharedConstants.IS_RUNNING_IN_IDE)
                LOGGER.warn("Failed to create new sound handle");
            return;
        }

        this.soundDeleteTime.put(assSound, this.tickCount + 20);
        this.instanceToChannel.put(assSound, channelHandle);
        this.instanceBySource.put(assSound.getSource(), assSound);

        final var shouldLoopAutomatically = assSound.shouldLoopAutomatically();

        channelHandle.execute((channel) -> {
            channel.setPitch(assSound.getPitch());
            channel.setVolume(assSound.getVolume());
            if (assSound.getAttenuation() == SoundInstance.Attenuation.LINEAR) {
                channel.linearAttenuation(16f);
            } else {
                channel.disableAttenuation();
            }

            channel.setLooping(shouldLoopAutomatically && !shouldStream);
            channel.setSelfPosition(new Vec3(assSound.getX(), assSound.getY(), assSound.getZ()));
            channel.setRelative(assSound.isRelative());
        });

        if (shouldStream)
        {
            this.soundBuffers.getStream(assSound.getSoundPath(), shouldLoopAutomatically).thenAccept((stream) -> channelHandle.execute((channel) -> {
                channel.attachBufferStream(stream);
                channel.play();
            }));
            return;
        }

        this.soundBuffers.getCompleteBuffer(assSound.getSoundPath()).thenAccept((soundBuffer) -> channelHandle.execute((channel) -> {
            channel.attachStaticBuffer(soundBuffer);
            channel.play();
        }));
    }

    private float calculateVolume(ASSSound assSound)
    {
        if(assSound.isMusic())
        {
            float volume = 1.0f;
            final var path = assSound.getSoundPath().getPath();

            if(assSound.getSoundEvent() == SoundEvents.MUSIC_MENU && ASSConfig.CONFIG.getMenuMusics().containsKey(path))
                volume = ASSConfig.CONFIG.getMenuMusics().get(path).volume();
            else if(assSound.getSoundEvent() == SoundEvents.MUSIC_GAME && ASSConfig.CONFIG.getGameMusics().containsKey(path))
                volume = ASSConfig.CONFIG.getGameMusics().get(path).volume();

            assSound.setVolume(volume);
        }
        return assSound.getVolume();
    }

    @Override
    public void destroy()
    {
        super.destroy();
        if(this.loaded)
        {
            SOUND_MUSIC_MAP.clear();
            MUSIC_MAP.clear();
        }
    }

    @CalledAtRuntime
    public static Music getSituationalMusic()
    {
        final Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.screen instanceof WinScreen)
            return Musics.CREDITS;

        if (minecraft.player == null)
            return getMenuMusic();

        if (minecraft.player.level.dimension() == Level.END)
            return minecraft.gui.getBossOverlay().shouldPlayMusic() ? Musics.END_BOSS : Musics.END;

        Holder<Biome> holder = minecraft.player.level.getBiome(minecraft.player.blockPosition());
        final var biomeCategory = holder.value().getBiomeCategory();
        if (!minecraft.getMusicManager().isPlayingMusic(Musics.UNDER_WATER) && (!minecraft.player.isUnderWater() || biomeCategory != Biome.BiomeCategory.OCEAN && biomeCategory != Biome.BiomeCategory.RIVER))
        {
            if(minecraft.player.level.dimension() != Level.NETHER && minecraft.player.getAbilities().instabuild && minecraft.player.getAbilities().mayfly)
                return getGameMusic();

            return holder.value().getBackgroundMusic().orElse(getGameMusic());
        }

        return Musics.UNDER_WATER;
    }

    private static Music getMenuMusic()
    {
        if(!ClientEventHandler.SOUND_FILE_ERROR.isEmpty())
            return Musics.MENU;

        final var menuMusics = ASSConfig.CONFIG.getMenuMusics();

        if(menuMusics.isEmpty())
            return Musics.MENU;

        final var music = menuMusics.keySet().toArray(new String[0])[RANDOM.nextInt(menuMusics.size())];

        if(music.equals(ASSConfig.MINECRAFT_DEFAULT_CONFIG_KEY))
            return Musics.MENU;
        
        if(MUSIC_MAP.containsKey(music))
            return MUSIC_MAP.get(music);

        final var assMusic = new ASSMusic(new ResourceLocation(VIP3.MOD_ID, music), SoundEvents.MUSIC_MENU, true);
        MUSIC_MAP.put(music, assMusic);
        return assMusic;
    }

    private static Music getGameMusic()
    {
        if(!ClientEventHandler.SOUND_FILE_ERROR.isEmpty())
            return Musics.GAME;

        final var gameMusics = ASSConfig.CONFIG.getGameMusics();

        if(gameMusics.isEmpty())
            return Musics.GAME;

        final var music = gameMusics.keySet().toArray(new String[0])[RANDOM.nextInt(gameMusics.size())];

        if(music.equals(ASSConfig.MINECRAFT_DEFAULT_CONFIG_KEY))
            return Musics.GAME;

        if(MUSIC_MAP.containsKey(music))
            return MUSIC_MAP.get(music);

        final var assMusic = new ASSMusic(new ResourceLocation(VIP3.MOD_ID, music), SoundEvents.MUSIC_GAME, false);
        MUSIC_MAP.put(music, assMusic);
        return assMusic;
    }

    public void updateVolume()
    {
        this.instanceToChannel.forEach((instance, handle) -> {
            if(!(instance instanceof ASSSound assSound))
                return;

            float f = this.calculateVolume(assSound);
            handle.execute((channel) -> channel.setVolume(f));
        });
    }

    @CalledAtRuntime
    public static SoundInstance forMusic(Music music)
    {
        if(music instanceof ASSMusic assMusic)
        {
            final var key = assMusic.getSoundPath().getPath();
            if(SOUND_MUSIC_MAP.containsKey(key))
                return SOUND_MUSIC_MAP.get(key);

            final var sound = new ASSSound(true, assMusic.getEvent(), assMusic.getSoundPath(), false, true, SoundSource.MUSIC, 0, 0, 0);
            SOUND_MUSIC_MAP.put(key, sound);
            return sound;
        }
        return new SimpleSoundInstance(music.getEvent().getLocation(), SoundSource.MUSIC, 1.0F, 1.0F, false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D, true);
    }
}
