package fr.flowarg.vip3.client.ass;

import com.mojang.blaze3d.audio.OggAudioStream;
import com.mojang.blaze3d.audio.SoundBuffer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.LoopingAudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@OnlyIn(Dist.CLIENT)
public class ASSSoundBufferLibrary extends SoundBufferLibrary
{
    public static final Path VIP_SOUNDS = Path.of("vipsounds");

    public ASSSoundBufferLibrary()
    {
        super(Minecraft.getInstance().getResourceManager());
    }

    @Override
    public @NotNull CompletableFuture<SoundBuffer> getCompleteBuffer(@NotNull ResourceLocation pSoundID)
    {
        return this.cache.computeIfAbsent(pSoundID, (resourceLocation) -> CompletableFuture.supplyAsync(() -> {
            try
            {
                SoundBuffer soundBuffer;
                InputStream inputStream = this.getResource(resourceLocation);

                try(OggAudioStream oggAudioStream = new OggAudioStream(inputStream);)
                {
                    try
                    {
                        final ByteBuffer byteBuffer = oggAudioStream.readAll();
                        soundBuffer = new SoundBuffer(byteBuffer, oggAudioStream.getFormat());
                    } catch (Throwable throwable3)
                    {
                        try
                        {
                            oggAudioStream.close();
                        } catch (Throwable throwable2)
                        {
                            throwable3.addSuppressed(throwable2);
                        }

                        throw throwable3;
                    }
                } catch (Throwable throwable4)
                {
                    if (inputStream != null)
                    {
                        try
                        {
                            inputStream.close();
                        } catch (Throwable throwable1)
                        {
                            throwable4.addSuppressed(throwable1);
                        }
                    }

                    throw throwable4;
                }

                if (inputStream != null)
                    inputStream.close();

                return soundBuffer;
            } catch (IOException ioexception) {
                throw new CompletionException(ioexception);
            }
        }, Util.backgroundExecutor()));
    }

    @Override
    public @NotNull CompletableFuture<AudioStream> getStream(@NotNull ResourceLocation pResourceLocation, boolean pIsWrapper)
    {
        return CompletableFuture.supplyAsync(() -> {
            try
            {
                final InputStream inputStream = this.getResource(pResourceLocation);
                return pIsWrapper ? new LoopingAudioStream(OggAudioStream::new, inputStream) : new OggAudioStream(inputStream);
            }
            catch (IOException e)
            {
                throw new CompletionException(e);
            }
        }, Util.backgroundExecutor());
    }

    private @NotNull InputStream getResource(@NotNull ResourceLocation resourceLocation) throws IOException
    {
        final Path path = VIP_SOUNDS.resolve(resourceLocation.getPath() + ".ogg");
        return Files.newInputStream(path, StandardOpenOption.READ);
    }
}
