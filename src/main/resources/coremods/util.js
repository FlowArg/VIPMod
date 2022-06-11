var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

// https://github.com/UltimateBoomer/mc-smoothboot/blob/1.18/src/main/java/io/github/ultimateboomer/smoothboot/mixin/UtilMixin.java
function initializeCoreMod() {
    return {
        'MinecraftTransformer': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/Util',
                'methodName': '<clinit>',
                'methodDesc': '()V'
            },
            'transformer': function (mn) {
                for(var i = 0; i < mn.instructions.toArray().length; i++) {
                    var node = mn.instructions.toArray()[i];
                    if(node.getOpcode() == Opcodes.LDC) {
                        mn.instructions.set(mn.instructions.toArray()[i + 1], new MethodInsnNode(Opcodes.INVOKESTATIC, "fr/flowarg/vip3/utils/ExecutorReplacement", "makeExecutor", "(Ljava/lang/String;)Ljava/util/concurrent/ExecutorService;"));
                    }

                    if(node.getOpcode() == Opcodes.INVOKESTATIC) {
                        if(node.name == "makeIoExecutor") {
                            node.owner = "fr/flowarg/vip3/utils/ExecutorReplacement";
                        }
                    }
                }
                return mn;
            }
        }
    }
}