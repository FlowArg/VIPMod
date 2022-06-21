package fr.flowarg.vip3.client.ass;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.flowarg.vip3.client.VCheckbox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
public class ASSConfigScreen extends OptionsSubScreen
{
    private final Screen lastScreen;
    private int midWidth = this.width / 2;
    private MusicList musicList;

    public ASSConfigScreen(Screen lastScreen, Options options)
    {
        super(lastScreen, options, new TranslatableComponent("vip3.ass.title"));
        this.lastScreen = lastScreen;
    }

    public static void reload()
    {
        final var mc = Minecraft.getInstance();
        mc.execute(() -> {
            if(mc.screen instanceof ASSConfigScreen)
                mc.setScreen(mc.screen);
        });
    }

    @Override
    protected void init()
    {
        this.midWidth = this.width / 2;
        this.musicList = new MusicList(this.minecraft, this);
        this.addWidget(this.musicList);
        this.addRenderableWidget(new Button(this.width / 2 - 50, this.height - 29, 100, 20, CommonComponents.GUI_DONE, (p_96713_) -> this.minecraft.setScreen(this.lastScreen)));
    }

    @Override
    public void removed()
    {
        ASSConfig.CONFIG.save();
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        this.renderBackground(poseStack);
        this.musicList.render(poseStack, mouseX, mouseY, partialTick);
        drawCenteredString(poseStack, this.font, this.title, this.midWidth, 10, 0xFFFFFF);
        drawCenteredString(poseStack, this.font, new TranslatableComponent("vip3.ass.menu"), this.midWidth - 100, 25, 0xFFFFFF);
        drawCenteredString(poseStack, this.font, new TranslatableComponent("vip3.ass.game"), this.midWidth + 100, 25, 0xFFFFFF);
        super.render(poseStack, mouseX, mouseY, partialTick);
    }

