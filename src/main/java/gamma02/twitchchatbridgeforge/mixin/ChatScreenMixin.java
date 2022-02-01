package gamma02.twitchchatbridgeforge.mixin;

import gamma02.twitchchatbridgeforge.ClientCommandHelpers.ClientCommandSuggestionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.CommandSuggestionHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {


        @Shadow protected TextFieldWidget inputField;

        @Shadow private int sentHistoryCursor;

        @Shadow private String defaultInputFieldText;

        @Shadow protected abstract void func_212997_a(String p_212997_1_);

        @Shadow private CommandSuggestionHelper commandSuggestionHelper;

        protected ChatScreenMixin(ITextComponent titleIn) {
                super(titleIn);
        }

//        @Redirect(method = "init", at = @At(value = "NEW", target = "Lnet/minecraft/client/gui/CommandSuggestionHelper;<init>(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/client/gui/widget/TextFieldWidget;Lnet/minecraft/client/gui/FontRenderer;ZZIIZI)V"))
//        public CommandSuggestionHelper wrapSuggestionHelper(Minecraft mc, Screen screen, TextFieldWidget inputField, FontRenderer font, boolean commandsOnly, boolean hasCursor, int minAmountRendered, int maxAmountRendered, boolean isChat, int color) {
//                return new ClientCommandSuggestionHelper(mc, screen, inputField, font, commandsOnly, hasCursor, minAmountRendered, maxAmountRendered, isChat, color);
//        }
//        @Overwrite
//        protected void init() {
//                this.minecraft.keyboardListener.enableRepeatEvents(true);
//                this.sentHistoryCursor = this.minecraft.ingameGUI.getChatGUI().getSentMessages().size();
//                this.inputField = new TextFieldWidget(this.font, 4, this.height - 12, this.width - 4, 12, new TranslationTextComponent("chat.editBox")) {
//                        protected IFormattableTextComponent getNarrationMessage() {
//                                return super.getNarrationMessage().appendString(commandSuggestionHelper.getSuggestionMessage());
//                        }
//                };
//                this.inputField.setMaxStringLength(256);
//                this.inputField.setEnableBackgroundDrawing(false);
//                this.inputField.setText(this.defaultInputFieldText);
//                this.inputField.setResponder(this::func_212997_a);
//                this.children.add(this.inputField);
//                this.commandSuggestionHelper = new ClientCommandSuggestionHelper(this.minecraft, this, this.inputField, this.font, false, false, 1, 10, true, -805306368);
//                this.commandSuggestionHelper.init();
//                this.setFocusedDefault(this.inputField);
//        }
        @Redirect(method = "init", at = @At(value = "NEW", target = "net.minecraft.client.gui.CommandSuggestionHelper"))
        public CommandSuggestionHelper wrapSuggestionHelper(Minecraft mc, Screen screen, TextFieldWidget inputField, FontRenderer font, boolean commandsOnly, boolean hasCursor, int minAmountRendered, int maxAmountRendered, boolean isChat, int color){
                return new ClientCommandSuggestionHelper( mc, screen, inputField, font, commandsOnly, hasCursor, minAmountRendered, maxAmountRendered, isChat, color);
        }


}

