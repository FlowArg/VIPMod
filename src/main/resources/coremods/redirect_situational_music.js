var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

function initializeCoreMod() {
    return {
        'MinecraftTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/client/sounds/MusicManager',
                'methodName': 'm_120183_',
                'methodDesc': '()V'
            },
            'transformer': function (mn) {
                for(var i = 0; i < mn.instructions.toArray().length; i++) {
                    var node = mn.instructions.toArray()[i];

                    if(node.getOpcode() == Opcodes.INVOKEVIRTUAL)
                    {
                        if(node.name == ASMAPI.mapMethod("m_91107_")) {
                            mn.instructions.set(node, new MethodInsnNode(Opcodes.INVOKESTATIC, "fr/flowarg/vip3/client/ass/ASSEngine", "getSituationalMusic", "()Lnet/minecraft/sounds/Music;", false));
                            mn.instructions.remove(mn.instructions.toArray()[i - 1]);
                            mn.instructions.remove(mn.instructions.toArray()[i - 2]);
                            break;
                        }
                    }
                }
                return mn;
            }
        }
    }
}