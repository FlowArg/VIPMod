var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

function initializeCoreMod() {
    return {
        'ReloadLanguageLanguageSelectScreenTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/client/gui/screens/LanguageSelectScreen',
                'methodName': 'm_96098_',
                'methodDesc': '(Lnet/minecraft/client/gui/components/Button;)V'
            },
            'transformer': function(mn) {
                var instructions = mn.instructions.toArray();
                for(var i = 0; i < instructions.length; i++)
                {
                    var instruction = instructions[i];

                    if(instruction.getOpcode() != Opcodes.ALOAD)
                        continue;

                    var nextNextInstruction = instructions[i+2];

                    if(nextNextInstruction.getOpcode() != Opcodes.INVOKEVIRTUAL || nextNextInstruction.name != ASMAPI.mapMethod("m_91391_"))
                        continue;

                    var insnList = new InsnList();
                    insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/screens/LanguageSelectScreen", ASMAPI.mapField("f_96080_"), "Lnet/minecraft/client/resources/language/LanguageManager;"));
                    insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/screens/LanguageSelectScreen", ASMAPI.mapField("f_96541_"), "Lnet/minecraft/client/Minecraft;"));
                    insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/Minecraft", ASMAPI.mapMethod("m_91098_"), "()Lnet/minecraft/server/packs/resources/ResourceManager;"));
                    insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/resources/language/LanguageManager", ASMAPI.mapMethod("m_6213_"), "(Lnet/minecraft/server/packs/resources/ResourceManager;)V"));

                    mn.instructions.remove(instructions[i + 1]);
                    mn.instructions.remove(nextNextInstruction);
                    mn.instructions.remove(instructions[i + 3]);
                    mn.instructions.insert(instruction, insnList);
                    break;
                }
                return mn;
            }
        }
    }
}