    private static class MusicList extends ContainerObjectSelectionList<Entry>
    {
        public MusicList(Minecraft mc, @NotNull ASSConfigScreen screen)
        {
            super(mc, screen.width, screen.height, 20, screen.height - 32, 25);
            try(Stream<Path> pathStream = Files.list(Path.of("vipsounds")))
            {
                final AtomicInteger maxTextWidth = new AtomicInteger(screen.font.width(new TranslatableComponent("vip3.ass.default")));

                final List<Path> paths = pathStream.sorted(Comparator.comparing(o -> o.getFileName().toString())).toList();
                paths.forEach(path -> {
                    if(Files.isRegularFile(path))
                        maxTextWidth.set(Math.max(maxTextWidth.get(), screen.font.width(path.getFileName().toString())));
                });

                final var defaultEntry = new MusicListEntry(new TranslatableComponent("vip3.ass.default").getString(), maxTextWidth.get(), true, screen);

                this.addEntry(defaultEntry);
                screen.addWidget(defaultEntry);

                paths.forEach(path -> {
                    if(!Files.isRegularFile(path))
                        return;

                    var entry = new MusicListEntry(path.getFileName().toString(), maxTextWidth.get(), false, screen);
                    this.addEntry(entry);
                    screen.addWidget(entry);
                });

                this.addEntry(new FakeEntry());
                this.addEntry(new FakeEntry());
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        @Override
        public int getScrollbarPosition()
        {
            return super.getScrollbarPosition() + 15 + 20 + 70;
        }

        @Override
        public int getRowWidth()
        {
            return super.getRowWidth() + 32;
        }
    }

    private static abstract class Entry extends ContainerObjectSelectionList.Entry<Entry> {}

    private static class FakeEntry extends Entry
    {
        @Override
        public @NotNull List<? extends NarratableEntry> narratables()
        {
            return List.of();
        }

        @Override
        public void render(@NotNull PoseStack poseStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick)
        {

        }

        @Override
        public @NotNull List<? extends GuiEventListener> children()
        {
            return List.of();
        }
    }

    private static class MusicListEntry extends Entry implements NarratableEntry
    {
        private final String name;
        private final VCheckbox checkboxLeft;
        private final VCheckbox checkboxRight;
        private final ASSVolumeSlider volumeSliderLeft;
        private final ASSVolumeSlider volumeSliderRight;
        private final int maxTextWidth;
        private final Font font = Minecraft.getInstance().font;
        private final TextComponent toRender;
        private final ASSConfigScreen screen;

        public MusicListEntry(@NotNull String path, int maxTextWidth, boolean defaultValue, ASSConfigScreen screen)
        {
            if(defaultValue)
                this.name = "MC_DEFAULT";
            else this.name = path.replace(".ogg", "");

            this.toRender = new TextComponent(path);
            this.screen = screen;
            this.volumeSliderLeft = new ASSVolumeSlider(this, 0, 0, defaultValue ? 1.0f : ASSConfig.CONFIG.getMenuMusics().containsKey(this.name) ? ASSConfig.CONFIG.getMenuMusics().get(this.name).volume() : 1.0f);
            this.volumeSliderRight = new ASSVolumeSlider(this, 0, 0, defaultValue ? 1.0f : ASSConfig.CONFIG.getGameMusics().containsKey(this.name) ? ASSConfig.CONFIG.getGameMusics().get(this.name).volume() : 1.0f);
            this.checkboxLeft = new VCheckbox(0, 0, 19, 19, ASSConfig.CONFIG.getMenuMusics().containsKey(this.name), checkbox -> this.update(true));
            this.checkboxRight = new VCheckbox(0, 0, 19, 19, ASSConfig.CONFIG.getGameMusics().containsKey(this.name), checkbox -> this.update(false));
            this.maxTextWidth = maxTextWidth;

            if(defaultValue)
            {
                this.volumeSliderLeft.active = false;
                this.volumeSliderRight.active = false;
            }
        }

        public void update()
        {
            this.update(true);
            this.update(false);
        }

        public void update(boolean left)
        {
            if(left)
            {
                this.update(this.checkboxLeft, this.volumeSliderLeft, ASSConfig.CONFIG.getMenuMusics());
                return;
            }

            this.update(this.checkboxRight, this.volumeSliderRight, ASSConfig.CONFIG.getGameMusics());
        }

        private void update(@NotNull VCheckbox checkbox, ASSVolumeSlider slider, Map<String, ASSConfigSound> musics)
        {
            if(checkbox.selected())
            {
                if(!musics.containsKey(this.name))
                {
                    musics.put(this.name, new ASSConfigSound(this.name, slider.getValue()));
                    return;
                }

                if(musics.get(this.name).volume() != slider.getValue())
                    musics.replace(this.name, new ASSConfigSound(this.name, slider.getValue()));
            }
            else musics.remove(this.name);
        }

        @Contract(pure = true)
        @Override
        public @NotNull @Unmodifiable List<? extends NarratableEntry> narratables()
        {
            return List.of(this.checkboxLeft, this.checkboxRight, this.volumeSliderLeft, this.volumeSliderRight);
        }

        @Override
        public void render(@NotNull PoseStack poseStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick)
        {
            this.checkboxLeft.x = this.screen.midWidth - this.maxTextWidth / 2 - 30;
            this.checkboxLeft.y = top + 35;
            this.checkboxLeft.render(poseStack, mouseX, mouseY, partialTick);
            drawCenteredString(poseStack, this.font, this.toRender, this.screen.midWidth, top + 35 + (19 - 8) / 2, 0xFFFFFF);

            this.checkboxRight.x = this.screen.midWidth + this.maxTextWidth / 2 + 30 - 19;
            this.checkboxRight.y = top + 35;
            this.checkboxRight.render(poseStack, mouseX, mouseY, partialTick);

            this.volumeSliderLeft.x = this.checkboxLeft.x - this.volumeSliderLeft.getWidth() - 5;
            this.volumeSliderLeft.y = top + 35;
            this.volumeSliderLeft.render(poseStack, mouseX, mouseY, partialTick);

            this.volumeSliderRight.x = this.checkboxRight.x + this.checkboxRight.getWidth() + 5;
            this.volumeSliderRight.y = top + 35;
            this.volumeSliderRight.render(poseStack, mouseX, mouseY, partialTick);
        }

        @Contract(pure = true)
        @Override
        public @NotNull @Unmodifiable List<? extends GuiEventListener> children()
        {
            return List.of(this.checkboxLeft, this.checkboxRight, this.volumeSliderLeft, this.volumeSliderRight);
        }

        @Override
        public @NotNull NarrationPriority narrationPriority()
        {
            return NarrationPriority.NONE;
        }

        @Override
        public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {}

        private static class ASSVolumeSlider extends AbstractSliderButton
        {
            private final MusicListEntry entry;

            public ASSVolumeSlider(MusicListEntry entry, int x, int y, double value)
            {
                super(x, y, 100, 20, TextComponent.EMPTY, value);
                this.entry = entry;
                this.updateMessage();
            }

            @Override
            protected void updateMessage()
            {
                this.setMessage(new TextComponent((int)(this.value * 100.0D) + "%"));
            }

            @Override
            protected void applyValue()
            {
                this.entry.update();
                ASSEngine.unsafeInstance.updateVolume();
            }

            public float getValue()
            {
                return (float)this.value;
            }
        }
    }
}
